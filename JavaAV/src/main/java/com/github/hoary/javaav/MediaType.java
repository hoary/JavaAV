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

/**
 * Enumeration of all media types. {@code MediaType} describes the type of media content
 * e.g. audio, video, etc. A {@code MediaType} can be retrieved by passing a FFmpeg media
 * type id to {@link #byId(int)}.
 *
 * @author Alex Andres
 */
public enum MediaType {

	UNKNOWN     (avutil.AVMEDIA_TYPE_UNKNOWN),
	VIDEO       (avutil.AVMEDIA_TYPE_VIDEO),
	AUDIO       (avutil.AVMEDIA_TYPE_AUDIO),
	DATA        (avutil.AVMEDIA_TYPE_DATA),
	SUBTITLE    (avutil.AVMEDIA_TYPE_SUBTITLE),
	ATTACHMENT  (avutil.AVMEDIA_TYPE_ATTACHMENT),
	NB          (avutil.AVMEDIA_TYPE_NB);


	/** FFmpeg media type id. */
	private final int id;


	/**
	 * Create a new {@code MediaType}.
	 *
	 * @param id FFmpeg media type id.
	 */
	private MediaType(int id) {
		this.id = id;
	}

	/**
	 * Get the media type id defined in FFmpeg.
	 *
	 * @return FFmpeg media type id.
	 */
	public final int value() {
		return id;
	}

	/**
	 * Get a {@code MediaType} that matches to the specified FFmpeg id.
	 *
	 * @param id FFmpeg media type id.
	 *
	 * @return matching media type, or {@code null} if id is not defined.
	 */
	public static MediaType byId(int id) {
		for (MediaType value : values()) {
			if (value.id == id)
				return value;
		}

		return null;
	}
	
}
