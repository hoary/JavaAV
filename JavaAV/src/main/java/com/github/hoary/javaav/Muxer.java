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

import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avformat.AVFormatContext;
import org.bytedeco.javacpp.avformat.AVIOContext;
import org.bytedeco.javacpp.avformat.AVOutputFormat;
import org.bytedeco.javacpp.avformat.AVStream;

import static org.bytedeco.javacpp.avcodec.AV_PKT_FLAG_KEY;
import static org.bytedeco.javacpp.avformat.AVFMT_GLOBALHEADER;
import static org.bytedeco.javacpp.avformat.AVFMT_NOFILE;
import static org.bytedeco.javacpp.avformat.AVIO_FLAG_WRITE;
import static org.bytedeco.javacpp.avformat.av_dump_format;
import static org.bytedeco.javacpp.avformat.av_guess_format;
import static org.bytedeco.javacpp.avformat.av_interleaved_write_frame;
import static org.bytedeco.javacpp.avformat.av_write_frame;
import static org.bytedeco.javacpp.avformat.av_write_trailer;
import static org.bytedeco.javacpp.avformat.avformat_alloc_context;
import static org.bytedeco.javacpp.avformat.avformat_new_stream;
import static org.bytedeco.javacpp.avformat.avformat_write_header;
import static org.bytedeco.javacpp.avformat.avio_close;
import static org.bytedeco.javacpp.avformat.avio_open;
import static org.bytedeco.javacpp.avutil.AVDictionary;
import static org.bytedeco.javacpp.avutil.AVRational;
import static org.bytedeco.javacpp.avutil.AV_NOPTS_VALUE;
import static org.bytedeco.javacpp.avutil.av_free;
import static org.bytedeco.javacpp.avutil.av_rescale_q;

public class Muxer extends Configurable {

	private String outputPath;

	private AVOutputFormat outputFormat;
	private AVFormatContext formatContext;

	private AVStream videoStream;
	private AVStream audioStream;

	private Encoder videoEncoder;
	private Encoder audioEncoder;

	private Codec videoCodec;
	private Codec audioCodec;

	private Options videoOptions;
	private Options audioOptions;

	private int videoBitrate;
	private int audioBitrate;

	private double videoQuality = -1;
	private double audioQuality = -1;

	private boolean interleave = true;


	public Muxer(String outputPath) {
		this.outputPath = outputPath;
	}

	public void open() throws Exception {
		formatContext = null;
		videoStream = null;
		audioStream = null;

        /* auto detect the output format from the name. */
		String formatName = null;
		if ((outputFormat = av_guess_format(formatName, outputPath, null)) == null) {
			int proto = outputPath.indexOf("://");
			if (proto > 0) {
				formatName = outputPath.substring(0, proto);
			}
			if ((outputFormat = av_guess_format(formatName, outputPath, null)) == null)
				throw new JavaAVException("Could not guess output format for " + outputPath);
		}

        /* allocate the output media context */
		if ((formatContext = avformat_alloc_context()) == null)
			throw new JavaAVException("Could not allocate format context");

		formatContext.oformat(outputFormat);
		formatContext.filename().putString(outputPath);

		if (getImageWidth() > 0 && getImageHeight() > 0) {
			outputFormat.video_codec(videoCodec.getID().value());

			if ((videoStream = avformat_new_stream(formatContext, videoCodec.getCodec())) == null) {
				release();
				throw new JavaAVException("Could not allocate video stream.");
			}

			videoEncoder = new Encoder(videoCodec, videoStream.codec());
			videoEncoder.setMediaType(MediaType.VIDEO);
			videoEncoder.setBitrate(getVideoBitrate());
			videoEncoder.setImageWidth((getImageWidth() + 15) / 16 * 16);
			videoEncoder.setImageHeight(getImageHeight());
			videoEncoder.setFramerate(getFramerate());
			videoEncoder.setGOPSize(getGOPSize());
			videoEncoder.setQuality(getVideoQuality());
			videoEncoder.setPixelFormat(getPixelFormat());
			videoEncoder.setProfile(AVCodecContext.FF_PROFILE_H264_CONSTRAINED_BASELINE);

			if ((outputFormat.flags() & AVFMT_GLOBALHEADER) != 0)
				videoEncoder.setFlag(CodecFlag.GLOBAL_HEADER);
		}

        /* add an audio output stream */
		if (getAudioChannels() > 0 && getAudioBitrate() > 0 && getSampleRate() > 0) {
			outputFormat.audio_codec(audioCodec.getID().value());

			audioStream = avformat_new_stream(formatContext, audioCodec.getCodec());

			if (audioStream == null) {
				release();
				throw new JavaAVException("Could not allocate audio stream.");
			}

			audioEncoder = new Encoder(audioCodec, audioStream.codec());
			audioEncoder.setMediaType(MediaType.AUDIO);
			audioEncoder.setBitrate(getAudioBitrate());
			audioEncoder.setSampleRate(getSampleRate());
			audioEncoder.setChannels(getAudioChannels());
			audioEncoder.setSampleFormat(getSampleFormat());
			audioEncoder.setQuality(getAudioQuality());

			if ((outputFormat.flags() & AVFMT_GLOBALHEADER) != 0)
				audioEncoder.setFlag(CodecFlag.GLOBAL_HEADER);
		}

		av_dump_format(formatContext, 0, outputPath, 1);

		if (videoStream != null)
			videoEncoder.open(videoOptions);

		if (audioStream != null)
			audioEncoder.open(audioOptions);

        /* open the output file */
		if ((outputFormat.flags() & AVFMT_NOFILE) == 0) {
			AVIOContext pb = new AVIOContext(null);
			if (avio_open(pb, outputPath, AVIO_FLAG_WRITE) < 0) {
				release();
				throw new JavaAVException("Could not open " + outputPath);
			}
			formatContext.pb(pb);
		}

        /* write the stream header*/
		avformat_write_header(formatContext, (AVDictionary) null);
	}

	public void close() throws JavaAVException {
		if (formatContext != null) {
			try {
				/* write buffered frames */
				while (videoStream != null && flushVideo()) ;
				while (audioStream != null && flushAudio()) ;

				if (interleave && videoStream != null && audioStream != null) {
					av_interleaved_write_frame(formatContext, null);
				}
				else {
					av_write_frame(formatContext, null);
				}

				av_write_trailer(formatContext);
			}
			finally {
				release();
			}
		}
	}

	public MediaPacket addImage(VideoFrame frame) throws JavaAVException {
		MediaPacket mediaPacket = videoEncoder.encodeVideo(frame);

		if (mediaPacket != null) {
			AVPacket avPacket = mediaPacket.getAVPacket();

			writeVideoPacket(avPacket);
		}

		return mediaPacket;
	}

	public MediaPacket[] addSamples(AudioFrame frame) throws JavaAVException {
		MediaPacket[] mediaPackets = audioEncoder.encodeAudio(frame);

		for (MediaPacket mediaPacket : mediaPackets) {
			if (mediaPacket == null)
				continue;

			AVPacket avPacket = mediaPacket.getAVPacket();

			if (avPacket == null)
				continue;

			writeAudioPacket(avPacket);
		}

		return mediaPackets;
	}

	public void setVideoCodec(Codec videoCodec) {
		this.videoCodec = videoCodec;
	}

	public void setAudioCodec(Codec audioCodec) {
		this.audioCodec = audioCodec;
	}

	public void setInterleave(boolean interleave) {
		this.interleave = interleave;
	}

	public void setVideoQuality(double videoQuality) {
		this.videoQuality = videoQuality;
	}

	public double getVideoQuality() {
		return videoQuality;
	}

	public void setAudioQuality(double audioQuality) {
		this.audioQuality = audioQuality;
	}

	public double getAudioQuality() {
		return audioQuality;
	}

	public void setVideoOptions(Options options) {
		this.videoOptions = options;
	}

	public void setAudioOptions(Options options) {
		this.audioOptions = options;
	}

	public void setVideoBitrate(int bitrate) {
		this.videoBitrate = bitrate;
	}

	public int getVideoBitrate() {
		return videoBitrate;
	}

	public void setAudioBitrate(int bitrate) {
		this.audioBitrate = bitrate;
	}

	public int getAudioBitrate() {
		return audioBitrate;
	}

	private boolean flushVideo() throws JavaAVException {
		MediaPacket mediaPacket = videoEncoder.flushVideo();

		if (mediaPacket == null)
			return false;

		AVPacket avPacket = mediaPacket.getAVPacket();
		// write flushed video
		writeVideoPacket(avPacket);

		return mediaPacket.isKeyFrame();
	}

	private boolean flushAudio() throws JavaAVException {
		MediaPacket mediaPacket = audioEncoder.flushAudio();

		if (mediaPacket == null)
			return false;

		AVPacket avPacket = mediaPacket.getAVPacket();
		// write flushed audio
		writeAudioPacket(avPacket);

		return true;
	}

	private void writeVideoPacket(AVPacket avPacket) throws JavaAVException {
		AVRational codecTimeBase = videoEncoder.getCodec().getContext().time_base();
		AVRational streamTimeBase = videoStream.time_base();

		if (avPacket.pts() != AV_NOPTS_VALUE)
			avPacket.pts(av_rescale_q(avPacket.pts(), codecTimeBase, streamTimeBase));

		if (avPacket.dts() != AV_NOPTS_VALUE)
			avPacket.dts(av_rescale_q(avPacket.dts(), codecTimeBase, streamTimeBase));

		avPacket.stream_index(videoStream.index());

		synchronized (formatContext) {
	        /* write the compressed frame in the media file */
			if (interleave && audioStream != null) {
				if (av_interleaved_write_frame(formatContext, avPacket) < 0)
					throw new JavaAVException("Could not write interleaved video frame.");
			}
			else {
				if (av_write_frame(formatContext, avPacket) < 0)
					throw new JavaAVException("Could not write video frame.");
			}
		}
	}

	private void writeAudioPacket(AVPacket avPacket) throws JavaAVException {
		AVRational timeBase = audioEncoder.getCodec().getContext().time_base();
		AVRational streamTimeBase = audioStream.time_base();

		if (avPacket.pts() != AV_NOPTS_VALUE)
			avPacket.pts(av_rescale_q(avPacket.pts(), timeBase, streamTimeBase));

		if (avPacket.dts() != AV_NOPTS_VALUE)
			avPacket.dts(av_rescale_q(avPacket.dts(), timeBase, streamTimeBase));

		if (avPacket.duration() > 0)
			avPacket.duration((int) av_rescale_q(avPacket.duration(), timeBase, audioStream.time_base()));

		avPacket.flags(avPacket.flags() | AV_PKT_FLAG_KEY);
		avPacket.stream_index(audioStream.index());

		/* write the compressed frame in the media file */
		synchronized (formatContext) {
			if (interleave && videoStream != null) {
				if (av_interleaved_write_frame(formatContext, avPacket) < 0)
					throw new JavaAVException("Could not write interleaved audio frame.");
			}
			else {
				if (av_write_frame(formatContext, avPacket) < 0)
					throw new JavaAVException("Could not write audio frame.");
			}
		}
	}

	private void release() throws JavaAVException {
		if (videoEncoder != null) {
			videoEncoder.close();
			videoEncoder = null;
		}
		if (audioEncoder != null) {
			audioEncoder.close();
			audioEncoder = null;
		}

		if (formatContext != null && !formatContext.isNull()) {
			if ((outputFormat.flags() & AVFMT_NOFILE) == 0) {
				/* close the output file */
				avio_close(formatContext.pb());
			}

			/* free the streams */
			int nb_streams = formatContext.nb_streams();
			for (int i = 0; i < nb_streams; i++) {
				//av_free(oc.streams(i).codec());
				av_free(formatContext.streams(i));
			}

			/* free the stream */
			av_free(formatContext);
			formatContext = null;
		}

		videoStream = null;
		audioStream = null;
	}

}
