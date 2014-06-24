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
 * Enumeration of all supported channel layouts. A {@code ChannelLayout} can be
 * retrieved by passing a FFmpeg channel layout id to {@link #byId(long)}.
 *
 * @author Alex Andres
 */
public enum ChannelLayout {

	MONO                (avutil.AV_CH_LAYOUT_MONO,              "Mono"),
	STEREO              (avutil.AV_CH_LAYOUT_STEREO,            "Stereo"),
	_2POINT1            (avutil.AV_CH_LAYOUT_2POINT1,           "2.1"),
	_2_1                (avutil.AV_CH_LAYOUT_2_1,               "2_1"),
	SURROUND            (avutil.AV_CH_LAYOUT_SURROUND,          "Surround"),
	_3POINT1            (avutil.AV_CH_LAYOUT_3POINT1,           "3.1"),
	_4POINT0            (avutil.AV_CH_LAYOUT_4POINT0,           "4.0"),
	_4POINT1            (avutil.AV_CH_LAYOUT_4POINT1,           "4.1"),
	_2_2                (avutil.AV_CH_LAYOUT_2_2,               "2_2"),
	QUAD                (avutil.AV_CH_LAYOUT_QUAD,              "Quad"),
	_5POINT0            (avutil.AV_CH_LAYOUT_5POINT0,           "5.0"),
	_5POINT1            (avutil.AV_CH_LAYOUT_5POINT1,           "5.1"),
	_5POINT0_BACK       (avutil.AV_CH_LAYOUT_5POINT0_BACK,      "5.0 Back"),
	_5POINT1_BACK       (avutil.AV_CH_LAYOUT_5POINT1_BACK,      "5.1 Back"),
	_6POINT0            (avutil.AV_CH_LAYOUT_6POINT0,           "6.0"),
	_6POINT0_FRONT      (avutil.AV_CH_LAYOUT_6POINT0_FRONT,     "6.0 Front"),
	HEXAGONAL           (avutil.AV_CH_LAYOUT_HEXAGONAL,         "Hexagonal"),
	_6POINT1            (avutil.AV_CH_LAYOUT_6POINT1,           "6.1"),
	_6POINT1_BACK       (avutil.AV_CH_LAYOUT_6POINT1_BACK,      "6.1 Back"),
	_6POINT1_FRONT      (avutil.AV_CH_LAYOUT_6POINT1_FRONT,     "6.1 Front"),
	_7POINT0            (avutil.AV_CH_LAYOUT_7POINT0,           "7.0"),
	_7POINT0_FRONT      (avutil.AV_CH_LAYOUT_7POINT0_FRONT,     "7.0 Front"),
	_7POINT1            (avutil.AV_CH_LAYOUT_7POINT1,           "7.1"),
	_7POINT1_WIDE       (avutil.AV_CH_LAYOUT_7POINT1_WIDE,      "7.1 Wide"),
	_7POINT1_WIDE_BACK  (avutil.AV_CH_LAYOUT_7POINT1_WIDE_BACK, "7.1 Wide Back"),
	OCTAGONAL           (avutil.AV_CH_LAYOUT_OCTAGONAL,         "Octagonal"),
	STEREO_DOWNMIX      (avutil.AV_CH_LAYOUT_STEREO_DOWNMIX,    "Stereo Downmix");


	/** FFmpeg channel layout id. */
	private final long id;

	/** Channel layout description. */
	private final String name;


	/**
	 * Create a new {@code ChannelLayout}.
	 *
	 * @param id   FFmpeg channel layout id.
	 * @param name layout description.
	 */
	private ChannelLayout(long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Get the channel layout id defined in FFmpeg.
	 *
	 * @return FFmpeg channel layout id.
	 */
	public final long value() {
		return id;
	}

	/**
	 * Get the channel layout description.
	 *
	 * @return channel layout description.
	 */
	public final String asString() {
		return name;
	}

	/**
	 * Get a {@code ChannelLayout} that matches to the specified FFmpeg id.
	 *
	 * @param id FFmpeg channel layout id.
	 *
	 * @return matching channel layout, or {@code null} if id is not defined.
	 */
	public static ChannelLayout byId(long id) {
		for (ChannelLayout value : values()) {
			if (value.id == id)
				return value;
		}

		return null;
	}
	
}
