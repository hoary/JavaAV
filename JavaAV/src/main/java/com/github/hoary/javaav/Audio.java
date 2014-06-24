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

import java.nio.ByteBuffer;

/**
 * Helper class to convert audio samples into interleaved byte array ready for
 * playback with Java Sound.
 *
 * @author Alex Andres
 */
public class Audio {

	private static final float twoPower7  = 128.0f;
	private static final float twoPower15 = 32768.0f;
	private static final float twoPower23 = 8388608.0f;
	private static final float twoPower31 = 2147483648.0f;

	/**
	 * Get number of bytes per sample for specified sample format.
	 *
	 * @param format the sample format.
	 *
	 * @return number of bytes per sample, or {@code zero} if sample format is unknown.
	 */
	public static int getFormatDepth(SampleFormat format) {
		switch (format) {
			case U8:
			case U8P:
				return 1;
			case S16:
			case S16P:
				return 2;
			case S32:
			case S32P:
				return 4;
			case FLT:
			case FLTP:
				return 4;
			case DBL:
			case DBLP:
				return 8;

			default:
				return 0;
		}
	}

	/**
	 * Read a {@code Number} at specified index from {@code ByteBuffer} for specified
	 * {@code SampleFormat}. Depending on the sample format the value is represented by
	 * a {@code byte, short, integer, float or double} number.
	 *
	 * @param buffer {@code ByteBuffer} with audio samples.
	 * @param format {@code SampleFormat} of audio samples in the buffer.
	 * @param index  the position in the buffer.
	 *
	 * @return a value for a sample format represented by a {@code Number}, or {@code zero}
	 *         if sample format is unknown.
	 */
	public static Number getValue(ByteBuffer buffer, SampleFormat format, int index) {
		switch (format) {
			case U8:
			case U8P:
				return buffer.get(index);
			case S16:
			case S16P:
				return buffer.getShort(index);
			case S32:
			case S32P:
				return buffer.getInt(index);
			case FLT:
			case FLTP:
				return buffer.getFloat(index);
			case DBL:
			case DBLP:
				return buffer.getDouble(index);

			default:
				return 0;
		}
	}

	/**
	 * Get byte array with interleaved signed {@code byte} samples. Since each sample fits in
	 * one byte the possible sample values are within the interval [-2^7, 2^7-1]. It is assumed
	 * that the samples within the {@code AudioFrame} are normalized, meaning in [-1, 1].
	 *
	 * @param frame {@code AudioFrame} with audio samples.
	 *
	 * @return interleaved byte array with signed samples within [-2^7, 2^7-1].
	 */
	public static byte[] getAudio8(AudioFrame frame) {
		BytePointer[] planes = frame.getPlanes();
		SampleFormat format = frame.getAudioFormat().getSampleFormat();
		int depth = getFormatDepth(format);

		int length = planes[0].limit() / depth;
		int channels = planes.length;
		byte[] samples = new byte[channels * length];

		for (int i = 0; i < channels; i++) {
			ByteBuffer buffer = planes[i].asByteBuffer();
			int offset = i * channels;

			for (int j = 0, k = offset; j < length; j++) {
				long sample = quantize8(getValue(buffer, format, j * depth).doubleValue() * twoPower7);
				samples[k++] = (byte) (sample & 0x80);

				// interleave
				k += 2 * (channels - 1);
			}
		}

		return samples;
	}

	/**
	 * Get byte array with interleaved signed two byte samples. Since each sample has a length
	 * of two bytes the possible sample values are within the interval [-2^15, 2^15-1]. It is assumed
	 * that the samples within the {@code AudioFrame} are normalized, meaning in [-1, 1].
	 *
	 * @param frame {@code AudioFrame} with audio samples.
	 *
	 * @return interleaved byte array with signed samples within [-2^15, 2^15-1].
	 */
	public static byte[] getAudio16(AudioFrame frame) {
		BytePointer[] planes = frame.getPlanes();
		SampleFormat format = frame.getAudioFormat().getSampleFormat();
		int depth = getFormatDepth(format);

		int length = planes[0].limit() / depth;
		int channels = planes.length;
		byte[] samples = new byte[channels * length * 2];

		for (int i = 0; i < channels; i++) {
			ByteBuffer buffer = planes[i].asByteBuffer();
			int offset = i * channels;

			for (int j = 0, k = offset; j < length; j++) {
				long sample = quantize16(getValue(buffer, format, j * depth).doubleValue() * twoPower15);
				samples[k++] = (byte) (sample        & 0xFF);
				samples[k++] = (byte) ((sample >> 8) & 0xFF);

				// interleave
				k += 2 * (channels - 1);
			}
		}

		return samples;
	}

	/**
	 * Get byte array with interleaved signed three byte samples. Since each sample has a length
	 * of three bytes the possible sample values are within the interval [-2^23, 2^23-1]. It is assumed
	 * that the samples within the {@code AudioFrame} are normalized, meaning in [-1, 1].
	 *
	 * @param frame {@code AudioFrame} with audio samples.
	 *
	 * @return interleaved byte array with signed samples within [-2^23, 2^23-1].
	 */
	public static byte[] getAudio24(AudioFrame frame) {
		BytePointer[] planes = frame.getPlanes();
		SampleFormat format = frame.getAudioFormat().getSampleFormat();
		int depth = getFormatDepth(format);

		int length = planes[0].limit() / depth;
		int channels = planes.length;
		byte[] samples = new byte[channels * length * 3];

		for (int i = 0; i < channels; i++) {
			ByteBuffer buffer = planes[i].asByteBuffer();
			int offset = i * channels;

			for (int j = 0, k = offset; j < length; j++) {
				long sample = quantize24(getValue(buffer, format, j * depth).doubleValue() * twoPower23);
				samples[k++] = (byte) (sample         & 0xFF);
				samples[k++] = (byte) ((sample >> 8)  & 0xFF);
				samples[k++] = (byte) ((sample >> 16) & 0xFF);

				// interleave
				k += 2 * (channels - 1);
			}
		}

		return samples;
	}

	/**
	 * Get byte array with interleaved signed four byte samples. Since each sample has a length
	 * of four bytes the possible sample values are within the interval [-2^31, 2^31-1]. It is assumed
	 * that the samples within the {@code AudioFrame} are normalized, meaning in [-1, 1].
	 *
	 * @param frame {@code AudioFrame} with audio samples.
	 *
	 * @return interleaved byte array with signed samples within [-2^31, 2^31-1].
	 */
	public static byte[] getAudio32(AudioFrame frame) {
		BytePointer[] planes = frame.getPlanes();
		SampleFormat format = frame.getAudioFormat().getSampleFormat();
		int depth = getFormatDepth(format);

		int length = planes[0].limit() / depth;
		int channels = planes.length;
		byte[] samples = new byte[channels * length * 4];

		for (int i = 0; i < channels; i++) {
			ByteBuffer buffer = planes[i].asByteBuffer();
			int offset = i * channels;

			for (int j = 0, k = offset; j < length; j++) {
				long sample = quantize32(getValue(buffer, format, j * depth).doubleValue() * twoPower31);
				samples[k++] = (byte) (sample         & 0xFF);
				samples[k++] = (byte) ((sample >> 8)  & 0xFF);
				samples[k++] = (byte) ((sample >> 16) & 0xFF);
				samples[k++] = (byte) ((sample >> 24) & 0xFF);

				// interleave
				k += 2 * (channels - 1);
			}
		}

		return samples;
	}

	/**
	 * Get sample value within [-2^7, 2^7-1].
	 *
	 * @param sample the sample value to convert.
	 *
	 * @return the cropped sample value.
	 */
	private static byte quantize8(double sample) {
		if (sample >= 127.0f) {
			return (byte) 127;
		}
		else if (sample <= -128) {
			return (byte) -128;
		}
		else {
			return (byte) (sample < 0 ? (sample - 0.5f) : (sample + 0.5f));
		}
	}

	/**
	 * Get sample value within [-2^15, 2^15-1].
	 *
	 * @param sample the sample value to convert.
	 *
	 * @return the cropped sample value.
	 */
	private static int quantize16(double sample) {
		if (sample >= 32767.0f) {
			return 32767;
		}
		else if (sample <= -32768.0f) {
			return -32768;
		}
		else {
			return (int) (sample < 0 ? (sample - 0.5f) : (sample + 0.5f));
		}
	}

	/**
	 * Get sample value within [-2^23, 2^23-1].
	 *
	 * @param sample the sample value to convert.
	 *
	 * @return the cropped sample value.
	 */
	private static int quantize24(double sample) {
		if (sample >= 8388607.0f) {
			return 8388607;
		}
		else if (sample <= -8388608.0f) {
			return -8388608;
		}
		else {
			return (int) (sample < 0 ? (sample - 0.5f) : (sample + 0.5f));
		}
	}

	/**
	 * Get sample value within [-2^31, 2^31-1].
	 *
	 * @param sample the sample value to convert.
	 *
	 * @return the cropped sample value.
	 */
	private static int quantize32(double sample) {
		if (sample >= 2147483647.0f) {
			return 2147483647;
		}
		else if (sample <= -2147483648.0f) {
			return -2147483648;
		}
		else {
			return (int) (sample < 0 ? (sample - 0.5f) : (sample + 0.5f));
		}
	}
	
}
