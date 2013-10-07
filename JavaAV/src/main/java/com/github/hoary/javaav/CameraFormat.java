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
 * {@code CameraFormat} represents the image size and frame rate with which
 * a video input device is capturing frames. {@code CameraFormat} also implements
 * {@code Comparable} to compare the image size with other formats.
 *
 * @author Alex Andres
 */
public class CameraFormat implements Comparable<CameraFormat> {

	/** Image width. */
	private int width;

	/** Image height. */
	private int height;

	/** Capturing frame rate. */
	private double frameRate;


	/**
	 * Create a new {@code CameraFormat} with image size and frame rate equal {@code zero}.
	 */
	public CameraFormat() {
		this(0, 0, 0);
	}

	/**
	 * Create a new {@code CameraFormat} with specified image size and frame rate.
	 *
	 * @param width     image width.
	 * @param height    image height.
	 * @param frameRate capturing frame rate.
	 */
	public CameraFormat(int width, int height, double frameRate) {
		this.width = width;
		this.height = height;
		this.frameRate = frameRate;
	}

	/**
	 * Get the image height.
	 *
	 * @return image height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set new image height.
	 *
	 * @param height new image height.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the image width.
	 *
	 * @return image width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set new image width.
	 *
	 * @param width new image width.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the frame rate.
	 *
	 * @return frame rate.
	 */
	public double getFrameRate() {
		return frameRate;
	}

	/**
	 * Set new frame rate.
	 *
	 * @param frameRate new frame rate.
	 */
	public void setFrameRate(double frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * Checks whether this format is valid. A {@code CameraFormat} is valid only if
	 * the image width and height and frame rate are greater {@code zero}.
	 *
	 * @return true if this format is valid, false otherwise.
	 */
	public boolean isValid() {
		return width > 0 && height > 0 && frameRate > 0;
	}

	@Override
	public int compareTo(CameraFormat o) {
		if (o == null)
			return 0;

		if (getWidth() == o.getWidth()) {
			if (getHeight() > o.getHeight())
				return 1;
			if (getHeight() < o.getHeight())
				return -1;

			return 0;
		}
		if (getWidth() > o.getWidth())
			return 1;
		if (getWidth() < o.getWidth())
			return -1;

		return 0;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + width + "x" + height + " FPS: " + frameRate;
	}

}
