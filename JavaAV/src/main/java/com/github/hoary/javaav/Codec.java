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

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.avcodec.AVCodec;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avutil.AVDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.bytedeco.javacpp.avcodec.av_codec_is_decoder;
import static org.bytedeco.javacpp.avcodec.av_codec_is_encoder;
import static org.bytedeco.javacpp.avcodec.av_codec_next;
import static org.bytedeco.javacpp.avcodec.avcodec_alloc_context3;
import static org.bytedeco.javacpp.avcodec.avcodec_find_decoder;
import static org.bytedeco.javacpp.avcodec.avcodec_find_decoder_by_name;
import static org.bytedeco.javacpp.avcodec.avcodec_find_encoder;
import static org.bytedeco.javacpp.avcodec.avcodec_find_encoder_by_name;
import static org.bytedeco.javacpp.avcodec.avcodec_open2;
import static org.bytedeco.javacpp.avutil.AVRational;
import static org.bytedeco.javacpp.avutil.av_q2d;

/**
 * {@code Codecs} are usually used by encoders and decoders. To create a {@code Codec} use
 * the {@link #getEncoderById(CodecID)} or {@link #getEncoderByName(String)}, for decoder
 * respectively.
 *
 * @author Alex Andres
 */
public class Codec {

	static {
		JavaAV.loadLibrary();
	}

	/** The codec itself. */
	private AVCodec avCodec;

	/** The codec context. */
	private AVCodecContext avContext;


	/**
	 * Private constructor to prevent creating an empty {@code Codec}.
	 */
	private Codec() {

	}

	/*
	 * The following methods must not be exposed outside the package scope, since
	 * they use FFmpeg classes directly.
	 */

	/**
	 * Initializes the {@code Codec} with a dictionary that may contain specific
	 * codec parameters.
	 *
	 * @param avDictionary dictionary with codec parameters.
	 *
	 * @return zero on success, a negative value on error.
	 *
	 * @throws JavaAVException if the codec could not be opened.
	 */
	int open(AVDictionary avDictionary) throws JavaAVException {
		AVCodecContext avContext = avcodec_alloc_context3(avCodec);

		return open(avDictionary, avContext);
	}

	/**
	 * Initializes the {@code Codec} with a dictionary that may contain specific codec
	 * parameters and a previously created codec context.
	 *
	 * @param avDictionary dictionary with codec parameters.
	 * @param avContext    codec context.
	 *
	 * @return zero on success, a negative value on error.
	 *
	 * @throws JavaAVException if the codec could not be opened.
	 */
	int open(AVDictionary avDictionary, AVCodecContext avContext) throws JavaAVException {
		if (avContext == null)
			throw new JavaAVException("Could not open codec. Codec context is null.");

		this.avContext = avContext;

		return avcodec_open2(avContext, avCodec, avDictionary);
	}

	/**
	 * Get the codec context.
	 *
	 * @return the codec context.
	 */
	AVCodecContext getContext() {
		return avContext;
	}

	/**
	 * Get the codec.
	 *
	 * @return the codec.
	 */
	AVCodec getCodec() {
		return avCodec;
	}

	/*
	 * End of package scoped methods.
	 */


	/**
	 * Create a new encoder with specified codec id.
	 *
	 * @param codecId the codec id.
	 *
	 * @return a new encoder.
	 *
	 * @throws JavaAVException if encoder could not be created.
	 */
	public static Codec getEncoderById(CodecID codecId) throws JavaAVException {
		if (codecId == null)
			throw new NullPointerException("CodecID is null.");

		AVCodec avCodec = avcodec_find_encoder(codecId.value());

		if (avCodec == null || avCodec.isNull())
			throw new JavaAVException("Encoder not found: " + codecId.toString());

		Codec codec = new Codec();
		codec.avCodec = avCodec;

		return codec;
	}

	/**
	 * Create a new decoder with specified codec id.
	 *
	 * @param codecId the codec id.
	 *
	 * @return a new decoder.
	 *
	 * @throws JavaAVException if decoder could not be created.
	 */
	public static Codec getDecoderById(CodecID codecId) throws JavaAVException {
		if (codecId == null)
			throw new NullPointerException("CodecID is null.");

		AVCodec avCodec = avcodec_find_decoder(codecId.value());

		if (avCodec == null || avCodec.isNull())
			throw new JavaAVException("Decoder not found: " + codecId.toString());

		Codec codec = new Codec();
		codec.avCodec = avCodec;

		return codec;
	}

	/**
	 * Create a new encoder with specified codec name.
	 *
	 * @param avCodecName the codec name.
	 *
	 * @return a new encoder.
	 *
	 * @throws JavaAVException if encoder could not be created.
	 */
	public static Codec getEncoderByName(String avCodecName) throws JavaAVException {
		if (avCodecName == null || avCodecName.isEmpty())
			throw new NullPointerException("Codec name is null or empty.");

		AVCodec avCodec = avcodec_find_encoder_by_name(avCodecName);

		if (avCodec == null || avCodec.isNull())
			throw new JavaAVException("Encoder not found: " + avCodecName);

		Codec codec = new Codec();
		codec.avCodec = avCodec;

		return codec;
	}

	/**
	 * Create a new decoder with specified codec name.
	 *
	 * @param avCodecName the codec name.
	 *
	 * @return a new decoder.
	 *
	 * @throws JavaAVException if decoder could not be created.
	 */
	public static Codec getDecoderByName(String avCodecName) throws JavaAVException {
		if (avCodecName == null || avCodecName.isEmpty())
			throw new NullPointerException("Codec name is null or empty.");

		AVCodec avCodec = avcodec_find_decoder_by_name(avCodecName);

		if (avCodec == null || avCodec.isNull())
			throw new JavaAVException("Decoder not found: " + avCodecName);

		Codec codec = new Codec();
		codec.avCodec = avCodec;

		return codec;
	}

	/**
	 * Get all names of codecs that are compiled into FFmpeg.
	 *
	 * @return short codec names that the current FFmpeg version supports.
	 */
	public static String[] getInstalledCodecs() {
		Set<String> names = new TreeSet<String>();

		AVCodec codec = null;
		while ((codec = av_codec_next(codec)) != null) {
			String shortName = codec.name().getString();
			String type = MediaType.byId(codec.type()).toString().substring(0, 1);
			String longName = codec.long_name().getString();

			names.add(String.format("%-17s [%s] %s", shortName, type, longName));
		}

		return names.toArray(new String[0]);
	}

	/**
	 * Get the short name of this {@code Codec}.
	 *
	 * @return the short name of this {@code Codec}.
	 */
	public String getName() {
		if (avCodec == null)
			return null;

		return avCodec.name().getString();
	}

	/**
	 * Get the long name of this {@code Codec}.
	 *
	 * @return the long name of this {@code Codec}.
	 */
	public String getNameLong() {
		if (avCodec == null)
			return null;

		return avCodec.long_name().getString();
	}

	/**
	 * Get the id of this {@code Codec}.
	 *
	 * @return codec id.
	 */
	public CodecID getID() {
		if (avCodec.isNull())
			return null;

		return CodecID.byId(avCodec.id());
	}

	/**
	 * Get the media type of this {@code Codec}.
	 *
	 * @return the media type of this {@code Codec}.
	 */
	public MediaType getType() {
		if (avCodec.isNull())
			return null;

		return MediaType.byId(avCodec.type());
	}

	/**
	 * Determines whether this {@code Codec} is a decoder.
	 *
	 * @return true if this {@code Codec} is a decoder, false otherwise.
	 */
	public boolean canDecode() {
		if (avCodec.isNull())
			return false;

		return av_codec_is_decoder(avCodec) != 0;
	}

	/**
	 * Determines whether this {@code Codec} is a encoder.
	 *
	 * @return true if this {@code Codec} is a encoder, false otherwise.
	 */
	public boolean canEncode() {
		if (avCodec.isNull())
			return false;

		return av_codec_is_encoder(avCodec) != 0;
	}

	/**
	 * Get the codec capabilities.
	 *
	 * @return codec capabilities.
	 */
	public int getCapabilities() {
		return avCodec.capabilities();
	}

	/**
	 * Determines whether this {@code Codec} has the specified capability.
	 *
	 * @param flag the codec capability.
	 *
	 * @return true if codec has the specified capability, false otherwise.
	 */
	public boolean hasCapability(CodecCapability flag) {
		if (avCodec.isNull())
			return false;

		return (avCodec.capabilities() & flag.value()) != 0;
	}

	/**
	 * Get all supported sample formats by this {@code Codec}. If this {@code Codec}
	 * is not an audio codec, then {@code null} is returned.
	 *
	 * @return all supported sample formats by this {@code Codec}.
	 */
	public SampleFormat[] getSupportedSampleFormats() {
		IntPointer sampleFormatsPointer = avCodec.sample_fmts();

		if (getType() != MediaType.AUDIO || sampleFormatsPointer == null)
			return null;

		List<SampleFormat> sampleFormats = new ArrayList<SampleFormat>();

		int format;
		int index = 0;
		while ((format = sampleFormatsPointer.get(index++)) != -1)
			sampleFormats.add(SampleFormat.byId(format));

		return sampleFormats.toArray(new SampleFormat[0]);
	}

	/**
	 * Get all supported sample rates by this {@code Codec}. If this {@code Codec}
	 * is not an audio codec, then {@code null} is returned. The sample rates are
	 * ordered in ascending order.
	 *
	 * @return all supported sample rates by this {@code Codec}.
	 */
	public Integer[] getSupportedSampleRates() {
		IntPointer sampleRatesPointer = avCodec.supported_samplerates();

		if (getType() != MediaType.AUDIO || sampleRatesPointer == null)
			return null;

		List<Integer> sampleRates = new ArrayList<Integer>();

		int sampleRate;
		int index = 0;
		while ((sampleRate = sampleRatesPointer.get(index++)) != 0)
			sampleRates.add(sampleRate);

		// ascending order
		Collections.sort(sampleRates);

		return sampleRates.toArray(new Integer[0]);
	}

	/**
	 * Get all supported frame rates by this {@code Codec}. If this {@code Codec}
	 * is not a video codec, then {@code null} is returned. The frame rates are
	 * ordered in ascending order.
	 *
	 * @return all supported frame rates by this {@code Codec}.
	 */
	public Integer[] getSupportedFrameRates() {
		AVRational frameRates = avCodec.supported_framerates();

		if (getType() != MediaType.VIDEO || frameRates == null)
			return null;

		List<Integer> rates = new ArrayList<Integer>();

		AVRational frameRate;
		int index = 0;
		while ((frameRate = frameRates.position(index++)) != null)
			rates.add((int) av_q2d(frameRate));

		// ascending order
		Collections.sort(rates);

		return rates.toArray(new Integer[0]);
	}

	/**
	 * Get all supported channel layouts by this {@code Codec}. If this {@code Codec}
	 * is not an audio codec, then {@code null} is returned.
	 *
	 * @return all supported channel layouts by this {@code Codec}.
	 */
	public ChannelLayout[] getSupportedChannelLayouts() {
		LongPointer layoutsPointer = avCodec.channel_layouts();

		if (getType() != MediaType.AUDIO || layoutsPointer == null)
			return null;

		List<ChannelLayout> layouts = new ArrayList<ChannelLayout>();

		long layout;
		int index = 0;
		while ((layout = layoutsPointer.get(index++)) != 0)
			layouts.add(ChannelLayout.byId(layout));

		return layouts.toArray(new ChannelLayout[0]);
	}

	/**
	 * Get all supported pixel formats by this {@code Codec}. If this {@code Codec}
	 * is not a video codec, then {@code null} is returned. The pixel formats are
	 * ordered in ascending order.
	 *
	 * @return all supported pixel formats by this {@code Codec}.
	 */
	public PixelFormat[] getSupportedPixelFormats() {
		IntPointer formatsPointer = avCodec.pix_fmts();

		if (getType() != MediaType.VIDEO || formatsPointer == null)
			return null;

		List<PixelFormat> pixelFormats = new ArrayList<PixelFormat>();

		int format;
		int index = 0;
		while ((format = formatsPointer.get(index++)) != -1)
			pixelFormats.add(PixelFormat.byId(format));

		// ascending order
		Collections.sort(pixelFormats);

		return pixelFormats.toArray(new PixelFormat[0]);
	}

}
