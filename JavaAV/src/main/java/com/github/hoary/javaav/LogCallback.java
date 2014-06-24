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
import org.bytedeco.javacpp.avutil.Callback_Pointer_int_String_Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.bytedeco.javacpp.avutil.av_log_format_line;

public class LogCallback extends Callback_Pointer_int_String_Pointer {

	private final static Logger LOGGER = LoggerFactory.getLogger(LogCallback.class.getName());

	private boolean printPrefix = true;

	private LogLevel logLevel = LogLevel.INFO;


	@Override
	public void call(Pointer source, int level, String formatStr, Pointer params) {
		if (logLevel.value() < level)
			return;

		byte[] bytes = new byte[1024];
		int prefix = printPrefix ? 1 : 0;

		av_log_format_line(source, level, formatStr, params, bytes, bytes.length, new int[]{prefix});

		String message = new String(bytes).trim();

		switch (logLevel) {
			case PANIC:
			case FATAL:
			case ERROR:
				LOGGER.error(message);  break;
			case WARNING:
				LOGGER.warn(message);   break;
			case INFO:
				LOGGER.info(message);   break;
			case VERBOSE:
			case DEBUG:
				LOGGER.debug(message);  break;
			default:
				break;
		}
	}

	public void setPrintPrefix(boolean printPrefix) {
		this.printPrefix = printPrefix;
	}

	public void setLogLevel(LogLevel level) {
		this.logLevel = level;
	}

}
