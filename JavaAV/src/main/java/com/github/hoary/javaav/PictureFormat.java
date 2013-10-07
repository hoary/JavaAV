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

public class PictureFormat {

	private PixelFormat format;

	private int width;

	private int height;


	public PictureFormat(int width, int height, PixelFormat format) {
		this.width = width;
		this.height = height;
		this.format = format;
	}

	/** @return the format */
	public PixelFormat getFormat() {
		return format;
	}

	/** @param format the format to set */
	public void setFormat(PixelFormat format) {
		this.format = format;
	}

	/** @return the width */
	public int getWidth() {
		return width;
	}

	/** @param width the width to set */
	public void setWidth(int width) {
		this.width = width;
	}

	/** @return the height */
	public int getHeight() {
		return height;
	}

	/** @param height the height to set */
	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isValid() {
		return width != 0 && height != 0 && format != PixelFormat.NONE;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final PictureFormat other = (PictureFormat) obj;

		return width == other.width && height == other.height && format == other.format;
	}

	@Override
	public String toString() {
		return "PictureFormat: " + width + "x" + height + " " + format;
	}
	
}
