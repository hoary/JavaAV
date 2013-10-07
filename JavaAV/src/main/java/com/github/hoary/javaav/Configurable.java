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

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Configurable} provides various methods to configure codecs.
 *
 * @author Alex Andres
 */
public abstract class Configurable {

	static {
		JavaAV.loadLibrary();
	}

	/** The media type, e.g. audio, video, etc. */
	protected MediaType mediaType;

	/** The pixel format for video encoder. */
	protected PixelFormat pixelFormat;

	/** The sample format for audio encoder. */
	protected SampleFormat sampleFormat;

	/** The image width for video encoder. */
	protected int imageWidth;

	/** The image height for video encoder. */
	protected int imageHeight;

	/** The group of pictures for video encoder. */
	protected int gopSize;

	/** The number of audio channels for audio encoder. */
	protected int audioChannels;

	/** The sample rate for audio encoder. */
	protected int sampleRate;

	/** The bitrate for audio and video encoder. */
	protected int bitrate;

	/** The profile for audio and video encoder. */
	protected int profile;

	/** The framerate for video encoder. */
	protected double frameRate;

	/** The audio and video quality for encoder. */
	protected double quality = -1;

	/** List of codec flags for audio and video encoder. */
	protected List<CodecFlag> flags = new ArrayList<CodecFlag>();


	/**
	 * Get the media type, audio, video, etc.
	 *
	 * @return media type.
	 */
	public MediaType getMediaType() {
		return mediaType;
	}

	/**
	 * Set new media type, audio, video, etc.
	 *
	 * @param type the media type to set.
	 */
	public void setMediaType(MediaType type) {
		this.mediaType = type;
	}

	/**
	 * Set new image width.
	 *
	 * @param width new image width.
	 */
	public void setImageWidth(int width) throws JavaAVException {
		this.imageWidth = width;
	}

	/**
	 * Get the image width.
	 *
	 * @return image width.
	 */
	public int getImageWidth() {
		return imageWidth;
	}

	/**
	 * Set new image height.
	 *
	 * @param height new image height.
	 */
	public void setImageHeight(int height) throws JavaAVException {
		this.imageHeight = height;
	}

	/**
	 * Get the image height.
	 *
	 * @return image height.
	 */
	public int getImageHeight() {
		return imageHeight;
	}

	/**
	 * Set group of pictures.
	 *
	 * @param size group of pictures.
	 *
	 * @throws JavaAVException if group of pictures could not be set.
	 */
	public void setGOPSize(int size) throws JavaAVException {
		this.gopSize = size;
	}

	/**
	 * Get group of pictures.
	 *
	 * @return group of pictures.
	 */
	public int getGOPSize() {
		return this.gopSize;
	}

	/**
	 * Set new pixel format.
	 *
	 * @param pixelFormat pixel format.
	 *
	 * @throws JavaAVException if pixel format could not be set.
	 */
	public void setPixelFormat(PixelFormat pixelFormat) throws JavaAVException {
		this.pixelFormat = pixelFormat;
	}

	/**
	 * Get pixel format.
	 *
	 * @return pixel format.
	 */
	public PixelFormat getPixelFormat() {
		return pixelFormat;
	}

	/**
	 * Set new sample format.
	 *
	 * @param sampleFormat sample format.
	 *
	 * @throws JavaAVException if sample format could not be set.
	 */
	public void setSampleFormat(SampleFormat sampleFormat) throws JavaAVException {
		this.sampleFormat = sampleFormat;
	}

	/**
	 * Get sample format.
	 *
	 * @return sample format.
	 */
	public SampleFormat getSampleFormat() {
		return sampleFormat;
	}

	/**
	 * Set new bitrate.
	 *
	 * @param bitrate bitrate.
	 */
	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	/**
	 * Get bitrate.
	 *
	 * @return bitrate.
	 */
	public int getBitrate() {
		return bitrate;
	}

	/**
	 * Set new frame rate.
	 *
	 * @param framerate new frame rate.
	 */
	public void setFramerate(double framerate) throws JavaAVException {
		this.frameRate = framerate;
	}

	/**
	 * Get the frame rate.
	 *
	 * @return frame rate.
	 */
	public double getFramerate() {
		return frameRate;
	}

	/**
	 * Set new sample rate.
	 *
	 * @param samplerate sample rate.
	 */
	public void setSamplerate(int samplerate) {
		this.sampleRate = samplerate;
	}

	/**
	 * Get sample rate.
	 *
	 * @return sample rate.
	 */
	public int getSampleRate() {
		return sampleRate;
	}

	/**
	 * Set new audio channels.
	 *
	 * @param channels audio channels.
	 */
	public void setAudioChannels(int channels) {
		this.audioChannels = channels;
	}

	/**
	 * Get audio channels.
	 *
	 * @return number of audio channels.
	 */
	public int getAudioChannels() {
		return audioChannels;
	}

	/**
	 * Set new quality.
	 *
	 * @param quality new quality.
	 */
	public void setQuality(double quality) {
		this.quality = quality;
	}

	/**
	 * Get quality.
	 *
	 * @return quality.
	 */
	public double getQuality() {
		return quality;
	}

	/**
	 * Set new profile.
	 *
	 * @param profile new profile.
	 */
	public void setProfile(int profile) {
		this.profile = bitrate;
	}

	/**
	 * Get profile.
	 *
	 * @return profile.
	 */
	public int getProfile() {
		return profile;
	}

	/**
	 * Set a codec flag.
	 *
	 * @param flag codec flag.
	 *
	 * @throws JavaAVException if flag is {@code null}.
	 */
	public void setFlag(CodecFlag flag) throws JavaAVException {
		if (flag == null)
			throw new JavaAVException("Could not set codec flag. Null provided.");

		flags.add(flag);
	}

}
