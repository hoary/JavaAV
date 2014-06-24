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

import org.bytedeco.javacpp.BytePointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.bytedeco.javacpp.avcodec.AVCodecContext;
import static org.bytedeco.javacpp.avcodec.AVPacket;
import static org.bytedeco.javacpp.avcodec.AVPicture;
import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_MJPEG;
import static org.bytedeco.javacpp.avcodec.AV_FIELD_BB;
import static org.bytedeco.javacpp.avcodec.AV_FIELD_BT;
import static org.bytedeco.javacpp.avcodec.AV_FIELD_PROGRESSIVE;
import static org.bytedeco.javacpp.avcodec.AV_FIELD_TB;
import static org.bytedeco.javacpp.avcodec.AV_FIELD_TT;
import static org.bytedeco.javacpp.avcodec.CODEC_CAP_DELAY;
import static org.bytedeco.javacpp.avcodec.av_copy_packet;
import static org.bytedeco.javacpp.avcodec.av_init_packet;
import static org.bytedeco.javacpp.avcodec.avcodec_encode_audio2;
import static org.bytedeco.javacpp.avcodec.avcodec_encode_video2;
import static org.bytedeco.javacpp.avcodec.avcodec_get_frame_defaults;
import static org.bytedeco.javacpp.avcodec.avpicture_alloc;
import static org.bytedeco.javacpp.avcodec.avpicture_fill;
import static org.bytedeco.javacpp.avcodec.avpicture_get_size;
import static org.bytedeco.javacpp.avutil.AVFrame;
import static org.bytedeco.javacpp.avutil.AVRational;
import static org.bytedeco.javacpp.avutil.AV_NOPTS_VALUE;
import static org.bytedeco.javacpp.avutil.av_d2q;
import static org.bytedeco.javacpp.avutil.av_find_nearest_q_idx;
import static org.bytedeco.javacpp.avutil.av_free;
import static org.bytedeco.javacpp.avutil.av_get_channel_layout_nb_channels;
import static org.bytedeco.javacpp.avutil.av_get_default_channel_layout;
import static org.bytedeco.javacpp.avutil.av_malloc;
import static org.bytedeco.javacpp.avutil.av_q2d;

public class Encoder extends Coder {

	private final static Logger LOGGER = LoggerFactory.getLogger(Encoder.class.getName());

	/** Encoded audio packet buffer */
	private BytePointer audioBuffer;

	/** Encoded video packet buffer */
	private BytePointer videoBuffer;

	/** Encoded audio packet buffer size */
	private int audioBufferSize;

	/** Encoded video packet buffer size */
	private int videoBufferSize;

	/** The encoder picture format */
	private PictureFormat dstVideoFormat;

	/** The encoder audio format */
	private AudioFormat audioFormat;

	/** Source image structure */
	private AVPicture picture;

	/** Encoding video frame buffer */
	private BytePointer pictureBuffer;

	/** Audio re-sampler that is used to convert provided audio frames into encoder audio format */
	private AudioResampler audioResampler;

	/** Picture re-sampler that is used to convert provided pictures into encoder picture format */
	private PictureResampler videoResampler;

	/** Synchronization counter */
	private long sync_opts;


	public Encoder(CodecID codecId) throws JavaAVException {
		this(Codec.getEncoderById(codecId), null);
	}

	public Encoder(Codec codec) throws JavaAVException {
		this(codec, null);
	}

	Encoder(Codec codec, AVCodecContext avContext) throws JavaAVException {
		super(codec, avContext);

		if (this.avContext != null)
			this.avContext.codec_id(codec.getCodec().id());
	}

	@Override
	public void open(Map<String, String> options) throws JavaAVException {
		super.open(options);

		if (codec.hasCapability(CodecCapability.EXPERIMENTAL))
			this.avContext.strict_std_compliance(AVCodecContext.FF_COMPLIANCE_EXPERIMENTAL);

		if (getMediaType() == MediaType.VIDEO) {
			avFrame.pts(0); // required by libx264

			createVideoBuffer();

			dstVideoFormat = new PictureFormat(avContext.width(), avContext.height(), PixelFormat.byId(avContext.pix_fmt()));
		}
		else if (getMediaType() == MediaType.AUDIO) {
			audioFormat = new AudioFormat();
			audioFormat.setSampleFormat(SampleFormat.byId(avContext.sample_fmt()));
			audioFormat.setChannelLayout(ChannelLayout.byId(avContext.channel_layout()));
			audioFormat.setChannels(avContext.channels());
			audioFormat.setSampleRate(avContext.sample_rate());

			audioBufferSize = 256 * 4096;
			audioBuffer = new BytePointer(av_malloc(audioBufferSize));
		}

		state = State.Opened;
	}

	@Override
	public void close() {
		// FIXME
//    	if (picture != null) {
//			avpicture_free(picture);
//			picture = null;
//		}

		if (pictureBuffer != null) {
			av_free(pictureBuffer);
			pictureBuffer = null;
		}
		if (videoBuffer != null) {
			av_free(videoBuffer);
			videoBuffer = null;
		}

		if (videoResampler != null) {
			videoResampler.close();
			videoResampler = null;
		}

		if (audioResampler != null) {
			audioResampler.close();
			audioResampler = null;
		}

		super.close();
	}

	public MediaPacket encodeVideo(VideoFrame frame) throws JavaAVException {
		ByteBuffer imageBuffer = null;

		if (frame != null) {
			imageBuffer = frame.getData();

			int width = frame.getWidth();
			int height = frame.getHeight();
			int step = imageBuffer.capacity() / (width * height) * width;
			int pixelFormat = frame.getPixelFormat().value();

			BytePointer data = new BytePointer(imageBuffer);
			PictureFormat srcVideoFormat = frame.getPictureFormat();

			if (!srcVideoFormat.equals(dstVideoFormat)) {
				if (videoResampler == null)
					videoResampler = new PictureResampler();

				videoResampler.open(srcVideoFormat, dstVideoFormat);

				int codecWidth = avContext.width();
				int codecHeight = avContext.height();

				avpicture_fill(picture, data, pixelFormat, width, height);
				avpicture_fill(new AVPicture(avFrame), pictureBuffer, avContext.pix_fmt(), codecWidth, codecHeight);

				videoResampler.resample(picture, new AVPicture(avFrame));
			}
			else {
				avpicture_fill(new AVPicture(avFrame), data, pixelFormat, width, height);
				avFrame.linesize(0, step);
			}
		}

		av_init_packet(avPacket);
		avPacket.data(videoBuffer);
		avPacket.size(videoBufferSize);

		avFrame.pts(sync_opts);

		if (avFrame.interlaced_frame() != 0) {
			if (avContext.codec().id() == AV_CODEC_ID_MJPEG)
				avContext.field_order(avFrame.top_field_first() != 0 ? AV_FIELD_TT : AV_FIELD_BB);
			else
				avContext.field_order(avFrame.top_field_first() != 0 ? AV_FIELD_TB : AV_FIELD_BT);
		}
		else {
			avContext.field_order(AV_FIELD_PROGRESSIVE);
		}

		avFrame.quality(avContext.global_quality());

		if (avContext.me_threshold() == 0)
			avFrame.pict_type(0);

		if (avcodec_encode_video2(avContext, avPacket, imageBuffer == null ? null : avFrame, gotFrame) < 0)
			throw new JavaAVException("Could not encode video packet.");

		if (gotFrame[0] != 0) {
			if (avPacket.pts() == AV_NOPTS_VALUE && (avContext.codec().capabilities() & CODEC_CAP_DELAY) == 0)
				avPacket.pts(sync_opts);

			MediaPacket mediaPacket = new MediaPacket(avPacket);
			mediaPacket.setKeyFrame(avFrame.key_frame() != 0);

			sync_opts++;

			return mediaPacket;
		}
		else {
			sync_opts++;

			return null;
		}
	}

	public MediaPacket[] encodeAudio(AudioFrame audioFrame) throws JavaAVException {
		if (audioFormat == null)
			throw new JavaAVException("Could not encode audio. No audio format specified.");

		List<MediaPacket> packets = new ArrayList<MediaPacket>();
		AudioFormat srcFormat = audioFrame.getAudioFormat();

		AudioFrame[] frames;

		// create re-sampler if sample formats does not match
		if (!srcFormat.equals(audioFormat)) {
			if (audioResampler == null) {
				audioResampler = new AudioResampler();
				audioResampler.open(srcFormat, audioFormat, avContext.frame_size());
			}

			frames = audioResampler.resample(audioFrame);
		}
		else {
			frames = new AudioFrame[]{audioFrame};
		}

		for (AudioFrame frame : frames) {
			avcodec_get_frame_defaults(avFrame);

			for (int i = 0; i < frame.getPlaneCount(); i++) {
				avFrame.data(i, frame.getPlane(i).position(0));
				avFrame.linesize(i, frame.getPlane(i).limit());
			}

			avFrame.nb_samples(frame.getSampleCount());
			avFrame.quality(avContext.global_quality());

			MediaPacket mediaPacket = encodeAudioFrame(avFrame);
			packets.add(mediaPacket);
		}

		return packets.toArray(new MediaPacket[0]);
	}

	public MediaPacket flushVideo() throws JavaAVException {
		return encodeVideo(null);
	}

	public MediaPacket flushAudio() throws JavaAVException {
		return encodeAudioFrame(null);
	}

	private MediaPacket encodeAudioFrame(AVFrame frame) throws JavaAVException {
		av_init_packet(avPacket);
		avPacket.data(audioBuffer);
		avPacket.size(audioBufferSize);

		if (frame != null) {
			if (frame.pts() == AV_NOPTS_VALUE)
				frame.pts(sync_opts);

			sync_opts = frame.pts() + frame.nb_samples();
		}

		if (avcodec_encode_audio2(avContext, avPacket, frame, gotFrame) < 0)
			throw new JavaAVException("Could not encode audio packet.");

		if (gotFrame[0] != 0) {
			AVPacket copyPacket = new AVPacket();

			// need to copy packet, since the encoder uses common packet buffer
			av_copy_packet(copyPacket, avPacket);

			MediaPacket mediaPacket = new MediaPacket(copyPacket);
			mediaPacket.setKeyFrame(avFrame.key_frame() != 0);

			return mediaPacket;
		}
		else {
			return null;
		}
	}

	private void createVideoBuffer() throws JavaAVException {
		picture = new AVPicture();

		if (avpicture_alloc(picture, avContext.pix_fmt(), avContext.width(), avContext.height()) < 0)
			throw new JavaAVException("Could not allocate decoding picture.");

		// like in ffmpeg.c
		videoBufferSize = Math.max(256 * 1024, 8 * avContext.width() * avContext.height());
		videoBuffer = new BytePointer(av_malloc(videoBufferSize));

		int size = avpicture_get_size(avContext.pix_fmt(), avContext.width(), avContext.height());
		if ((pictureBuffer = new BytePointer(av_malloc(size))).isNull()) {
			close();
			throw new JavaAVException("Could not allocate picture buffer.");
		}
	}

	public Codec getCodec() {
		return codec;
	}

	public void setSampleRate(int samplerate) throws JavaAVException {
		if (codec.getType() != MediaType.AUDIO)
			throw new JavaAVException("Cannot set sample rate for non-audio codec.");

		List<Integer> supportedRates = Arrays.asList(codec.getSupportedSampleRates());

		if (!supportedRates.contains(samplerate)) {
			// pick the highest supported sample rate
			samplerate = supportedRates.get(supportedRates.size() - 1);

			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Sample rate {} is not supported by {}.", samplerate, getCodec().getName());
				LOGGER.warn("-> Selected supported sample rate {}.", samplerate);
			}
		}

		super.setSamplerate(samplerate);
	}

	public void setChannels(int channels) throws JavaAVException {
		if (codec.getType() != MediaType.AUDIO)
			throw new JavaAVException("Cannot set audio channels for non-audio codec.");

		ChannelLayout[] channelLayouts = getCodec().getSupportedChannelLayouts();

		if (channelLayouts != null) {
			long layout = av_get_default_channel_layout(channels);
			ChannelLayout channelLayout = ChannelLayout.byId(layout);

			List<ChannelLayout> layouts = Arrays.asList(channelLayouts);

			if (channelLayout == null || !layouts.contains(channelLayout)) {
				int oldChannels = channels;
				channelLayout = ChannelLayout.STEREO;
				channels = av_get_channel_layout_nb_channels(channelLayout.value());

				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("Codec {} does not support {} channels and channel layout {}.",
							getCodec().getName(), oldChannels, channelLayout.asString());
					LOGGER.warn("-> Selected {} channels as default with channel layout {}.", channels, channelLayout);
				}
			}
		}

		super.setAudioChannels(channels);
	}

	public void setFramerate(double framerate) throws JavaAVException {
		if (codec.getType() != MediaType.VIDEO)
			throw new JavaAVException("Cannot set frame rate for non-video codec.");

		AVRational frameRate = av_d2q(framerate, 1001000);
		AVRational supportedFramerates = codec.getCodec().supported_framerates();

		if (supportedFramerates != null) {
			int idx = av_find_nearest_q_idx(frameRate, supportedFramerates);
			frameRate = supportedFramerates.position(idx);
		}

		super.setFramerate(av_q2d(frameRate));
	}

	public void setImageWidth(int width) throws JavaAVException {
		if (codec.getType() != MediaType.VIDEO)
			throw new JavaAVException("Cannot set image width for non-video codec.");

		if (width < 0)
			throw new JavaAVException("Image width cannot be < 0.");

		super.setImageWidth(width);
	}

	public void setImageHeight(int height) throws JavaAVException {
		if (codec.getType() != MediaType.VIDEO)
			throw new JavaAVException("Cannot set image height for non-video codec.");

		if (height < 0)
			throw new JavaAVException("Image height cannot be < 0.");

		super.setImageHeight(height);
	}

	public void setGOPSize(int gopSize) throws JavaAVException {
		if (codec.getType() != MediaType.VIDEO)
			throw new JavaAVException("Cannot set Group Of Pictures for non-video codec.");

		if (gopSize < 0)
			throw new JavaAVException("Group Of Pictures cannot be < 0.");

		super.setGOPSize(gopSize);
	}

	public void setPixelFormat(PixelFormat format) throws JavaAVException {
		if (codec.getType() != MediaType.VIDEO)
			throw new JavaAVException("Cannot set pixel format for non-video codec.");

		List<PixelFormat> supportedFormats = Arrays.asList(codec.getSupportedPixelFormats());

		if (format == null || !supportedFormats.contains(format)) {
			// pick the first supported pixel format
			format = supportedFormats.get(0);

			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("No valid pixel format provided for codec: {}", getCodec().getName());
				LOGGER.warn("-> Selected default supported pixel format: {}", format);
			}
		}

		super.setPixelFormat(format);
	}

	public void setSampleFormat(SampleFormat format) throws JavaAVException {
		if (codec.getType() != MediaType.AUDIO)
			throw new JavaAVException("Cannot set sample format for non-audio codec.");

		if (format == null) {
			// select one of supported sample formats ourselves
			List<SampleFormat> supported = Arrays.asList(codec.getSupportedSampleFormats());
			// prioritized formats
			SampleFormat[] defaults = {
					SampleFormat.S16, SampleFormat.S16P,
					SampleFormat.S32, SampleFormat.S32P,
					SampleFormat.FLT, SampleFormat.FLTP,
					SampleFormat.DBL, SampleFormat.DBLP
			};

			for (SampleFormat defaultFormat : defaults) {
				if (supported.contains(defaultFormat)) {
					format = defaultFormat;
					break;
				}
			}

			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("No sample format provided for codec: {}", getCodec().getName());
				LOGGER.warn("-> Selected default supported sample format: {}", format);
			}
		}

		if (format == null)
			throw new JavaAVException("Could not set sample format. No format available.");

		super.setSampleFormat(format);
	}

}
