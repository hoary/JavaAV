/*
 * Copyright (C) 2013 Alex Andres
 *
 * This file is part of JavaAV.
 *
 * JavaAV is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version (subject to the "Classpath"
 * exception as provided in the LICENSE file that accompanied
 * this code).
 *
 * JavaAV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JavaAV. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.hoary.javaav;

import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.javacpp.avcodec.AVCodecContext;
import static org.bytedeco.javacpp.avcodec.AVPacket;
import static org.bytedeco.javacpp.avcodec.av_free_packet;
import static org.bytedeco.javacpp.avformat.AVFormatContext;
import static org.bytedeco.javacpp.avformat.AVInputFormat;
import static org.bytedeco.javacpp.avformat.AVStream;
import static org.bytedeco.javacpp.avformat.av_find_input_format;
import static org.bytedeco.javacpp.avformat.av_read_frame;
import static org.bytedeco.javacpp.avformat.avformat_close_input;
import static org.bytedeco.javacpp.avformat.avformat_find_stream_info;
import static org.bytedeco.javacpp.avformat.avformat_open_input;
import static org.bytedeco.javacpp.avutil.AVDictionary;
import static org.bytedeco.javacpp.avutil.AVMEDIA_TYPE_AUDIO;
import static org.bytedeco.javacpp.avutil.AVMEDIA_TYPE_VIDEO;
import static org.bytedeco.javacpp.avutil.AVRational;
import static org.bytedeco.javacpp.avutil.av_d2q;
import static org.bytedeco.javacpp.avutil.av_dict_free;
import static org.bytedeco.javacpp.avutil.av_dict_set;

/**
 * {@code Demuxer} is used to read single media streams from an input source. Media
 * is retrieved by consecutively calling {@link #readFrame()}.
 * A {@code Demuxer} is often used with a complementary {@code Muxer}.
 *
 * @author Alex Andres
 */
public class Demuxer extends Configurable {

	/** Video decoders mapped to input streams. */
	private Map<Integer, Decoder> videoDecoders;

	/** Audio decoders mapped to input streams. */
	private Map<Integer, Decoder> audioDecoders;

	/** The input format context. */
	private AVFormatContext formatContext;

	/** Media packet used while demuxing the source. */
	private AVPacket avPacket;

	/** The input format. */
	private String format = null;


	/**
	 * Initializes the {@code Demuxer} and open the specified input source.
	 *
	 * @param inputSource an input source can be a file, device or a remote source.
	 *
	 * @throws JavaAVException if {@code Demuxer} could not be opened.
	 */
	public void open(String inputSource) throws JavaAVException {
		AVInputFormat inputFormat = null;
		if (format != null && format.length() > 0) {
			inputFormat = av_find_input_format(format);

			if (inputFormat == null)
				throw new JavaAVException("Could not find input format: " + format);
		}

		formatContext = new AVFormatContext(null);

		AVDictionary options = new AVDictionary(null);
		if (frameRate > 0) {
			AVRational r = av_d2q(frameRate, 1001000);
			av_dict_set(options, "framerate", r.num() + "/" + r.den(), 0);
		}
		if (imageWidth > 0 && imageHeight > 0)
			av_dict_set(options, "video_size", imageWidth + "x" + imageHeight, 0);

		if (sampleRate > 0)
			av_dict_set(options, "sample_rate", "" + sampleRate, 0);

		if (audioChannels > 0)
			av_dict_set(options, "channels", "" + audioChannels, 0);

		if (avformat_open_input(formatContext, inputSource, inputFormat, options) < 0)
			throw new JavaAVException("Could not open input: " + inputSource);

		av_dict_free(options);

		// retrieve stream information
		if (avformat_find_stream_info(formatContext, (AVDictionary) null) < 0)
			throw new JavaAVException("Could not find stream information.");

		int streams = formatContext.nb_streams();
		videoDecoders = new HashMap<Integer, Decoder>();
		audioDecoders = new HashMap<Integer, Decoder>();

		// get a pointer to the codec context for the video or audio stream
		for (int index = 0; index < streams; index++) {
			AVStream stream = formatContext.streams(index);
			AVCodecContext context = stream.codec();

			if (context.codec_type() == AVMEDIA_TYPE_VIDEO) {
				initVideoDecoder(index, context);
			}
			else if (context.codec_type() == AVMEDIA_TYPE_AUDIO) {
				initAudioDecoder(index, context);
			}
		}

		if (videoDecoders.isEmpty() && audioDecoders.isEmpty())
			throw new JavaAVException("Could not find any video or audio stream.");

		avPacket = new AVPacket();
	}

	/**
	 * Close this {@code Demuxer} and free allocated memory.
	 */
	public void close() {
		if (formatContext != null && !formatContext.isNull()) {
			avformat_close_input(formatContext);
			formatContext = null;
		}

		/*
		 * Set codec context explicitly to null since avformat_close_input already released it.
		 * This way the decoder knows the context is already closed.
		 */
		for (Integer index : videoDecoders.keySet()) {
			videoDecoders.get(index).getCodec().getContext().setNull();
			videoDecoders.get(index).close();
		}
		for (Integer index : audioDecoders.keySet()) {
			audioDecoders.get(index).getCodec().getContext().setNull();
			audioDecoders.get(index).close();
		}

		videoDecoders = null;
		audioDecoders = null;
	}

	/**
	 * Consecutively retrieves media frames from previously specified input source.
	 * The media type of the returned frame may alter between consecutive calls. One
	 * call may return an audio frame and the next call may return a video frame.
	 *
	 * @return a media frame of audio or video ready to play.
	 *
	 * @throws JavaAVException if media frame could not be retrieved.
	 */
	public MediaFrame readFrame() throws JavaAVException {
		MediaFrame mediaFrame = new MediaFrame();

		while (mediaFrame != null && !mediaFrame.hasFrame()) {
			if (av_read_frame(formatContext, avPacket) < 0) {
				if (videoDecoders.get(avPacket.stream_index()) != null) {
					// video codec may have buffered some frames
					avPacket.data(null);
					avPacket.size(0);
				}
				else {
					return null;
				}
			}

			MediaPacket mediaPacket = new MediaPacket(avPacket);
			Decoder decoder;

			if ((decoder = videoDecoders.get(avPacket.stream_index())) != null) {
				mediaFrame = decoder.decodeVideo(mediaPacket);
			}
			else if ((decoder = audioDecoders.get(avPacket.stream_index())) != null) {
				mediaFrame = decoder.decodeAudio(mediaPacket);
			}

			av_free_packet(avPacket);
			mediaPacket.clear();
		}

		return mediaFrame;
	}

	/**
	 * Set the format of the input source. Usually this is not required since the format
	 * is detected automatically while opening the source.
	 *
	 * @param format the format of the input source.
	 */
	public void setInputFormat(String format) {
		this.format = format;
	}

	/**
	 * Get the format of the specified source.
	 *
	 * @return format of the media source.
	 */
	public String getInputFormat() {
		return formatContext == null ? format : formatContext.iformat().name().getString();
	}

	public int getImageWidth() {
		Decoder decoder = videoDecoders.get(0);
		return decoder == null ? super.getImageWidth() : decoder.getImageWidth();
	}

	public int getImageHeight() {
		Decoder decoder = videoDecoders.get(0);
		return decoder == null ? super.getImageHeight() : decoder.getImageHeight();
	}

	public PixelFormat getPixelFormat() {
		Decoder decoder = videoDecoders.get(0);
		return decoder == null ? super.getPixelFormat() : decoder.getPixelFormat();
	}

	public double getFrameRate() {
		Decoder decoder = videoDecoders.get(0);
		return decoder == null ? super.getFramerate() : decoder.getFramerate();
	}

	public int getAudioChannels() {
		Decoder decoder = audioDecoders.get(0);
		return decoder == null ? super.getAudioChannels() : decoder.getAudioChannels();
	}

	public SampleFormat getSampleFormat() {
		Decoder decoder = audioDecoders.get(0);
		return decoder == null ? super.getSampleFormat() : decoder.getSampleFormat();
	}

	public int getSampleRate() {
		Decoder decoder = audioDecoders.get(0);
		return decoder == null ? super.getSampleRate() : decoder.getSampleRate();
	}

	private void initVideoDecoder(int index, AVCodecContext codecContext) throws JavaAVException {
		if (codecContext == null)
			return;

		CodecID codecId = CodecID.byId(codecContext.codec_id());

		Decoder decoder = new Decoder(codecId, codecContext);
		decoder.setPixelFormat(getPixelFormat());
		decoder.open(null);

		videoDecoders.put(index, decoder);
	}

	private void initAudioDecoder(int index, AVCodecContext codecContext) throws JavaAVException {
		if (codecContext == null)
			return;

		CodecID codecId = CodecID.byId(codecContext.codec_id());

		Decoder decoder = new Decoder(codecId, codecContext);
		decoder.open(null);

		audioDecoders.put(index, decoder);
	}
	
}
