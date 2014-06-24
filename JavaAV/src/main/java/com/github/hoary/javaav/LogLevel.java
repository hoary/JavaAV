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

import org.bytedeco.javacpp.avutil;

public enum LogLevel {

	QUIET   (avutil.AV_LOG_QUIET),
	/** Something went really wrong and we will crash now. */
	PANIC   (avutil.AV_LOG_PANIC),
	/** Something went wrong and recovery is not possible. */
	FATAL   (avutil.AV_LOG_FATAL),
	/** Something went wrong and cannot losslessly be recovered. */
	ERROR   (avutil.AV_LOG_ERROR),
	/** Something somehow does not look correct. */
	WARNING (avutil.AV_LOG_WARNING),
	INFO    (avutil.AV_LOG_INFO),
	VERBOSE (avutil.AV_LOG_VERBOSE),
	/** Stuff which is only useful for libav* developers. */
	DEBUG   (avutil.AV_LOG_DEBUG);


	private final int id;


	private LogLevel(int id) {
		this.id = id;
	}

	public final int value() {
		return id;
	}

	public static LogLevel byId(int id) {
		for (LogLevel value : values()) {
			if (value.id == id)
				return value;
		}

		return null;
	}

}
