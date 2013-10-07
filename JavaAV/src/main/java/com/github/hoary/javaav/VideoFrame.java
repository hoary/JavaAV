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

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class VideoFrame extends MediaFrame {

	private ByteBuffer data;

	private PixelFormat format;

	private int width;
	private int height;


	public VideoFrame() {
		this(null, 0, 0, null);
	}

	public VideoFrame(ByteBuffer data, int width, int height, PixelFormat format) {
		this.data = data;
		this.width = width;
		this.height = height;
		this.format = format;
	}

	public static VideoFrame create(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		PixelFormat format = Image.getPixelFormat(image);

		return new VideoFrame(Image.createImageBuffer(image), width, height, format);
	}

	public ByteBuffer getData() {
		return data;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public PixelFormat getPixelFormat() {
		return format;
	}

	public PictureFormat getPictureFormat() {
		return new PictureFormat(width, height, format);
	}

	@Override
	public Type getType() {
		return Type.VIDEO;
	}

	@Override
	public boolean hasFrame() {
		return data != null && data.capacity() > 1;
	}
	
}
