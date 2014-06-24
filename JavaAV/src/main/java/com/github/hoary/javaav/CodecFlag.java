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

import org.bytedeco.javacpp.avcodec;

/**
 * Enumeration of all possible codec flags.
 *
 * NOTE: The codec flag comments were taken from avcodec.h.
 *
 * @author Alex Andres
 */
public enum CodecFlag {

	/**  */
	INPUT_PRESERVED     (avcodec.CODEC_FLAG_INPUT_PRESERVED),

	/** Use internal 2pass rate control in first pass mode. */
	PASS1               (avcodec.CODEC_FLAG_PASS1),

	/** Use internal 2pass rate control in second pass mode. */
	PASS2               (avcodec.CODEC_FLAG_PASS2),

	/** Only decode/encode grayscale. */
	GRAY                (avcodec.CODEC_FLAG_GRAY),

	/** Don't draw edges. */
	EMU_EDGE            (avcodec.CODEC_FLAG_EMU_EDGE),

	/** error[?] variables will be set during encoding. */
	PSNR                (avcodec.CODEC_FLAG_PSNR),

	/** Input bitstream might be truncated at a random location instead of only at frame boundaries. */
	TRUNCATED           (avcodec.CODEC_FLAG_TRUNCATED),

	/** Normalize adaptive quantization. */
	NORMALIZE_AQP       (avcodec.CODEC_FLAG_NORMALIZE_AQP),

	/** Use interlaced DCT. */
	INTERLACED_DCT      (avcodec.CODEC_FLAG_INTERLACED_DCT),

	/** Force low delay. */
	LOW_DELAY           (avcodec.CODEC_FLAG_LOW_DELAY),

	/** Place global headers in extradata instead of every keyframe. */
	GLOBAL_HEADER       (avcodec.CODEC_FLAG_GLOBAL_HEADER),

	/** Use only bitexact stuff (except (I)DCT). */
	BITEXACT            (avcodec.CODEC_FLAG_BITEXACT),

    /* Fx : Flag for h263+ extra options */
	/** H.263 advanced intra coding / MPEG-4 AC prediction */
	AC_PRED             (avcodec.CODEC_FLAG_AC_PRED),

	/** loop filter */
	LOOP_FILTER         (avcodec.CODEC_FLAG_LOOP_FILTER),

	/** interlaced motion estimation */
	INTERLACED_ME       (avcodec.CODEC_FLAG_INTERLACED_ME),

	/** */
	CLOSED_GOP          (avcodec.CODEC_FLAG_CLOSED_GOP),

	/** Allow non spec compliant speedup tricks. */
	FAST                (avcodec.CODEC_FLAG2_FAST),

	/** Skip bitstream encoding. */
	NO_OUTPUT           (avcodec.CODEC_FLAG2_NO_OUTPUT),

	/** Place global headers at every keyframe instead of in extradata. */
	LOCAL_HEADER        (avcodec.CODEC_FLAG2_LOCAL_HEADER),

	/** Timecode is in drop frame format. DEPRECATED!!!! */
	DROP_FRAME_TIMECODE (avcodec.CODEC_FLAG2_DROP_FRAME_TIMECODE),

	/** Discard cropping information from SPS. */
	IGNORE_CROP         (avcodec.CODEC_FLAG2_IGNORE_CROP),

	/** Input bitstream might be truncated at a packet boundaries instead of only at frame boundaries. */
	CHUNKS              (avcodec.CODEC_FLAG2_CHUNKS),

	/** Show all frames before the first keyframe */
	SHOW_ALL            (avcodec.CODEC_FLAG2_SHOW_ALL);


	/** FFmpeg codec flag id. */
	private final int id;


	/**
	 * Create a new {@code CodecFlag}.
	 *
	 * @param id FFmpeg codec flag id.
	 */
	private CodecFlag(int id) {
		this.id = id;
	}

	/**
	 * Get the codec flag id defined in FFmpeg.
	 *
	 * @return FFmpeg codec flag id.
	 */
	public final int value() {
		return id;
	}

	/**
	 * Get a {@code CodecFlag} that matches to the specified FFmpeg id.
	 *
	 * @param id FFmpeg codec flag id.
	 *
	 * @return matching codec flag, or {@code null} if id is not defined.
	 */
	public static CodecFlag byId(int id) {
		for (CodecFlag value : values()) {
			if (value.id == id)
				return value;
		}

		return null;
	}
	
}
