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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to capture images from input devices. {@code Camera} is a
 * convenience class that makes use of {@code Demuxer} to retrieve images from
 * input devices.
 *
 * @author Alex Andres
 */
public class Camera {

	/** Represents the current camera state: capturing or not. */
	private final AtomicBoolean open = new AtomicBoolean(false);

	/** The demuxer that captures camera frames. */
	private Demuxer demuxer;

	/** The device name or identifier, e.g. /dev/video0. */
	private String device;

	/** The input source, e.g. vfwcap, dshow (Windows) or video4linux2, v4l2 (Linux) */
	private String format;


	/**
	 * Creates a new {@code Camera} capture for specified device that captures frames
	 * from the specified source.
	 *
	 * @param device device name or identifier, e.g. /dev/video0.
	 * @param source input source, e.g. vfwcap, dshow (Windows) or video4linux2, v4l2 (Linux).
	 */
	public Camera(String device, String source) {
		this.device = device;
		this.format = source;
	}

	/**
	 * Get the camera name or identifier.
	 *
	 * @return camera name or identifier
	 */
	public String getName() {
		return device;
	}

	/**
	 * Retrieve one picture from this camera. The picture will be of type {@code 3BYTE_BGR}.
	 *
	 * @return one picture from this camera, or {@code null} if an error occurs.
	 */
	public BufferedImage getImage() throws JavaAVException {
		if (open.get()) {
			MediaFrame mediaFrame = demuxer.readFrame();
			if (mediaFrame.getType() == MediaFrame.Type.VIDEO) {
				VideoFrame videoFrame = (VideoFrame) mediaFrame;
				BufferedImage image = Image.createImage(videoFrame, BufferedImage.TYPE_3BYTE_BGR);
				return image;
			}
		}
		return null;
	}

	/**
	 * Open the camera with specified image size and capture frame rate.
	 *
	 * @param width     the image width.
	 * @param height    the image height.
	 * @param frameRate the capture frame rate.
	 *
	 * @throws JavaAVException if camera could not be opened.
	 */
	public void open(int width, int height, double frameRate) throws JavaAVException {
		if (!open.get()) {
			String input = "video=" + device;

			demuxer = new Demuxer();
			demuxer.setInputFormat(format);
			demuxer.setImageWidth(width);
			demuxer.setImageHeight(height);
			demuxer.setFramerate(frameRate);
			demuxer.open(input);

			open.set(true);
		}
	}

	/**
	 * Close the camera and free associated memory. Subsequent calls of {@link #getImage()}
	 * will return {@code null}.
	 */
    public void close() {
        if (open.compareAndSet(true, false) && demuxer != null) {
            demuxer.close();
        }
    }

	/**
	 * Checks whether this camera is opened.
	 *
	 * @return true if camera is opened, false otherwise.
	 */
	public boolean isOpen() {
		return open.get();
	}

}
