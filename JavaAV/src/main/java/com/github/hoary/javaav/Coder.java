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

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avutil.AVDictionary;
import org.bytedeco.javacpp.avutil.AVFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

import static org.bytedeco.javacpp.avcodec.avcodec_alloc_context3;
import static org.bytedeco.javacpp.avcodec.avcodec_alloc_frame;
import static org.bytedeco.javacpp.avcodec.avcodec_close;
import static org.bytedeco.javacpp.avcodec.avcodec_flush_buffers;
import static org.bytedeco.javacpp.avcodec.avcodec_free_frame;
import static org.bytedeco.javacpp.avutil.FF_QP2LAMBDA;
import static org.bytedeco.javacpp.avutil.av_d2q;
import static org.bytedeco.javacpp.avutil.av_dict_free;
import static org.bytedeco.javacpp.avutil.av_dict_set;
import static org.bytedeco.javacpp.avutil.av_free;
import static org.bytedeco.javacpp.avutil.av_get_bytes_per_sample;
import static org.bytedeco.javacpp.avutil.av_get_default_channel_layout;
import static org.bytedeco.javacpp.avutil.av_inv_q;
import static org.bytedeco.javacpp.avutil.av_q2d;

/**
 * {@code Coder} is an abstract representation of an encoder or decoder. This class
 * provides basic functionality to initialize and close this {@code Coders} codec.
 * For an implementation see {@link Encoder} and {@link Decoder}.
 *
 * @author Alex Andres
 */
public abstract class Coder extends Configurable {

	/** The logger. */
	private final static Logger logger = LoggerFactory.getLogger(Coder.class.getName());

	/** Coder state indicates whether the {@code Coder} is opened or closed. */
	public enum State {
		Closed, Opened
	}

	/** Indicates whether last frame was encoded or decoded. */
	protected int[] gotFrame = new int[1];

	/** The codec context. */
	protected AVCodecContext avContext;

	/** Output data of decoder. Input data for encoder. */
	protected AVFrame avFrame;

	/** Input data for decoder. Output data of encoder. */
	protected AVPacket avPacket;

	/** The chosen codec. */
	protected final Codec codec;

	/** Current coder state. */
	protected State state;


	/**
	 * Create a new {@code Coder} with specified codec and context.
	 *
	 * @param codec     the codec.
	 * @param avContext the codec context.
	 */
	Coder(Codec codec, AVCodecContext avContext) {
		this.codec = codec;
		this.avContext = avContext;

		state = State.Closed;
	}

	/**
	 * Initializes the {@code Coder} with codec options that may contain specific
	 * codec parameters.
	 *
	 * @param options codec options.
	 *
	 * @throws JavaAVException if {@code Coder} could not be opened.
	 */
	public void open(Map<String, String> options) throws JavaAVException {
		if (state == State.Opened) {
			logger.warn("Trying to open an already opened Coder. Aborted.");
			return;
		}
		if (codec == null)
			throw new JavaAVException("Codec is null. Aborted.");

		if (avContext == null)
			avContext = avcodec_alloc_context3(codec.getCodec());

		if (avContext == null)
			throw new JavaAVException("No codec context available for codec " + codec.getName());

		// set configuration parameters
		if (pixelFormat != null) {
			avContext.pix_fmt(pixelFormat.value());
		}
		if (sampleFormat != null) {
			int sampleBitSize = av_get_bytes_per_sample(sampleFormat.value()) * 8;
			avContext.sample_fmt(sampleFormat.value());
			avContext.bits_per_raw_sample(sampleBitSize);
		}
		if (imageWidth > 0) {
			avContext.width(imageWidth);
		}
		if (imageHeight > 0) {
			avContext.height(imageHeight);
		}
		if (gopSize > 0) {
			avContext.gop_size(gopSize);
		}
		if (audioChannels > 0) {
			avContext.channels(audioChannels);
			avContext.channel_layout(av_get_default_channel_layout(audioChannels));
		}
		if (sampleRate > 0) {
			avContext.sample_rate(sampleRate);
			avContext.time_base().num(1).den(sampleRate);
		}
		if (frameRate > 0) {
			avContext.time_base(av_inv_q(av_d2q(frameRate, 1001000)));
		}
		if (bitrate > 0) {
			avContext.bit_rate(bitrate);
		}
		if (profile > 0) {
			avContext.profile(profile);
		}
		if (quality > -10) {
			avContext.flags(avContext.flags() | avcodec.CODEC_FLAG_QSCALE);
			avContext.global_quality((int) Math.round(FF_QP2LAMBDA * quality));
		}
		for (CodecFlag flag : flags)
			avContext.flags(avContext.flags() | flag.value());

		AVDictionary avDictionary = new AVDictionary(null);

		if (getQuality() >= 0)
			av_dict_set(avDictionary, "crf", getQuality() + "", 0);

		if (options != null) {
			for (Entry<String, String> e : options.entrySet()) {
				av_dict_set(avDictionary, e.getKey(), e.getValue(), 0);
			}
		}

		if (codec.open(avDictionary, avContext) < 0)
			throw new JavaAVException("Could not open codec.");

		av_dict_free(avDictionary);

		avFrame = avcodec_alloc_frame();

		if (avFrame == null)
			throw new JavaAVException("Could not allocate frame.");

		avPacket = new AVPacket();

		state = State.Opened;
	}

	/**
	 * Close this {@code Coder} and free allocated memory.
	 */
	public void close() {
		if (avFrame != null) {
			avcodec_free_frame(avFrame);
			avFrame = null;
		}

		if (avContext != null && !avContext.isNull()) {
			avcodec_close(avContext);

			if (avContext.extradata() != null)
				av_free(avContext.extradata());

			av_free(avContext);

			avContext = null;
		}

		state = State.Closed;
	}
	
	/**
	 * Flush buffers, should be called when seeking or switching to a
	 * different stream.
	 */
	public void flush() {
		if (avContext == null)
			return;

		avcodec_flush_buffers(avContext);
	}

	/**
	 * Return this coders codec.
	 *
	 * @return codec of this coder.
	 */
	public Codec getCodec() {
		return codec;
	}

	@Override
	public MediaType getMediaType() {
		if (avContext != null)
			return MediaType.byId(avContext.codec_type());

		return super.getMediaType();
	}

	@Override
	public int getImageWidth() {
		if (avContext != null)
			return avContext.width();

		return super.getImageWidth();
	}

	@Override
	public int getImageHeight() {
		if (avContext != null)
			return avContext.height();

		return super.getImageHeight();
	}

	@Override
	public int getGOPSize() {
		if (avContext != null)
			return avContext.gop_size();

		return super.getGOPSize();
	}

	@Override
	public PixelFormat getPixelFormat() {
		if (avContext != null)
			return PixelFormat.byId(avContext.pix_fmt());

		return super.getPixelFormat();
	}

	@Override
	public SampleFormat getSampleFormat() {
		if (avContext != null)
			return SampleFormat.byId(avContext.sample_fmt());

		return super.getSampleFormat();
	}

	@Override
	public int getBitrate() {
		if (avContext != null)
			return avContext.bit_rate();

		return super.getBitrate();
	}

	@Override
	public double getFramerate() {
		if (avContext != null)
			return av_q2d(avContext.time_base());

		return super.getFramerate();
	}

	@Override
	public int getSampleRate() {
		if (avContext != null)
			return avContext.sample_rate();

		return super.getSampleRate();
	}

	@Override
	public int getAudioChannels() {
		if (avContext != null)
			return avContext.channels();

		return super.getAudioChannels();
	}

	@Override
	public double getQuality() {
		if (avContext != null)
			return Math.round(avContext.global_quality() / FF_QP2LAMBDA);

		return super.getQuality();
	}

	@Override
	public int getProfile() {
		if (avContext != null)
			return avContext.profile();

		return super.getProfile();
	}

}
