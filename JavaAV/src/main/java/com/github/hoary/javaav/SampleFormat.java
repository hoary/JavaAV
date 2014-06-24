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

public enum SampleFormat {

	NONE    (avutil.AV_SAMPLE_FMT_NONE),

	/** unsigned 8 bits */
	U8      (avutil.AV_SAMPLE_FMT_U8),
	/** signed 16 bits */
	S16     (avutil.AV_SAMPLE_FMT_S16),
	/** signed 32 bits */
	S32     (avutil.AV_SAMPLE_FMT_S32),
	/** float */
	FLT     (avutil.AV_SAMPLE_FMT_FLT),
	/** double */
	DBL     (avutil.AV_SAMPLE_FMT_DBL),

	/** unsigned 8 bits, planar */
	U8P     (avutil.AV_SAMPLE_FMT_U8P),
	/** signed 16 bits, planar */
	S16P    (avutil.AV_SAMPLE_FMT_S16P),
	/** signed 32 bits, planar */
	S32P    (avutil.AV_SAMPLE_FMT_S32P),
	/** float, planar */
	FLTP    (avutil.AV_SAMPLE_FMT_FLTP),
	/** double, planar */
	DBLP    (avutil.AV_SAMPLE_FMT_DBLP);


	private final int id;


	private SampleFormat(int id) {
		this.id = id;
	}

	public final int value() {
		return id;
	}

	public static SampleFormat byId(int id) {
		for (SampleFormat value : values()) {
			if (value.id == id)
				return value;
		}

		return null;
	}
	
}
