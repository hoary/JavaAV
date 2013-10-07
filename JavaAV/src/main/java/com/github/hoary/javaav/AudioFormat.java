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

/**
 * AudioFormat describes the sample rate, sample format, the number of channels
 * and the channel layout of an audio byte stream.
 *
 * @author Alex Andres
 */
public class AudioFormat {

	/** Audio sample rate. */
	private int sampleRate;

	/** Audio channels. */
	private int channels;

	/** Audio channel layout. */
	private ChannelLayout channelLayout;

	/** Audio sample format. */
	private SampleFormat sampleFormat;


	/** Creates a new {@code AudioFormat} with zero and null values. */
	public AudioFormat() {
		this(null, null, 0, 0);
	}

	/**
	 * Creates a new {@code AudioFormat} with specified values.
	 *
	 * @param sampleFormat  the sample format.
	 * @param channelLayout the channel layout.
	 * @param channels      number of channels.
	 * @param sampleRate    the sample rate.
	 */
	public AudioFormat(SampleFormat sampleFormat, ChannelLayout channelLayout, int channels, int sampleRate) {
		this.sampleFormat = sampleFormat;
		this.channelLayout = channelLayout;
		this.channels = channels;
		this.sampleRate = sampleRate;
	}

	/**
	 * Get the sample rate.
	 *
	 * @return the sample rate.
	 */
	public int getSampleRate() {
		return sampleRate;
	}

	/**
	 * Set the sample rate.
	 *
	 * @param sampleRate the sample rate to set.
	 */
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * Get the number of channels.
	 *
	 * @return the channels.
	 */
	public int getChannels() {
		return channels;
	}

	/**
	 * Set the number of channels.
	 *
	 * @param channels the number channels to set.
	 */
	public void setChannels(int channels) {
		this.channels = channels;
	}

	/**
	 * Get the channel layout.
	 *
	 * @return the channel layout.
	 */
	public ChannelLayout getChannelLayout() {
		return channelLayout;
	}

	/**
	 * Set the channel layout.
	 *
	 * @param channelLayout the channel layout to set.
	 */
	public void setChannelLayout(ChannelLayout channelLayout) {
		this.channelLayout = channelLayout;
	}

	/**
	 * Get the sample format.
	 *
	 * @return the sample format .
	 */
	public SampleFormat getSampleFormat() {
		return sampleFormat;
	}

	/**
	 * Set the sample format.
	 *
	 * @param sampleFormat the sample format to set.
	 */
	public void setSampleFormat(SampleFormat sampleFormat) {
		this.sampleFormat = sampleFormat;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final AudioFormat other = (AudioFormat) obj;

		return sampleFormat == other.sampleFormat && channelLayout == other.channelLayout &&
				channels == other.channels && sampleRate == other.sampleRate;
	}

}
