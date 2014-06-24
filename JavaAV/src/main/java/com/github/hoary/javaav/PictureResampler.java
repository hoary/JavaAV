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

import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.avcodec.AVPicture;
import org.bytedeco.javacpp.swscale.SwsContext;

import static org.bytedeco.javacpp.swscale.SWS_BILINEAR;
import static org.bytedeco.javacpp.swscale.sws_freeContext;
import static org.bytedeco.javacpp.swscale.sws_getCachedContext;
import static org.bytedeco.javacpp.swscale.sws_scale;

public class PictureResampler {

	/** The re-sample context */
	private SwsContext convertContext;

	/** The input picture format */
	private PictureFormat srcFormat;

	/** The output picture format */
	private PictureFormat dstFormat;


	public void open(PictureFormat srcFormat, PictureFormat dstFormat) throws JavaAVException {
		if (srcFormat == null || dstFormat == null)
			throw new JavaAVException("Invalid video format provided: from " + srcFormat + " to " + dstFormat);

		if (srcFormat.equals(dstFormat))
			return;

		convertContext = sws_getCachedContext(convertContext,
				srcFormat.getWidth(), srcFormat.getHeight(), srcFormat.getFormat().value(),
				dstFormat.getWidth(), dstFormat.getHeight(), dstFormat.getFormat().value(),
				SWS_BILINEAR, null, null, (double[]) null);

		if (convertContext == null)
			throw new JavaAVException("Could not initialize the image conversion context.");

		this.srcFormat = srcFormat;
		this.dstFormat = dstFormat;
	}

	void resample(AVPicture srcPicture, AVPicture dstPicture) throws JavaAVException {
		sws_scale(convertContext, new PointerPointer(srcPicture), srcPicture.linesize(), 0,
				srcFormat.getHeight(), new PointerPointer(dstPicture), dstPicture.linesize());
	}

	public void close() {
		if (convertContext != null) {
			sws_freeContext(convertContext);
			convertContext = null;
		}
	}

}
