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

import org.bytedeco.javacpp.Pointer;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bytedeco.javacpp.avformat.AVFormatContext;
import static org.bytedeco.javacpp.avformat.AVInputFormat;
import static org.bytedeco.javacpp.avformat.av_find_input_format;
import static org.bytedeco.javacpp.avformat.avformat_close_input;
import static org.bytedeco.javacpp.avformat.avformat_open_input;
import static org.bytedeco.javacpp.avutil.AVDictionary;
import static org.bytedeco.javacpp.avutil.av_dict_set;
import static org.bytedeco.javacpp.avutil.av_log_format_line;

/**
 * Convenience class to get video input device capabilities.
 *
 * @author Alex Andres
 */
public class CameraCapabilities {

	/** Resolution pattern. */
	private static Pattern resPattern = Pattern.compile("(\\d{1,4})x(\\d{1,4})");

	/** Maximum frames per second pattern. */
	private static Pattern fpsPattern = Pattern.compile("max .* fps=(\\d{1,4})");

	/**
	 * Get supported image sizes and frame rates that are represented by {@code CameraFormat}
	 * of a video input device. To retrieve the capabilities of the input device the log
	 * output is parsed.
	 *
	 * @param source input source, e.g. vfwcap, dshow (Windows) or video4linux2, v4l2 (Linux).
	 * @param option list formats command, e.g. list_options or list_formats.
	 * @param device device name or identifier, e.g. /dev/video0.
	 *
	 * @return supported camera formats.
	 */
	public static CameraFormat[] probeResolutions(String source, String option, String device) {
		final TreeSet<CameraFormat> set = new TreeSet<CameraFormat>();

		LogCallback callback = new LogCallback() {
			@Override
			public void call(Pointer source, int level, String formatStr, Pointer params) {
				byte[] bytes = new byte[1024];
				// fill the log message with parameters
				av_log_format_line(source, level, formatStr, params, bytes, bytes.length, new int[]{0});

				// parse one log line
				String message = new String(bytes).trim();
				Matcher resMatcher = resPattern.matcher(message);

				CameraFormat format = new CameraFormat();

				// get the maximum resolution
				while (resMatcher.find()) {
					String[] parts = resMatcher.group(0).split("x");

					format.setWidth(Integer.parseInt(parts[0]));
					format.setHeight(Integer.parseInt(parts[1]));
				}

				// get maximum frames per second
				resMatcher = fpsPattern.matcher(message);

				if (resMatcher.find()) {
					format.setFrameRate(Integer.parseInt(resMatcher.group(1)));
				}

				if (format.isValid()) {
					if (!set.add(format)) {
						// if format already exists, then check for current max fps
						Iterator<CameraFormat> iter = set.iterator();
						while (iter.hasNext()) {
							CameraFormat f = iter.next();
							// if same resolution but higher fps, then replace format
							if (f.compareTo(format) == 0 && f.getFrameRate() < format.getFrameRate()) {
								iter.remove();
								set.add(format);
								break;
							}
						}
					}
				}
			}
		};
		// set the new resolution parser callback
		JavaAV.loadLibrary();
		JavaAV.setLogCallback(callback);

		AVFormatContext context = new AVFormatContext(null);
		AVDictionary dict = new AVDictionary(null);
		av_dict_set(dict, option, "true", 0);

		AVInputFormat inputFormat = av_find_input_format(source);
		avformat_open_input(context, "video=" + device, inputFormat, dict);

		if (context != null && !context.isNull())
			avformat_close_input(context);

		// restore to default log callback
		JavaAV.setLogCallback(new LogCallback());

		return set.toArray(new CameraFormat[0]);
	}

}
