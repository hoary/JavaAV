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

import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.swresample.SwrContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.avutil.AV_ROUND_DOWN;
import static org.bytedeco.javacpp.avutil.AV_SAMPLE_FMT_NONE;
import static org.bytedeco.javacpp.avutil.av_get_bytes_per_sample;
import static org.bytedeco.javacpp.avutil.av_get_channel_layout_nb_channels;
import static org.bytedeco.javacpp.avutil.av_opt_set_int;
import static org.bytedeco.javacpp.avutil.av_rescale_rnd;
import static org.bytedeco.javacpp.avutil.av_sample_fmt_is_planar;
import static org.bytedeco.javacpp.swresample.swr_alloc;
import static org.bytedeco.javacpp.swresample.swr_convert;
import static org.bytedeco.javacpp.swresample.swr_free;
import static org.bytedeco.javacpp.swresample.swr_init;

/**
 * The AudioResampler converts audio samples from one audio format to another.
 *
 * @author Alex Andres
 */
public class AudioResampler {

	/** The re-sample context */
	private SwrContext convertContext;

	/** Re-sampled sample buffer */
	private RingBuffer buffer;

	/** Amount of samples per output frame */
	private int frameSamples;

	/** Input audio format */
	private AudioFormat srcFormat;

	/** Output audio format */
	private AudioFormat dstFormat;
	
	
	/**
	 * Initializes the {@code AudioResampler} with specified input and output audio formats.
	 * Invocations of {@link #resample(AudioFrame)} will return audio frames with specified
	 * output audio format. The audio format of provided audio frames must be equal to the
	 * input audio format specified in this method.
	 *
	 * @param srcFormat the input audio format.
	 * @param dstFormat the output audio format.
	 * @param frameSamples amount of samples per output frame.
	 *
	 * @throws JavaAVException if resampler cannot be opened.
	 */
	public void open(AudioFormat srcFormat, AudioFormat dstFormat, int frameSamples) throws JavaAVException {
		if (srcFormat == null)
			throw new JavaAVException("Invalid input audio format provided: " + srcFormat);
		if (dstFormat == null)
			throw new JavaAVException("Invalid output audio format provided: " + dstFormat);

		if (srcFormat.equals(dstFormat))
			return;

		this.srcFormat = srcFormat;
		this.dstFormat = dstFormat;

		this.frameSamples = frameSamples;

		convertContext = swr_alloc();

		if (convertContext == null)
			throw new JavaAVException("Could not allocate the audio conversion context.");

		long dstChannelLayout = dstFormat.getChannelLayout().value();
		long srcChannelLayout = srcFormat.getChannelLayout().value();

		av_opt_set_int(convertContext, "ocl", dstChannelLayout, 0);
		av_opt_set_int(convertContext, "osf", dstFormat.getSampleFormat().value(), 0);
		av_opt_set_int(convertContext, "osr", dstFormat.getSampleRate(), 0);
		av_opt_set_int(convertContext, "icl", srcChannelLayout, 0);
		av_opt_set_int(convertContext, "isf", srcFormat.getSampleFormat().value(), 0);
		av_opt_set_int(convertContext, "isr", srcFormat.getSampleRate(), 0);
		av_opt_set_int(convertContext, "tsf", AV_SAMPLE_FMT_NONE, 0);
		av_opt_set_int(convertContext, "ich", av_get_channel_layout_nb_channels(srcChannelLayout), 0);
		av_opt_set_int(convertContext, "och", av_get_channel_layout_nb_channels(dstChannelLayout), 0);
		av_opt_set_int(convertContext, "uch", 0, 0);

		if (swr_init(convertContext) < 0)
			throw new JavaAVException("Could not initialize the conversion context.");

		buffer = new RingBuffer(1024 * 1000, dstFormat.getChannels());
	}

	/**
	 * Resample audio samples within the provided audio frame. The audio format of the input
	 * audio frame must be equal to the audio format previously specified with
	 * {@link #open(AudioFormat, AudioFormat, int)}. The resampled samples are buffered, if
	 * they do not fit completely in one output frame. Depending on output audio format more
	 * than one frame may be returned.
	 *
	 * @param frame the audio frame with samples to be resampled.
	 *
	 * @return one or more audio frames with resampled audio.
	 */
	public AudioFrame[] resample(AudioFrame frame) {
		List<AudioFrame> frames = new ArrayList<AudioFrame>();

		int outputChannels = dstFormat.getChannels();
		int outputRate = dstFormat.getSampleRate();
		int outputFormat = dstFormat.getSampleFormat().value();
		int inputRate = srcFormat.getSampleRate();

		int planes = av_sample_fmt_is_planar(outputFormat) != 0 ? outputChannels : 1;
		int destSamples = Math.max(frameSamples, (int) av_rescale_rnd(frameSamples, outputRate, inputRate, AV_ROUND_DOWN));

		// use enough space to avoid buffering
		AudioFrame tempFrame = new AudioFrame(dstFormat, destSamples * 4);

		int inSamples = 0;
		int outSamples = frameSamples * 4; // make sure we get everything out

		PointerPointer inPointer = null;

		if (frame != null) {
			inSamples = frame.getSampleCount();
			inPointer = frame.getData();
		}

		if (outSamples <= 0)
			outSamples = inSamples;

		int resampled = swr_convert(convertContext, tempFrame.getData(), outSamples, inPointer, inSamples);

		// re-sampled plane size
		int limit = (resampled * outputChannels * av_get_bytes_per_sample(outputFormat)) / planes;

		// write samples into buffer in case the current frame cannot be fully filled
		for (int i = 0; i < tempFrame.getPlaneCount(); i++) {
			ByteBuffer buf = tempFrame.getPlane(i).asByteBuffer();
			buf.limit(Math.min(limit, buf.capacity()));

			buffer.write(i, buf);
		}

		// output frame plane size
		int bufferSize = (destSamples * outputChannels * av_get_bytes_per_sample(outputFormat)) / planes;

		// get buffered samples that fit in one frame
		while (buffer.available() >= bufferSize) {
			AudioFrame outFrame = new AudioFrame(dstFormat, destSamples);

			// go through each buffered plane
			for (int i = 0; i < planes; i++) {
				byte[] data = new byte[bufferSize];

				try {
					buffer.read(i, data);
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				outFrame.getPlane(i).position(0).put(data);
			}

			outFrame.setSampleCount(frameSamples);

			frames.add(outFrame);
		}

		return frames.toArray(new AudioFrame[0]);
	}

	/**
	 * Close this resampler and free allocated memory. Subsequent calls of
	 * {@link #resample(AudioFrame)} will cause errors.
	 */
	public void close() {
		if (convertContext != null) {
			swr_free(convertContext);
			convertContext = null;
		}
	}
	
}
