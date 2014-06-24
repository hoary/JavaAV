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

public enum PixelFormat {

	NONE(avutil.AV_PIX_FMT_NONE),
	YUV420P(avutil.AV_PIX_FMT_YUV420P), ///< planar YUV 4:2:0, 12bpp, (1 Cr & Cb sample per 2x2 Y samples)
	YUYV422(avutil.AV_PIX_FMT_YUYV422), ///< packed YUV 4:2:2, 16bpp, Y0 Cb Y1 Cr
	RGB24(avutil.AV_PIX_FMT_RGB24), ///< packed RGB 8:8:8, 24bpp, RGBRGB...
	BGR24(avutil.AV_PIX_FMT_BGR24), ///< packed RGB 8:8:8, 24bpp, BGRBGR...
	YUV422P(avutil.AV_PIX_FMT_YUV422P), ///< planar YUV 4:2:2, 16bpp, (1 Cr & Cb sample per 2x1 Y samples)
	YUV444P(avutil.AV_PIX_FMT_YUV444P), ///< planar YUV 4:4:4, 24bpp, (1 Cr & Cb sample per 1x1 Y samples)
	YUV410P(avutil.AV_PIX_FMT_YUV410P), ///< planar YUV 4:1:0,  9bpp, (1 Cr & Cb sample per 4x4 Y samples)
	YUV411P(avutil.AV_PIX_FMT_YUV411P), ///< planar YUV 4:1:1, 12bpp, (1 Cr & Cb sample per 4x1 Y samples)
	GRAY8(avutil.AV_PIX_FMT_GRAY8), ///<        Y        ,  8bpp
	MONOWHITE(avutil.AV_PIX_FMT_MONOWHITE), ///<        Y        ,  1bpp, 0 is white, 1 is black, in each byte pixels are ordered from the msb to the lsb
	MONOBLACK(avutil.AV_PIX_FMT_MONOBLACK), ///<        Y        ,  1bpp, 0 is black, 1 is white, in each byte pixels are ordered from the msb to the lsb
	PAL8(avutil.AV_PIX_FMT_PAL8), ///< 8 bit with RGB32 palette
	YUVJ420P(avutil.AV_PIX_FMT_YUVJ420P), ///< planar YUV 4:2:0, 12bpp, full scale (JPEG), deprecated in favor of YUV420P and setting color_range
	YUVJ422P(avutil.AV_PIX_FMT_YUVJ422P), ///< planar YUV 4:2:2, 16bpp, full scale (JPEG), deprecated in favor of YUV422P and setting color_range
	YUVJ444P(avutil.AV_PIX_FMT_YUVJ444P), ///< planar YUV 4:4:4, 24bpp, full scale (JPEG), deprecated in favor of YUV444P and setting color_range
	XVMC_MPEG2_MC(avutil.AV_PIX_FMT_XVMC_MPEG2_MC),///< XVideo Motion Acceleration via common packet passing
	XVMC_MPEG2_IDCT(avutil.AV_PIX_FMT_XVMC_MPEG2_IDCT),
	UYVY422(avutil.AV_PIX_FMT_UYVY422), ///< packed YUV 4:2:2, 16bpp, Cb Y0 Cr Y1
	UYYVYY411(avutil.AV_PIX_FMT_UYYVYY411), ///< packed YUV 4:1:1, 12bpp, Cb Y0 Y1 Cr Y2 Y3
	BGR8(avutil.AV_PIX_FMT_BGR8), ///< packed RGB 3:3:2,  8bpp, (msb)2B 3G 3R(lsb)
	BGR4(avutil.AV_PIX_FMT_BGR4), ///< packed RGB 1:2:1 bitstream,  4bpp, (msb)1B 2G 1R(lsb), a byte contains two pixels, the first pixel in the byte is the one composed by the 4 msb bits
	BGR4_BYTE(avutil.AV_PIX_FMT_BGR4_BYTE), ///< packed RGB 1:2:1,  8bpp, (msb)1B 2G 1R(lsb)
	RGB8(avutil.AV_PIX_FMT_RGB8), ///< packed RGB 3:3:2,  8bpp, (msb)2R 3G 3B(lsb)
	RGB4(avutil.AV_PIX_FMT_RGB4), ///< packed RGB 1:2:1 bitstream,  4bpp, (msb)1R 2G 1B(lsb), a byte contains two pixels, the first pixel in the byte is the one composed by the 4 msb bits
	RGB4_BYTE(avutil.AV_PIX_FMT_RGB4_BYTE), ///< packed RGB 1:2:1,  8bpp, (msb)1R 2G 1B(lsb)
	NV12(avutil.AV_PIX_FMT_NV12), ///< planar YUV 4:2:0, 12bpp, 1 plane for Y and 1 plane for the UV components, which are interleaved (first byte U and the following byte V)
	NV21(avutil.AV_PIX_FMT_NV21), ///< as above, but U and V bytes are swapped

	ARGB(avutil.AV_PIX_FMT_ARGB), ///< packed ARGB 8:8:8:8, 32bpp, ARGBARGB...
	RGBA(avutil.AV_PIX_FMT_RGBA), ///< packed RGBA 8:8:8:8, 32bpp, RGBARGBA...
	ABGR(avutil.AV_PIX_FMT_ABGR), ///< packed ABGR 8:8:8:8, 32bpp, ABGRABGR...
	BGRA(avutil.AV_PIX_FMT_BGRA), ///< packed BGRA 8:8:8:8, 32bpp, BGRABGRA...

	GRAY16BE(avutil.AV_PIX_FMT_GRAY16BE), ///<        Y        , 16bpp, big-endian
	GRAY16LE(avutil.AV_PIX_FMT_GRAY16LE), ///<        Y        , 16bpp, little-endian
	YUV440P(avutil.AV_PIX_FMT_YUV440P), ///< planar YUV 4:4:0 (1 Cr & Cb sample per 1x2 Y samples)
	YUVJ440P(avutil.AV_PIX_FMT_YUVJ440P), ///< planar YUV 4:4:0 full scale (JPEG), deprecated in favor of YUV440P and setting color_range
	YUVA420P(avutil.AV_PIX_FMT_YUVA420P), ///< planar YUV 4:2:0, 20bpp, (1 Cr & Cb sample per 2x2 Y & A samples)
	VDPAU_H264(avutil.AV_PIX_FMT_VDPAU_H264),///< H.264 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
	VDPAU_MPEG1(avutil.AV_PIX_FMT_VDPAU_MPEG1),///< MPEG-1 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
	VDPAU_MPEG2(avutil.AV_PIX_FMT_VDPAU_MPEG2),///< MPEG-2 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
	VDPAU_WMV3(avutil.AV_PIX_FMT_VDPAU_WMV3),///< WMV3 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
	VDPAU_VC1(avutil.AV_PIX_FMT_VDPAU_VC1), ///< VC-1 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
	RGB48BE(avutil.AV_PIX_FMT_RGB48BE), ///< packed RGB 16:16:16, 48bpp, 16R, 16G, 16B, the 2-byte value for each R/G/B component is stored as big-endian
	RGB48LE(avutil.AV_PIX_FMT_RGB48LE), ///< packed RGB 16:16:16, 48bpp, 16R, 16G, 16B, the 2-byte value for each R/G/B component is stored as little-endian

	RGB565BE(avutil.AV_PIX_FMT_RGB48LE), ///< packed RGB 5:6:5, 16bpp, (msb)   5R 6G 5B(lsb), big-endian
	RGB565LE(avutil.AV_PIX_FMT_RGB565LE), ///< packed RGB 5:6:5, 16bpp, (msb)   5R 6G 5B(lsb), little-endian
	RGB555BE(avutil.AV_PIX_FMT_RGB555BE), ///< packed RGB 5:5:5, 16bpp, (msb)1A 5R 5G 5B(lsb), big-endian, most significant bit to 0
	RGB555LE(avutil.AV_PIX_FMT_RGB555LE), ///< packed RGB 5:5:5, 16bpp, (msb)1A 5R 5G 5B(lsb), little-endian, most significant bit to 0

	BGR565BE(avutil.AV_PIX_FMT_BGR565BE), ///< packed BGR 5:6:5, 16bpp, (msb)   5B 6G 5R(lsb), big-endian
	BGR565LE(avutil.AV_PIX_FMT_BGR565LE), ///< packed BGR 5:6:5, 16bpp, (msb)   5B 6G 5R(lsb), little-endian
	BGR555BE(avutil.AV_PIX_FMT_BGR555BE), ///< packed BGR 5:5:5, 16bpp, (msb)1A 5B 5G 5R(lsb), big-endian, most significant bit to 1
	BGR555LE(avutil.AV_PIX_FMT_BGR555LE), ///< packed BGR 5:5:5, 16bpp, (msb)1A 5B 5G 5R(lsb), little-endian, most significant bit to 1

	VAAPI_MOCO(avutil.AV_PIX_FMT_VAAPI_MOCO), ///< HW acceleration through VA API at motion compensation entry-point, Picture.data[3] contains a vaapi_render_state struct which contains macroblocks as well as various fields extracted from headers
	VAAPI_IDCT(avutil.AV_PIX_FMT_VAAPI_IDCT), ///< HW acceleration through VA API at IDCT entry-point, Picture.data[3] contains a vaapi_render_state struct which contains fields extracted from headers
	VAAPI_VLD(avutil.AV_PIX_FMT_VAAPI_VLD), ///< HW decoding through VA API, Picture.data[3] contains a vaapi_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers

	YUV420P16LE(avutil.AV_PIX_FMT_YUV420P16LE), ///< planar YUV 4:2:0, 24bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
	YUV420P16BE(avutil.AV_PIX_FMT_YUV420P16BE), ///< planar YUV 4:2:0, 24bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
	YUV422P16LE(avutil.AV_PIX_FMT_YUV422P16LE), ///< planar YUV 4:2:2, 32bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
	YUV422P16BE(avutil.AV_PIX_FMT_YUV422P16BE), ///< planar YUV 4:2:2, 32bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
	YUV444P16LE(avutil.AV_PIX_FMT_YUV444P16LE), ///< planar YUV 4:4:4, 48bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
	YUV444P16BE(avutil.AV_PIX_FMT_YUV444P16BE), ///< planar YUV 4:4:4, 48bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
	VDPAU_MPEG4(avutil.AV_PIX_FMT_VDPAU_MPEG4), ///< MPEG4 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
	DXVA2_VLD(avutil.AV_PIX_FMT_DXVA2_VLD), ///< HW decoding through DXVA2, Picture.data[3] contains a LPDIRECT3DSURFACE9 pointer

	RGB444LE(avutil.AV_PIX_FMT_RGB444LE), ///< packed RGB 4:4:4, 16bpp, (msb)4A 4R 4G 4B(lsb), little-endian, most significant bits to 0
	RGB444BE(avutil.AV_PIX_FMT_RGB444BE), ///< packed RGB 4:4:4, 16bpp, (msb)4A 4R 4G 4B(lsb), big-endian, most significant bits to 0
	BGR444LE(avutil.AV_PIX_FMT_BGR444LE), ///< packed BGR 4:4:4, 16bpp, (msb)4A 4B 4G 4R(lsb), little-endian, most significant bits to 1
	BGR444BE(avutil.AV_PIX_FMT_BGR444BE), ///< packed BGR 4:4:4, 16bpp, (msb)4A 4B 4G 4R(lsb), big-endian, most significant bits to 1
	GRAY8A(avutil.AV_PIX_FMT_GRAY8A), ///< 8bit gray, 8bit alpha
	BGR48BE(avutil.AV_PIX_FMT_BGR48BE), ///< packed RGB 16:16:16, 48bpp, 16B, 16G, 16R, the 2-byte value for each R/G/B component is stored as big-endian
	BGR48LE(avutil.AV_PIX_FMT_BGR48LE), ///< packed RGB 16:16:16, 48bpp, 16B, 16G, 16R, the 2-byte value for each R/G/B component is stored as little-endian

	//the following 10 formats have the disadvantage of needing 1 format for each bit depth, thus
	//If you want to support multiple bit depths, then using YUV420P16* with the bpp stored separately
	//is better
	YUV420P9BE(avutil.AV_PIX_FMT_YUV420P9BE), ///< planar YUV 4:2:0, 13.5bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
	YUV420P9LE(avutil.AV_PIX_FMT_YUV420P9LE), ///< planar YUV 4:2:0, 13.5bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
	YUV420P10BE(avutil.AV_PIX_FMT_YUV420P10BE), ///< planar YUV 4:2:0, 15bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
	YUV420P10LE(avutil.AV_PIX_FMT_YUV420P10LE), ///< planar YUV 4:2:0, 15bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
	YUV422P10BE(avutil.AV_PIX_FMT_YUV422P10BE), ///< planar YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
	YUV422P10LE(avutil.AV_PIX_FMT_YUV422P10LE), ///< planar YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
	YUV444P9BE(avutil.AV_PIX_FMT_YUV444P9BE), ///< planar YUV 4:4:4, 27bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
	YUV444P9LE(avutil.AV_PIX_FMT_YUV444P9LE), ///< planar YUV 4:4:4, 27bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
	YUV444P10BE(avutil.AV_PIX_FMT_YUV444P10BE), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
	YUV444P10LE(avutil.AV_PIX_FMT_YUV444P10LE), ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
	YUV422P9BE(avutil.AV_PIX_FMT_YUV422P9BE), ///< planar YUV 4:2:2, 18bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
	YUV422P9LE(avutil.AV_PIX_FMT_YUV422P9LE), ///< planar YUV 4:2:2, 18bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
	VDA_VLD(avutil.AV_PIX_FMT_VDA_VLD), ///< hardware decoding through VDA

//#ifdef ABI_GIT_MASTER
//    RGBA64BE,  ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
//    RGBA64LE,  ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian
//    BGRA64BE,  ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
//    BGRA64LE,  ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian
//#endif

	GBRP(avutil.AV_PIX_FMT_GBRP), ///< planar GBR 4:4:4 24bpp
	GBRP9BE(avutil.AV_PIX_FMT_GBRP9BE), ///< planar GBR 4:4:4 27bpp, big-endian
	GBRP9LE(avutil.AV_PIX_FMT_GBRP9LE), ///< planar GBR 4:4:4 27bpp, little-endian
	GBRP10BE(avutil.AV_PIX_FMT_GBRP10BE), ///< planar GBR 4:4:4 30bpp, big-endian
	GBRP10LE(avutil.AV_PIX_FMT_GBRP10LE), ///< planar GBR 4:4:4 30bpp, little-endian
	GBRP16BE(avutil.AV_PIX_FMT_GBRP16BE), ///< planar GBR 4:4:4 48bpp, big-endian
	GBRP16LE(avutil.AV_PIX_FMT_GBRP16LE), ///< planar GBR 4:4:4 48bpp, little-endian

	/**
	 * duplicated pixel formats for compatibility with libav. FFmpeg supports these formats since May 8 2012 and Jan 28
	 * 2012 (commits f9ca1ac7 and 143a5c55) Libav added them Oct 12 2012 with incompatible values (commit 6d5600e85)
	 */
	YUVA422P_LIBAV(avutil.AV_PIX_FMT_YUVA422P_LIBAV), ///< planar YUV 4:2:2 24bpp, (1 Cr & Cb sample per 2x1 Y & A samples)
	YUVA444P_LIBAV(avutil.AV_PIX_FMT_YUVA444P_LIBAV), ///< planar YUV 4:4:4 32bpp, (1 Cr & Cb sample per 1x1 Y & A samples)

	YUVA420P9BE(avutil.AV_PIX_FMT_YUVA420P9BE),  ///< planar YUV 4:2:0 22.5bpp, (1 Cr & Cb sample per 2x2 Y & A samples), big-endian
	YUVA420P9LE(avutil.AV_PIX_FMT_YUVA420P9LE),  ///< planar YUV 4:2:0 22.5bpp, (1 Cr & Cb sample per 2x2 Y & A samples), little-endian
	YUVA422P9BE(avutil.AV_PIX_FMT_YUVA422P9BE),  ///< planar YUV 4:2:2 27bpp, (1 Cr & Cb sample per 2x1 Y & A samples), big-endian
	YUVA422P9LE(avutil.AV_PIX_FMT_YUVA422P9LE),  ///< planar YUV 4:2:2 27bpp, (1 Cr & Cb sample per 2x1 Y & A samples), little-endian
	YUVA444P9BE(avutil.AV_PIX_FMT_YUVA444P9BE),  ///< planar YUV 4:4:4 36bpp, (1 Cr & Cb sample per 1x1 Y & A samples), big-endian
	YUVA444P9LE(avutil.AV_PIX_FMT_YUVA444P9LE),  ///< planar YUV 4:4:4 36bpp, (1 Cr & Cb sample per 1x1 Y & A samples), little-endian
	YUVA420P10BE(avutil.AV_PIX_FMT_YUVA420P10BE),  ///< planar YUV 4:2:0 25bpp, (1 Cr & Cb sample per 2x2 Y & A samples, big-endian)
	YUVA420P10LE(avutil.AV_PIX_FMT_YUVA420P10LE),  ///< planar YUV 4:2:0 25bpp, (1 Cr & Cb sample per 2x2 Y & A samples, little-endian)
	YUVA422P10BE(avutil.AV_PIX_FMT_YUVA422P10BE),  ///< planar YUV 4:2:2 30bpp, (1 Cr & Cb sample per 2x1 Y & A samples, big-endian)
	YUVA422P10LE(avutil.AV_PIX_FMT_YUVA422P10LE), ///< planar YUV 4:2:2 30bpp, (1 Cr & Cb sample per 2x1 Y & A samples, little-endian)
	YUVA444P10BE(avutil.AV_PIX_FMT_YUVA444P10BE), ///< planar YUV 4:4:4 40bpp, (1 Cr & Cb sample per 1x1 Y & A samples, big-endian)
	YUVA444P10LE(avutil.AV_PIX_FMT_YUVA444P10LE), ///< planar YUV 4:4:4 40bpp, (1 Cr & Cb sample per 1x1 Y & A samples, little-endian)
	YUVA420P16BE(avutil.AV_PIX_FMT_YUVA420P16BE), ///< planar YUV 4:2:0 40bpp, (1 Cr & Cb sample per 2x2 Y & A samples, big-endian)
	YUVA420P16LE(avutil.AV_PIX_FMT_YUVA420P16LE), ///< planar YUV 4:2:0 40bpp, (1 Cr & Cb sample per 2x2 Y & A samples, little-endian)
	YUVA422P16BE(avutil.AV_PIX_FMT_YUVA422P16BE), ///< planar YUV 4:2:2 48bpp, (1 Cr & Cb sample per 2x1 Y & A samples, big-endian)
	YUVA422P16LE(avutil.AV_PIX_FMT_YUVA422P16LE), ///< planar YUV 4:2:2 48bpp, (1 Cr & Cb sample per 2x1 Y & A samples, little-endian)
	YUVA444P16BE(avutil.AV_PIX_FMT_YUVA444P16BE), ///< planar YUV 4:4:4 64bpp, (1 Cr & Cb sample per 1x1 Y & A samples, big-endian)
	YUVA444P16LE(avutil.AV_PIX_FMT_YUVA444P16LE), ///< planar YUV 4:4:4 64bpp, (1 Cr & Cb sample per 1x1 Y & A samples, little-endian)

	VDPAU(avutil.AV_PIX_FMT_VDPAU), ///< HW acceleration through VDPAU, Picture.data[3] contains a VdpVideoSurface

	RGBA64BE(avutil.AV_PIX_FMT_RGBA64BE),   ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
	RGBA64LE(avutil.AV_PIX_FMT_RGBA64LE), ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian
	BGRA64BE(avutil.AV_PIX_FMT_BGRA64BE), ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
	BGRA64LE(avutil.AV_PIX_FMT_BGRA64LE), ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian

	_0RGB(avutil.AV_PIX_FMT_0RGB),     ///< packed RGB 8:8:8, 32bpp, 0RGB0RGB...
	RGB0(avutil.AV_PIX_FMT_RGB0),     ///< packed RGB 8:8:8, 32bpp, RGB0RGB0...
	_0BGR(avutil.AV_PIX_FMT_0BGR),     ///< packed BGR 8:8:8, 32bpp, 0BGR0BGR...
	BGR0(avutil.AV_PIX_FMT_BGR0),     ///< packed BGR 8:8:8, 32bpp, BGR0BGR0...
	YUVA444P(avutil.AV_PIX_FMT_YUVA444P), ///< planar YUV 4:4:4 32bpp, (1 Cr & Cb sample per 1x1 Y & A samples)
	YUVA422P(avutil.AV_PIX_FMT_YUVA422P), ///< planar YUV 4:2:2 24bpp, (1 Cr & Cb sample per 2x1 Y & A samples)

	YUV420P12BE(avutil.AV_PIX_FMT_YUV420P12BE),  ///< planar YUV 4:2:0,18bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
	YUV420P12LE(avutil.AV_PIX_FMT_YUV420P12LE), ///< planar YUV 4:2:0,18bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
	YUV420P14BE(avutil.AV_PIX_FMT_YUV420P14BE), ///< planar YUV 4:2:0,21bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
	YUV420P14LE(avutil.AV_PIX_FMT_YUV420P14LE), ///< planar YUV 4:2:0,21bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
	YUV422P12BE(avutil.AV_PIX_FMT_YUV422P12BE), ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
	YUV422P12LE(avutil.AV_PIX_FMT_YUV422P12LE), ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
	YUV422P14BE(avutil.AV_PIX_FMT_YUV422P14BE), ///< planar YUV 4:2:2,28bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
	YUV422P14LE(avutil.AV_PIX_FMT_YUV422P14LE), ///< planar YUV 4:2:2,28bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
	YUV444P12BE(avutil.AV_PIX_FMT_YUV444P12BE), ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
	YUV444P12LE(avutil.AV_PIX_FMT_YUV444P12LE), ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
	YUV444P14BE(avutil.AV_PIX_FMT_YUV444P14BE), ///< planar YUV 4:4:4,42bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
	YUV444P14LE(avutil.AV_PIX_FMT_YUV444P14LE), ///< planar YUV 4:4:4,42bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
	GBRP12BE(avutil.AV_PIX_FMT_GBRP12BE),    ///< planar GBR 4:4:4 36bpp, big-endian
	GBRP12LE(avutil.AV_PIX_FMT_GBRP12LE),    ///< planar GBR 4:4:4 36bpp, little-endian
	GBRP14BE(avutil.AV_PIX_FMT_GBRP14BE),    ///< planar GBR 4:4:4 42bpp, big-endian
	GBRP14LE(avutil.AV_PIX_FMT_GBRP14LE),    ///< planar GBR 4:4:4 42bpp, little-endian

	Y400A(avutil.AV_PIX_FMT_Y400A),
	GBR24P(avutil.AV_PIX_FMT_GBR24P),

	RGB32(avutil.AV_PIX_FMT_RGB32),
	RGB32_1(avutil.AV_PIX_FMT_RGB32_1),
	BGR32(avutil.AV_PIX_FMT_BGR32),
	BGR32_1(avutil.AV_PIX_FMT_BGR32_1),
	_0RGB32(avutil.AV_PIX_FMT_0RGB32),
	_0BGR32(avutil.AV_PIX_FMT_0BGR32),

	GRAY16(avutil.AV_PIX_FMT_GRAY16),
	RGB48(avutil.AV_PIX_FMT_RGB48),
	RGB565(avutil.AV_PIX_FMT_RGB565),
	RGB555(avutil.AV_PIX_FMT_RGB555),
	RGB444(avutil.AV_PIX_FMT_RGB444),
	BGR48(avutil.AV_PIX_FMT_BGR48),
	BGR565(avutil.AV_PIX_FMT_BGR565),
	BGR555(avutil.AV_PIX_FMT_BGR555),
	BGR444(avutil.AV_PIX_FMT_BGR444),

	YUV420P9(avutil.AV_PIX_FMT_YUV420P9),
	YUV422P9(avutil.AV_PIX_FMT_YUV422P9),
	YUV444P9(avutil.AV_PIX_FMT_YUV444P9),
	YUV420P10(avutil.AV_PIX_FMT_YUV420P10),
	YUV422P10(avutil.AV_PIX_FMT_YUV422P10),
	YUV444P10(avutil.AV_PIX_FMT_YUV444P10),
	YUV420P12(avutil.AV_PIX_FMT_YUV420P12),
	YUV422P12(avutil.AV_PIX_FMT_YUV422P12),
	YUV444P12(avutil.AV_PIX_FMT_YUV444P12),
	YUV420P14(avutil.AV_PIX_FMT_YUV420P14),
	YUV422P14(avutil.AV_PIX_FMT_YUV422P14),
	YUV444P14(avutil.AV_PIX_FMT_YUV444P14),
	YUV420P16(avutil.AV_PIX_FMT_YUV420P16),
	YUV422P16(avutil.AV_PIX_FMT_YUV422P16),
	YUV444P16(avutil.AV_PIX_FMT_YUV444P16),

	RGBA64(avutil.AV_PIX_FMT_RGBA64),
	BGRA64(avutil.AV_PIX_FMT_BGRA64),
	GBRP9(avutil.AV_PIX_FMT_GBRP9),
	GBRP10(avutil.AV_PIX_FMT_GBRP10),
	GBRP12(avutil.AV_PIX_FMT_GBRP12),
	GBRP14(avutil.AV_PIX_FMT_GBRP14),
	GBRP16(avutil.AV_PIX_FMT_GBRP16),

	YUVA420P9(avutil.AV_PIX_FMT_YUVA420P9),
	YUVA422P9(avutil.AV_PIX_FMT_YUVA422P9),
	YUVA444P9(avutil.AV_PIX_FMT_YUVA444P9),
	YUVA420P10(avutil.AV_PIX_FMT_YUVA420P10),
	YUVA422P10(avutil.AV_PIX_FMT_YUVA422P10),
	YUVA444P10(avutil.AV_PIX_FMT_YUVA444P10),
	YUVA420P16(avutil.AV_PIX_FMT_YUVA420P16),
	YUVA422P16(avutil.AV_PIX_FMT_YUVA422P16),
	YUVA444P16(avutil.AV_PIX_FMT_YUVA444P16);


	private final int id;


	private PixelFormat(int id) {
		this.id = id;
	}

	public final int value() {
		return id;
	}

	public static PixelFormat byId(int id) {
		for (PixelFormat value : values()) {
			if (value.id == id)
				return value;
		}

		return null;
	}
	
}
