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

import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class Image {

	public static PixelFormat getPixelFormat(BufferedImage image) {
		switch (image.getType()) {
			case BufferedImage.TYPE_3BYTE_BGR:
				return PixelFormat.BGR24;

			case BufferedImage.TYPE_4BYTE_ABGR:
			case BufferedImage.TYPE_4BYTE_ABGR_PRE:
				return PixelFormat.ABGR;

			case BufferedImage.TYPE_BYTE_BINARY:
				return PixelFormat.RGB444;

			case BufferedImage.TYPE_BYTE_INDEXED:
				return PixelFormat.RGB555;

			case BufferedImage.TYPE_BYTE_GRAY:
				return PixelFormat.GRAY8;

			case BufferedImage.TYPE_INT_ARGB:
			case BufferedImage.TYPE_INT_ARGB_PRE:
				return PixelFormat.ARGB;

			case BufferedImage.TYPE_INT_RGB:
				return PixelFormat.RGB24;

			case BufferedImage.TYPE_USHORT_GRAY:
				return PixelFormat.GRAY16;

			case BufferedImage.TYPE_USHORT_555_RGB:
				return PixelFormat.RGB555;

			case BufferedImage.TYPE_USHORT_565_RGB:
				return PixelFormat.RGB565;

			case BufferedImage.TYPE_CUSTOM:
				return null;

			default:
				return null;
		}
	}

	public static ByteBuffer createImageBuffer(BufferedImage image) {
		SampleModel model = image.getSampleModel();
		Raster raster = image.getRaster();
		DataBuffer inBuffer = raster.getDataBuffer();
		ByteBuffer outBuffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * model.getNumBands());

		int x = -raster.getSampleModelTranslateX();
		int y = -raster.getSampleModelTranslateY();
		int step = model.getWidth() * model.getNumBands();
		int channels = model.getNumBands();

		if (model instanceof ComponentSampleModel) {
			ComponentSampleModel compModel = (ComponentSampleModel) model;
			step = compModel.getScanlineStride();
			channels = ((ComponentSampleModel) model).getPixelStride();
		}
		else if (model instanceof SinglePixelPackedSampleModel) {
			SinglePixelPackedSampleModel singleModel = (SinglePixelPackedSampleModel) model;
			step = singleModel.getScanlineStride();
			channels = 1;
		}
		else if (model instanceof MultiPixelPackedSampleModel) {
			MultiPixelPackedSampleModel multiModel = (MultiPixelPackedSampleModel) model;
			step = multiModel.getScanlineStride();
			channels = ((MultiPixelPackedSampleModel) model).getPixelBitStride() / 8;
		}

		int start = y * step + x * channels;

		if (inBuffer instanceof DataBufferByte) {
			byte[] a = ((DataBufferByte) inBuffer).getData();
			copy(ByteBuffer.wrap(a, start, a.length - start), step, outBuffer, step, false);
		}
		else if (inBuffer instanceof DataBufferShort) {
			short[] a = ((DataBufferShort) inBuffer).getData();
			copy(ShortBuffer.wrap(a, start, a.length - start), step, outBuffer.asShortBuffer(), step / 2, true);
		}
		else if (inBuffer instanceof DataBufferUShort) {
			short[] a = ((DataBufferUShort) inBuffer).getData();
			copy(ShortBuffer.wrap(a, start, a.length - start), step, outBuffer.asShortBuffer(), step / 2, false);
		}
		else if (inBuffer instanceof DataBufferInt) {
			int[] a = ((DataBufferInt) inBuffer).getData();
			copy(IntBuffer.wrap(a, start, a.length - start), step, outBuffer.asIntBuffer(), step / 4);
		}
		else if (inBuffer instanceof DataBufferFloat) {
			float[] a = ((DataBufferFloat) inBuffer).getData();
			copy(FloatBuffer.wrap(a, start, a.length - start), step, outBuffer.asFloatBuffer(), step / 4);
		}
		else if (inBuffer instanceof DataBufferDouble) {
			double[] a = ((DataBufferDouble) inBuffer).getData();
			copy(DoubleBuffer.wrap(a, start, a.length - start), step, outBuffer.asDoubleBuffer(), step / 8);
		}

		outBuffer.position(0);

		return outBuffer;
	}

	public static BufferedImage createImage(VideoFrame frame, int type) {
		return createImage(frame.getData(), frame.getWidth(), frame.getHeight(), type);
	}

	public static BufferedImage createImage(ByteBuffer data, int width, int height, int type) {
		BufferedImage image = new BufferedImage(width, height, type);

		SampleModel model = image.getSampleModel();
		Raster raster = image.getRaster();
		DataBuffer outBuffer = raster.getDataBuffer();

		int x = -raster.getSampleModelTranslateX();
		int y = -raster.getSampleModelTranslateY();
		int step = model.getWidth() * model.getNumBands();
		int channels = model.getNumBands();

		data.position(0).limit(height * width * channels);

		if (model instanceof ComponentSampleModel) {
			ComponentSampleModel compModel = (ComponentSampleModel) model;
			step = compModel.getScanlineStride();
			channels = compModel.getPixelStride();
		}
		else if (model instanceof SinglePixelPackedSampleModel) {
			SinglePixelPackedSampleModel singleModel = (SinglePixelPackedSampleModel) model;
			step = singleModel.getScanlineStride();
			channels = 1;
		}
		else if (model instanceof MultiPixelPackedSampleModel) {
			MultiPixelPackedSampleModel multiModel = (MultiPixelPackedSampleModel) model;
			step = multiModel.getScanlineStride();
			channels = ((MultiPixelPackedSampleModel) model).getPixelBitStride() / 8;
		}

		int start = y * step + x * channels;

		if (outBuffer instanceof DataBufferByte) {
			byte[] a = ((DataBufferByte) outBuffer).getData();
			copy(data, step, ByteBuffer.wrap(a, start, a.length - start), step, false);
		}
		else if (outBuffer instanceof DataBufferShort) {
			short[] a = ((DataBufferShort) outBuffer).getData();
			copy(data.asShortBuffer(), step / 2, ShortBuffer.wrap(a, start, a.length - start), step, true);
		}
		else if (outBuffer instanceof DataBufferUShort) {
			short[] a = ((DataBufferUShort) outBuffer).getData();
			copy(data.asShortBuffer(), step / 2, ShortBuffer.wrap(a, start, a.length - start), step, false);
		}
		else if (outBuffer instanceof DataBufferInt) {
			int[] a = ((DataBufferInt) outBuffer).getData();
			copy(data.asIntBuffer(), step / 4, IntBuffer.wrap(a, start, a.length - start), step);
		}
		else if (outBuffer instanceof DataBufferFloat) {
			float[] a = ((DataBufferFloat) outBuffer).getData();
			copy(data.asFloatBuffer(), step / 4, FloatBuffer.wrap(a, start, a.length - start), step);
		}
		else if (outBuffer instanceof DataBufferDouble) {
			double[] a = ((DataBufferDouble) outBuffer).getData();
			copy(data.asDoubleBuffer(), step / 8, DoubleBuffer.wrap(a, start, a.length - start), step);
		}

		return image;
	}

	public static void copy(ByteBuffer srcBuf, int srcStep, ByteBuffer dstBuf, int dstStep, boolean signed) {
		int w = Math.min(srcStep, dstStep);
		int srcLine = srcBuf.position();
		int dstLine = dstBuf.position();

		while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
			srcBuf.position(srcLine);
			dstBuf.position(dstLine);

			w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

			for (int x = 0; x < w; x++) {
				int in = signed ? srcBuf.get() : srcBuf.get() & 0xFF;
				byte out = (byte) in;
				dstBuf.put(out);
			}

			srcLine += srcStep;
			dstLine += dstStep;
		}
	}

	public static void copy(ShortBuffer srcBuf, int srcStep, ShortBuffer dstBuf, int dstStep, boolean signed) {
		int w = Math.min(srcStep, dstStep);
		int srcLine = srcBuf.position();
		int dstLine = dstBuf.position();

		while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
			srcBuf.position(srcLine);
			dstBuf.position(dstLine);

			w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

			for (int x = 0; x < w; x++) {
				int in = signed ? srcBuf.get() : srcBuf.get() & 0xFFFF;
				short out = (short) in;
				dstBuf.put(out);
			}

			srcLine += srcStep;
			dstLine += dstStep;
		}
	}

	public static void copy(IntBuffer srcBuf, int srcStep, IntBuffer dstBuf, int dstStep) {
		int w = Math.min(srcStep, dstStep);
		int srcLine = srcBuf.position();
		int dstLine = dstBuf.position();

		while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
			srcBuf.position(srcLine);
			dstBuf.position(dstLine);

			w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

			for (int x = 0; x < w; x++) {
				int in = srcBuf.get();
				int out = in;
				dstBuf.put(out);
			}

			srcLine += srcStep;
			dstLine += dstStep;
		}
	}

	public static void copy(FloatBuffer srcBuf, int srcStep, FloatBuffer dstBuf, int dstStep) {
		int w = Math.min(srcStep, dstStep);
		int srcLine = srcBuf.position();
		int dstLine = dstBuf.position();

		while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
			srcBuf.position(srcLine);
			dstBuf.position(dstLine);

			w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

			for (int x = 0; x < w; x++) {
				float in = srcBuf.get();
				float out = in;

				dstBuf.put(out);
			}

			srcLine += srcStep;
			dstLine += dstStep;
		}
	}

	public static void copy(DoubleBuffer srcBuf, int srcStep, DoubleBuffer dstBuf, int dstStep) {
		int w = Math.min(srcStep, dstStep);
		int srcLine = srcBuf.position();
		int dstLine = dstBuf.position();

		while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
			srcBuf.position(srcLine);
			dstBuf.position(dstLine);

			w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());

			for (int x = 0; x < w; x++) {
				double in = srcBuf.get();
				double out = in;

				dstBuf.put(out);
			}

			srcLine += srcStep;
			dstLine += dstStep;
		}
	}
	
}
