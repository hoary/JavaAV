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

import com.googlecode.javacv.CanvasFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CameraExample {

	static boolean reading = true;

	public static void main(String[] args) throws Exception {
		String cameraName = "Integrated Webcam";
		String format = "dshow";

		CanvasFrame frame = new CanvasFrame("Camera Test");
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				reading = false;
			}
		});

		Camera camera = new Camera(cameraName, format);
		camera.open(640, 480, 25);

		while (reading && camera.isOpen()) {
			frame.showImage(camera.getImage());
		}

		camera.close();
		frame.dispose();
	}

}