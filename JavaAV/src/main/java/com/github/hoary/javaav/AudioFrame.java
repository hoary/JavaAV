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
import org.bytedeco.javacpp.PointerPointer;

import static org.bytedeco.javacpp.avutil.av_free;
import static org.bytedeco.javacpp.avutil.av_get_bytes_per_sample;
import static org.bytedeco.javacpp.avutil.av_malloc;
import static org.bytedeco.javacpp.avutil.av_sample_fmt_is_planar;

/**
 * An {@code AudioFrame} contains audio samples with a specific {@code AudioFormat}
 * that describes the characteristics of the samples. Depending on how many channels
 * the audio of this frame has, each channel can be accessed with {@code getPlane()}.
 *
 * @author Alex Andres
 */
public class AudioFrame extends MediaFrame {

	/** The format that describes the samples within this frame. */
	private AudioFormat format;

	/** Pointer for each audio plane. */
	private PointerPointer samplePointer;

	/** Pointer for each audio plane with samples. */
	private BytePointer[] planePointers;

	/** The number of samples per audio plane. */
	private int samples;


	/**
	 * Creates a new {@code AudioFrame} with descriptive format and allocates memory
	 * for the specified amount of samples.
	 *
	 * @param format  the format of the samples.
	 * @param samples the amount of samples.
	 */
	public AudioFrame(AudioFormat format, int samples) {
		int channels = format.getChannels();
		int sampleFormat = format.getSampleFormat().value();
		int planes = av_sample_fmt_is_planar(sampleFormat) != 0 ? channels : 1;
		int planeLength = (samples * channels * av_get_bytes_per_sample(sampleFormat)) / planes;

		this.format = format;
		this.samples = samples;
		this.planePointers = new BytePointer[planes];
		this.samplePointer = new PointerPointer(planes);

		for (int i = 0; i < planes; i++) {
			this.planePointers[i] = new BytePointer(av_malloc(planeLength)).capacity(planeLength);
			this.planePointers[i].limit(planeLength);
			this.samplePointer.put(i, planePointers[i]);
		}
	}

	/**
	 * Get the audio format of containing samples.
	 *
	 * @return the audio format.
	 */
	public AudioFormat getAudioFormat() {
		return format;
	}

	/**
	 * Get sample data pointers.
	 *
	 * @return pointers to samples.
	 */
	public PointerPointer getData() {
		return samplePointer;
	}

	/**
	 * Get buffer size per plane.
	 *
	 * @return buffer size per plane.
	 */
	public int getBufferSize() {
		return planePointers[0].capacity();
	}

	/**
	 * Get pointers to all audio planes.
	 *
	 * @return pointers to all audio planes.
	 */
	public BytePointer[] getPlanes() {
		return planePointers;
	}

	/**
	 * Get pointer to audio plane at specified index.
	 *
	 * @param index the plane index.
	 *
	 * @return pointer to an audio plane.
	 */
	public BytePointer getPlane(int index) {
		return planePointers[index];
	}

	/**
	 * Get the number of audio planes/channels.
	 *
	 * @return number of planes.
	 */
	public int getPlaneCount() {
		return planePointers.length;
	}

	/**
	 * Set the number of samples in this frame.
	 *
	 * @param samples the number of samples to set.
	 */
	public void setSampleCount(int samples) {
		this.samples = samples;
	}

	/**
	 * Get the number of samples in this frame.
	 *
	 * @return number of samples in this frame.
	 */
	public int getSampleCount() {
		return samples;
	}

	@Override
	public Type getType() {
		return Type.AUDIO;
	}

	@Override
	public boolean hasFrame() {
		return planePointers != null && planePointers.length > 0;
	}

	/**
	 * Free the memory of sample buffers.
	 */
	public void clear() {
		if (planePointers != null) {
			for (int i = 0; i < getPlaneCount(); i++) {
				av_free(planePointers[i].position(0));
			}
			planePointers = null;
		}
	}
	
}
