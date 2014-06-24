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

import static org.bytedeco.javacpp.avcodec.avcodec_register_all;
import static org.bytedeco.javacpp.avdevice.avdevice_register_all;
import static org.bytedeco.javacpp.avformat.av_register_all;
import static org.bytedeco.javacpp.avformat.avformat_network_init;
import static org.bytedeco.javacpp.avutil.av_log_set_callback;

public final class JavaAV {

	/** Indicates whether the native libraries were loaded or not. */
	private static boolean loaded = false;

	/**
	 * FFmpeg log callback function. Must be kept in memory, otherwise the pointer
	 * is freed and causes a crash. */
	private static LogCallback logCallback;


	public static void setLogCallback(LogCallback callback) {
		logCallback = callback;

		av_log_set_callback(logCallback);
	}

	public static void loadLibrary() {
		if (loaded)
			return;

		av_register_all();
		avcodec_register_all();
		avdevice_register_all();
		avformat_network_init();

		loaded = true;
	}

}
