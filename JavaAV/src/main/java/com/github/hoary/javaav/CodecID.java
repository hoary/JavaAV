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
 * Enumeration of all possible codec id's.
 * <p/>
 * NOTE: The codec id comments were taken from avcodec.h.
 *
 * @author Alex Andres
 */
public enum CodecID {

	NONE(avcodec.AV_CODEC_ID_NONE),

	/* video codecs */
	MPEG1VIDEO(avcodec.AV_CODEC_ID_MPEG1VIDEO),
	MPEG2VIDEO(avcodec.AV_CODEC_ID_MPEG2VIDEO),
	MPEG2VIDEO_XVMC(avcodec.AV_CODEC_ID_MPEG2VIDEO_XVMC),
	H261(avcodec.AV_CODEC_ID_H261),
	H263(avcodec.AV_CODEC_ID_H263),
	RV10(avcodec.AV_CODEC_ID_RV10),
	RV20(avcodec.AV_CODEC_ID_RV20),
	MJPEG(avcodec.AV_CODEC_ID_MJPEG),
	MJPEGB(avcodec.AV_CODEC_ID_MJPEGB),
	LJPEG(avcodec.AV_CODEC_ID_LJPEG),
	SP5X(avcodec.AV_CODEC_ID_SP5X),
	JPEGLS(avcodec.AV_CODEC_ID_JPEGLS),
	MPEG4(avcodec.AV_CODEC_ID_MPEG4),
	RAWVIDEO(avcodec.AV_CODEC_ID_RAWVIDEO),
	MSMPEG4V1(avcodec.AV_CODEC_ID_MSMPEG4V1),
	MSMPEG4V2(avcodec.AV_CODEC_ID_MSMPEG4V2),
	MSMPEG4V3(avcodec.AV_CODEC_ID_MSMPEG4V3),
	WMV1(avcodec.AV_CODEC_ID_WMV1),
	WMV2(avcodec.AV_CODEC_ID_WMV2),
	H263P(avcodec.AV_CODEC_ID_H263P),
	H263I(avcodec.AV_CODEC_ID_H263I),
	FLV1(avcodec.AV_CODEC_ID_FLV1),
	SVQ1(avcodec.AV_CODEC_ID_SVQ1),
	SVQ3(avcodec.AV_CODEC_ID_SVQ3),
	DVVIDEO(avcodec.AV_CODEC_ID_DVVIDEO),
	HUFFYUV(avcodec.AV_CODEC_ID_HUFFYUV),
	CYUV(avcodec.AV_CODEC_ID_CYUV),
	H264(avcodec.AV_CODEC_ID_H264),
	INDEO3(avcodec.AV_CODEC_ID_INDEO3),
	VP3(avcodec.AV_CODEC_ID_VP3),
	THEORA(avcodec.AV_CODEC_ID_THEORA),
	ASV1(avcodec.AV_CODEC_ID_ASV1),
	ASV2(avcodec.AV_CODEC_ID_ASV2),
	FFV1(avcodec.AV_CODEC_ID_FFV1),
	_4XM(avcodec.AV_CODEC_ID_4XM),
	VCR1(avcodec.AV_CODEC_ID_VCR1),
	CLJR(avcodec.AV_CODEC_ID_CLJR),
	MDEC(avcodec.AV_CODEC_ID_MDEC),
	ROQ(avcodec.AV_CODEC_ID_ROQ),
	INTERPLAY_VIDEO(avcodec.AV_CODEC_ID_INTERPLAY_VIDEO),
	XAN_WC3(avcodec.AV_CODEC_ID_XAN_WC3),
	XAN_WC4(avcodec.AV_CODEC_ID_XAN_WC4),
	RPZA(avcodec.AV_CODEC_ID_RPZA),
	CINEPAK(avcodec.AV_CODEC_ID_CINEPAK),
	WS_VQA(avcodec.AV_CODEC_ID_WS_VQA),
	MSRLE(avcodec.AV_CODEC_ID_MSRLE),
	MSVIDEO1(avcodec.AV_CODEC_ID_MSVIDEO1),
	IDCIN(avcodec.AV_CODEC_ID_IDCIN),
	_8BPS(avcodec.AV_CODEC_ID_8BPS),
	SMC(avcodec.AV_CODEC_ID_SMC),
	FLIC(avcodec.AV_CODEC_ID_FLIC),
	TRUEMOTION1(avcodec.AV_CODEC_ID_TRUEMOTION1),
	VMDVIDEO(avcodec.AV_CODEC_ID_VMDVIDEO),
	MSZH(avcodec.AV_CODEC_ID_MSZH),
	ZLIB(avcodec.AV_CODEC_ID_ZLIB),
	QTRLE(avcodec.AV_CODEC_ID_QTRLE),
	TSCC(avcodec.AV_CODEC_ID_TSCC),
	ULTI(avcodec.AV_CODEC_ID_ULTI),
	QDRAW(avcodec.AV_CODEC_ID_QDRAW),
	VIXL(avcodec.AV_CODEC_ID_VIXL),
	QPEG(avcodec.AV_CODEC_ID_QPEG),
	PNG(avcodec.AV_CODEC_ID_PNG),
	PPM(avcodec.AV_CODEC_ID_PPM),
	PBM(avcodec.AV_CODEC_ID_PBM),
	PGM(avcodec.AV_CODEC_ID_PGM),
	PGMYUV(avcodec.AV_CODEC_ID_PGMYUV),
	PAM(avcodec.AV_CODEC_ID_PAM),
	FFVHUFF(avcodec.AV_CODEC_ID_FFVHUFF),
	RV30(avcodec.AV_CODEC_ID_RV30),
	RV40(avcodec.AV_CODEC_ID_RV40),
	VC1(avcodec.AV_CODEC_ID_VC1),
	WMV3(avcodec.AV_CODEC_ID_WMV3),
	LOCO(avcodec.AV_CODEC_ID_LOCO),
	WNV1(avcodec.AV_CODEC_ID_WNV1),
	AASC(avcodec.AV_CODEC_ID_AASC),
	INDEO2(avcodec.AV_CODEC_ID_INDEO2),
	FRAPS(avcodec.AV_CODEC_ID_FRAPS),
	TRUEMOTION2(avcodec.AV_CODEC_ID_TRUEMOTION2),
	BMP(avcodec.AV_CODEC_ID_BMP),
	CSCD(avcodec.AV_CODEC_ID_CSCD),
	MMVIDEO(avcodec.AV_CODEC_ID_MMVIDEO),
	ZMBV(avcodec.AV_CODEC_ID_ZMBV),
	AVS(avcodec.AV_CODEC_ID_AVS),
	SMACKVIDEO(avcodec.AV_CODEC_ID_SMACKVIDEO),
	NUV(avcodec.AV_CODEC_ID_NUV),
	KMVC(avcodec.AV_CODEC_ID_KMVC),
	FLASHSV(avcodec.AV_CODEC_ID_FLASHSV),
	CAVS(avcodec.AV_CODEC_ID_CAVS),
	JPEG2000(avcodec.AV_CODEC_ID_JPEG2000),
	VMNC(avcodec.AV_CODEC_ID_VMNC),
	VP5(avcodec.AV_CODEC_ID_VP5),
	VP6(avcodec.AV_CODEC_ID_VP6),
	VP6F(avcodec.AV_CODEC_ID_VP6F),
	TARGA(avcodec.AV_CODEC_ID_TARGA),
	DSICINVIDEO(avcodec.AV_CODEC_ID_DSICINVIDEO),
	TIERTEXSEQVIDEO(avcodec.AV_CODEC_ID_TIERTEXSEQVIDEO),
	TIFF(avcodec.AV_CODEC_ID_TIFF),
	GIF(avcodec.AV_CODEC_ID_GIF),
	DXA(avcodec.AV_CODEC_ID_DXA),
	DNXHD(avcodec.AV_CODEC_ID_DNXHD),
	THP(avcodec.AV_CODEC_ID_THP),
	SGI(avcodec.AV_CODEC_ID_SGI),
	C93(avcodec.AV_CODEC_ID_C93),
	BETHSOFTVID(avcodec.AV_CODEC_ID_BETHSOFTVID),
	PTX(avcodec.AV_CODEC_ID_PTX),
	TXD(avcodec.AV_CODEC_ID_TXD),
	VP6A(avcodec.AV_CODEC_ID_VP6A),
	AMV(avcodec.AV_CODEC_ID_AMV),
	VB(avcodec.AV_CODEC_ID_VB),
	PCX(avcodec.AV_CODEC_ID_PCX),
	SUNRAST(avcodec.AV_CODEC_ID_SUNRAST),
	INDEO4(avcodec.AV_CODEC_ID_INDEO4),
	INDEO5(avcodec.AV_CODEC_ID_INDEO5),
	MIMIC(avcodec.AV_CODEC_ID_MIMIC),
	RL2(avcodec.AV_CODEC_ID_RL2),
	ESCAPE124(avcodec.AV_CODEC_ID_ESCAPE124),
	DIRAC(avcodec.AV_CODEC_ID_DIRAC),
	BFI(avcodec.AV_CODEC_ID_BFI),
	CMV(avcodec.AV_CODEC_ID_CMV),
	MOTIONPIXELS(avcodec.AV_CODEC_ID_MOTIONPIXELS),
	TGV(avcodec.AV_CODEC_ID_TGV),
	TGQ(avcodec.AV_CODEC_ID_TGQ),
	TQI(avcodec.AV_CODEC_ID_TQI),
	AURA(avcodec.AV_CODEC_ID_AURA),
	AURA2(avcodec.AV_CODEC_ID_AURA2),
	V210X(avcodec.AV_CODEC_ID_V210X),
	TMV(avcodec.AV_CODEC_ID_TMV),
	V210(avcodec.AV_CODEC_ID_V210),
	DPX(avcodec.AV_CODEC_ID_DPX),
	MAD(avcodec.AV_CODEC_ID_MAD),
	FRWU(avcodec.AV_CODEC_ID_FRWU),
	FLASHSV2(avcodec.AV_CODEC_ID_FLASHSV2),
	CDGRAPHICS(avcodec.AV_CODEC_ID_CDGRAPHICS),
	R210(avcodec.AV_CODEC_ID_R210),
	ANM(avcodec.AV_CODEC_ID_ANM),
	BINKVIDEO(avcodec.AV_CODEC_ID_BINKVIDEO),
	IFF_ILBM(avcodec.AV_CODEC_ID_IFF_ILBM),
	IFF_BYTERUN1(avcodec.AV_CODEC_ID_IFF_BYTERUN1),
	KGV1(avcodec.AV_CODEC_ID_KGV1),
	YOP(avcodec.AV_CODEC_ID_YOP),
	VP8(avcodec.AV_CODEC_ID_VP8),
	PICTOR(avcodec.AV_CODEC_ID_PICTOR),
	ANSI(avcodec.AV_CODEC_ID_ANSI),
	A64_MULTI(avcodec.AV_CODEC_ID_A64_MULTI),
	A64_MULTI5(avcodec.AV_CODEC_ID_A64_MULTI5),
	R10K(avcodec.AV_CODEC_ID_R10K),
	MXPEG(avcodec.AV_CODEC_ID_MXPEG),
	LAGARITH(avcodec.AV_CODEC_ID_LAGARITH),
	PRORES(avcodec.AV_CODEC_ID_PRORES),
	JV(avcodec.AV_CODEC_ID_JV),
	DFA(avcodec.AV_CODEC_ID_DFA),
	WMV3IMAGE(avcodec.AV_CODEC_ID_WMV3IMAGE),
	VC1IMAGE(avcodec.AV_CODEC_ID_VC1IMAGE),
	UTVIDEO(avcodec.AV_CODEC_ID_UTVIDEO),
	BMV_VIDEO(avcodec.AV_CODEC_ID_BMV_VIDEO),
	VBLE(avcodec.AV_CODEC_ID_VBLE),
	DXTORY(avcodec.AV_CODEC_ID_DXTORY),
	V410(avcodec.AV_CODEC_ID_V410),
	XWD(avcodec.AV_CODEC_ID_XWD),
	CDXL(avcodec.AV_CODEC_ID_CDXL),
	XBM(avcodec.AV_CODEC_ID_XBM),
	ZEROCODEC(avcodec.AV_CODEC_ID_ZEROCODEC),
	MSS1(avcodec.AV_CODEC_ID_MSS1),
	MSA1(avcodec.AV_CODEC_ID_MSA1),
	TSCC2(avcodec.AV_CODEC_ID_TSCC2),
	MTS2(avcodec.AV_CODEC_ID_MTS2),
	CLLC(avcodec.AV_CODEC_ID_CLLC),
	MSS2(avcodec.AV_CODEC_ID_MSS2),
	VP9(avcodec.AV_CODEC_ID_VP9),
	AIC(avcodec.AV_CODEC_ID_AIC),
	ESCAPE130_DEPRECATED(avcodec.AV_CODEC_ID_ESCAPE130_DEPRECATED),
	G2M_DEPRECATED(avcodec.AV_CODEC_ID_G2M_DEPRECATED),

	BRENDER_PIX(avcodec.AV_CODEC_ID_BRENDER_PIX),
	Y41P(avcodec.AV_CODEC_ID_Y41P),
	ESCAPE130(avcodec.AV_CODEC_ID_ESCAPE130),
	EXR(avcodec.AV_CODEC_ID_EXR),
	AVRP(avcodec.AV_CODEC_ID_AVRP),

	_012V(avcodec.AV_CODEC_ID_012V),
	G2M(avcodec.AV_CODEC_ID_G2M),
	AVUI(avcodec.AV_CODEC_ID_AVUI),
	AYUV(avcodec.AV_CODEC_ID_AYUV),
	TARGA_Y216(avcodec.AV_CODEC_ID_TARGA_Y216),
	V308(avcodec.AV_CODEC_ID_V308),
	V408(avcodec.AV_CODEC_ID_V408),
	YUV4(avcodec.AV_CODEC_ID_YUV4),
	SANM(avcodec.AV_CODEC_ID_SANM),
	PAF_VIDEO(avcodec.AV_CODEC_ID_PAF_VIDEO),
	AVRN(avcodec.AV_CODEC_ID_AVRN),
	CPIA(avcodec.AV_CODEC_ID_CPIA),
	XFACE(avcodec.AV_CODEC_ID_XFACE),
	SGIRLE(avcodec.AV_CODEC_ID_SGIRLE),
	MVC1(avcodec.AV_CODEC_ID_MVC1),
	MVC2(avcodec.AV_CODEC_ID_MVC2),
	SNOW(avcodec.AV_CODEC_ID_SNOW),
	WEBP(avcodec.AV_CODEC_ID_WEBP),
	SMVJPEG(avcodec.AV_CODEC_ID_SMVJPEG),
	// TODO
	//H265                    				(avcodec.AV_CODEC_ID_H265),

	/* various PCM "codecs" */
	FIRST_AUDIO(avcodec.AV_CODEC_ID_FIRST_AUDIO),
	PCM_S16LE(avcodec.AV_CODEC_ID_PCM_S16LE),
	PCM_S16BE(avcodec.AV_CODEC_ID_PCM_S16BE),
	PCM_U16LE(avcodec.AV_CODEC_ID_PCM_U16LE),
	PCM_U16BE(avcodec.AV_CODEC_ID_PCM_U16BE),
	PCM_S8(avcodec.AV_CODEC_ID_PCM_S8),
	PCM_U8(avcodec.AV_CODEC_ID_PCM_U8),
	PCM_MULAW(avcodec.AV_CODEC_ID_PCM_MULAW),
	PCM_ALAW(avcodec.AV_CODEC_ID_PCM_ALAW),
	PCM_S32LE(avcodec.AV_CODEC_ID_PCM_S32LE),
	PCM_S32BE(avcodec.AV_CODEC_ID_PCM_S32BE),
	PCM_U32LE(avcodec.AV_CODEC_ID_PCM_U32LE),
	PCM_U32BE(avcodec.AV_CODEC_ID_PCM_U32BE),
	PCM_S24LE(avcodec.AV_CODEC_ID_PCM_S24LE),
	PCM_S24BE(avcodec.AV_CODEC_ID_PCM_S24BE),
	PCM_U24LE(avcodec.AV_CODEC_ID_PCM_U24LE),
	PCM_U24BE(avcodec.AV_CODEC_ID_PCM_U24BE),
	PCM_S24DAUD(avcodec.AV_CODEC_ID_PCM_S24DAUD),
	PCM_ZORK(avcodec.AV_CODEC_ID_PCM_ZORK),
	PCM_S16LE_PLANAR(avcodec.AV_CODEC_ID_PCM_S16LE_PLANAR),
	PCM_DVD(avcodec.AV_CODEC_ID_PCM_DVD),
	PCM_F32BE(avcodec.AV_CODEC_ID_PCM_F32BE),
	PCM_F32LE(avcodec.AV_CODEC_ID_PCM_F32LE),
	PCM_F64BE(avcodec.AV_CODEC_ID_PCM_F64BE),
	PCM_F64LE(avcodec.AV_CODEC_ID_PCM_F64LE),
	PCM_BLURAY(avcodec.AV_CODEC_ID_PCM_BLURAY),
	PCM_LXF(avcodec.AV_CODEC_ID_PCM_LXF),
	S302M(avcodec.AV_CODEC_ID_S302M),
	PCM_S8_PLANAR(avcodec.AV_CODEC_ID_PCM_S8_PLANAR),
	PCM_S24LE_PLANAR(avcodec.AV_CODEC_ID_PCM_S24LE_PLANAR),
	PCM_S32LE_PLANAR(avcodec.AV_CODEC_ID_PCM_S32LE_PLANAR),
	PCM_S16BE_PLANAR(avcodec.AV_CODEC_ID_PCM_S16BE_PLANAR),

	/* various ADPCM codecs */
	ADPCM_IMA_QT(avcodec.AV_CODEC_ID_ADPCM_IMA_QT),
	ADPCM_IMA_WAV(avcodec.AV_CODEC_ID_ADPCM_IMA_WAV),
	ADPCM_IMA_DK3(avcodec.AV_CODEC_ID_ADPCM_IMA_DK3),
	ADPCM_IMA_DK4(avcodec.AV_CODEC_ID_ADPCM_IMA_DK4),
	ADPCM_IMA_WS(avcodec.AV_CODEC_ID_ADPCM_IMA_WS),
	ADPCM_IMA_SMJPEG(avcodec.AV_CODEC_ID_ADPCM_IMA_SMJPEG),
	ADPCM_MS(avcodec.AV_CODEC_ID_ADPCM_MS),
	ADPCM_4XM(avcodec.AV_CODEC_ID_ADPCM_4XM),
	ADPCM_XA(avcodec.AV_CODEC_ID_ADPCM_XA),
	ADPCM_ADX(avcodec.AV_CODEC_ID_ADPCM_ADX),
	ADPCM_EA(avcodec.AV_CODEC_ID_ADPCM_EA),
	ADPCM_G726(avcodec.AV_CODEC_ID_ADPCM_G726),
	ADPCM_CT(avcodec.AV_CODEC_ID_ADPCM_CT),
	ADPCM_SWF(avcodec.AV_CODEC_ID_ADPCM_SWF),
	ADPCM_YAMAHA(avcodec.AV_CODEC_ID_ADPCM_YAMAHA),
	ADPCM_SBPRO_4(avcodec.AV_CODEC_ID_ADPCM_SBPRO_4),
	ADPCM_SBPRO_3(avcodec.AV_CODEC_ID_ADPCM_SBPRO_3),
	ADPCM_SBPRO_2(avcodec.AV_CODEC_ID_ADPCM_SBPRO_2),
	ADPCM_THP(avcodec.AV_CODEC_ID_ADPCM_THP),
	ADPCM_IMA_AMV(avcodec.AV_CODEC_ID_ADPCM_IMA_AMV),
	ADPCM_EA_R1(avcodec.AV_CODEC_ID_ADPCM_EA_R1),
	ADPCM_EA_R3(avcodec.AV_CODEC_ID_ADPCM_EA_R3),
	ADPCM_EA_R2(avcodec.AV_CODEC_ID_ADPCM_EA_R2),
	ADPCM_IMA_EA_SEAD(avcodec.AV_CODEC_ID_ADPCM_IMA_EA_SEAD),
	ADPCM_IMA_EA_EACS(avcodec.AV_CODEC_ID_ADPCM_IMA_EA_EACS),
	ADPCM_EA_XAS(avcodec.AV_CODEC_ID_ADPCM_EA_XAS),
	ADPCM_EA_MAXIS_XA(avcodec.AV_CODEC_ID_ADPCM_EA_MAXIS_XA),
	ADPCM_IMA_ISS(avcodec.AV_CODEC_ID_ADPCM_IMA_ISS),
	ADPCM_G722(avcodec.AV_CODEC_ID_ADPCM_G722),
	ADPCM_IMA_APC(avcodec.AV_CODEC_ID_ADPCM_IMA_APC),
	VIMA(avcodec.AV_CODEC_ID_VIMA),
	ADPCM_AFC(avcodec.AV_CODEC_ID_ADPCM_AFC),
	ADPCM_IMA_OKI(avcodec.AV_CODEC_ID_ADPCM_IMA_OKI),
	ADPCM_DTK(avcodec.AV_CODEC_ID_ADPCM_DTK),
	ADPCM_IMA_RAD(avcodec.AV_CODEC_ID_ADPCM_IMA_RAD),

	/* AMR */
	AMR_NB(avcodec.AV_CODEC_ID_AMR_NB),
	AMR_WB(avcodec.AV_CODEC_ID_AMR_WB),

	/* RealAudio codecs */
	RA_144(avcodec.AV_CODEC_ID_RA_144),
	RA_288(avcodec.AV_CODEC_ID_RA_288),

	/* various DPCM codecs */
	ROQ_DPCM(avcodec.AV_CODEC_ID_ROQ_DPCM),
	INTERPLAY_DPCM(avcodec.AV_CODEC_ID_INTERPLAY_DPCM),
	XAN_DPCM(avcodec.AV_CODEC_ID_XAN_DPCM),
	SOL_DPCM(avcodec.AV_CODEC_ID_SOL_DPCM),

	/* audio codecs */
	MP2(avcodec.AV_CODEC_ID_MP2),

	/** preferred ID for decoding MPEG audio layer 1, 2 or 3 */
	MP3(avcodec.AV_CODEC_ID_MP3),
	AAC(avcodec.AV_CODEC_ID_AAC),
	AC3(avcodec.AV_CODEC_ID_AC3),
	DTS(avcodec.AV_CODEC_ID_DTS),
	VORBIS(avcodec.AV_CODEC_ID_VORBIS),
	DVAUDIO(avcodec.AV_CODEC_ID_DVAUDIO),
	WMAV1(avcodec.AV_CODEC_ID_WMAV1),
	WMAV2(avcodec.AV_CODEC_ID_WMAV2),
	MACE3(avcodec.AV_CODEC_ID_MACE3),
	MACE6(avcodec.AV_CODEC_ID_MACE6),
	VMDAUDIO(avcodec.AV_CODEC_ID_VMDAUDIO),
	FLAC(avcodec.AV_CODEC_ID_FLAC),
	MP3ADU(avcodec.AV_CODEC_ID_MP3ADU),
	MP3ON4(avcodec.AV_CODEC_ID_MP3ON4),
	SHORTEN(avcodec.AV_CODEC_ID_SHORTEN),
	ALAC(avcodec.AV_CODEC_ID_ALAC),
	WESTWOOD_SND1(avcodec.AV_CODEC_ID_WESTWOOD_SND1),
	GSM(avcodec.AV_CODEC_ID_GSM), // /< as in Berlin toast format
	QDM2(avcodec.AV_CODEC_ID_QDM2),
	COOK(avcodec.AV_CODEC_ID_COOK),
	TRUESPEECH(avcodec.AV_CODEC_ID_TRUESPEECH),
	TTA(avcodec.AV_CODEC_ID_TTA),
	SMACKAUDIO(avcodec.AV_CODEC_ID_SMACKAUDIO),
	QCELP(avcodec.AV_CODEC_ID_QCELP),
	WAVPACK(avcodec.AV_CODEC_ID_WAVPACK),
	DSICINAUDIO(avcodec.AV_CODEC_ID_DSICINAUDIO),
	IMC(avcodec.AV_CODEC_ID_IMC),
	MUSEPACK7(avcodec.AV_CODEC_ID_MUSEPACK7),
	MLP(avcodec.AV_CODEC_ID_MLP),
	GSM_MS(avcodec.AV_CODEC_ID_GSM_MS), /* as found in WAV */
	ATRAC3(avcodec.AV_CODEC_ID_ATRAC3),
	VOXWARE(avcodec.AV_CODEC_ID_VOXWARE),
	APE(avcodec.AV_CODEC_ID_APE),
	NELLYMOSER(avcodec.AV_CODEC_ID_NELLYMOSER),
	MUSEPACK8(avcodec.AV_CODEC_ID_MUSEPACK8),
	SPEEX(avcodec.AV_CODEC_ID_SPEEX),
	WMAVOICE(avcodec.AV_CODEC_ID_WMAVOICE),
	WMAPRO(avcodec.AV_CODEC_ID_WMAPRO),
	WMALOSSLESS(avcodec.AV_CODEC_ID_WMALOSSLESS),
	ATRAC3P(avcodec.AV_CODEC_ID_ATRAC3P),
	EAC3(avcodec.AV_CODEC_ID_EAC3),
	SIPR(avcodec.AV_CODEC_ID_SIPR),
	MP1(avcodec.AV_CODEC_ID_MP1),
	TWINVQ(avcodec.AV_CODEC_ID_TWINVQ),
	TRUEHD(avcodec.AV_CODEC_ID_TRUEHD),
	MP4ALS(avcodec.AV_CODEC_ID_MP4ALS),
	ATRAC1(avcodec.AV_CODEC_ID_ATRAC1),
	BINKAUDIO_RDFT(avcodec.AV_CODEC_ID_BINKAUDIO_RDFT),
	BINKAUDIO_DCT(avcodec.AV_CODEC_ID_BINKAUDIO_DCT),
	AAC_LATM(avcodec.AV_CODEC_ID_AAC_LATM),
	QDMC(avcodec.AV_CODEC_ID_QDMC),
	CELT(avcodec.AV_CODEC_ID_CELT),
	G723_1(avcodec.AV_CODEC_ID_G723_1),
	G729(avcodec.AV_CODEC_ID_G729),
	_8SVX_EXP(avcodec.AV_CODEC_ID_8SVX_EXP),
	_8SVX_FIB(avcodec.AV_CODEC_ID_8SVX_FIB),
	BMV_AUDIO(avcodec.AV_CODEC_ID_BMV_AUDIO),
	RALF(avcodec.AV_CODEC_ID_RALF),
	IAC(avcodec.AV_CODEC_ID_IAC),
	ILBC(avcodec.AV_CODEC_ID_ILBC),
	OPUS_DEPRECATED(avcodec.AV_CODEC_ID_OPUS_DEPRECATED),
	COMFORT_NOISE(avcodec.AV_CODEC_ID_COMFORT_NOISE),
	TAK_DEPRECATED(avcodec.AV_CODEC_ID_TAK_DEPRECATED),
	FFWAVESYNTH(avcodec.AV_CODEC_ID_FFWAVESYNTH),
	SONIC(avcodec.AV_CODEC_ID_SONIC),
	SONIC_LS(avcodec.AV_CODEC_ID_SONIC_LS),
	PAF_AUDIO(avcodec.AV_CODEC_ID_PAF_AUDIO),
	OPUS(avcodec.AV_CODEC_ID_OPUS),
	TAK(avcodec.AV_CODEC_ID_TAK),
	EVRC(avcodec.AV_CODEC_ID_EVRC),
	SMV(avcodec.AV_CODEC_ID_SMV),

	/* subtitle codecs */
	FIRST_SUBTITLE(avcodec.AV_CODEC_ID_FIRST_SUBTITLE),
	DVD_SUBTITLE(avcodec.AV_CODEC_ID_DVD_SUBTITLE),
	DVB_SUBTITLE(avcodec.AV_CODEC_ID_DVB_SUBTITLE),
	TEXT(avcodec.AV_CODEC_ID_TEXT), // /< raw UTF-8 text
	XSUB(avcodec.AV_CODEC_ID_XSUB),
	SSA(avcodec.AV_CODEC_ID_SSA),
	MOV_TEXT(avcodec.AV_CODEC_ID_MOV_TEXT),
	HDMV_PGS_SUBTITLE(avcodec.AV_CODEC_ID_HDMV_PGS_SUBTITLE),
	DVB_TELETEXT(avcodec.AV_CODEC_ID_DVB_TELETEXT),
	SRT(avcodec.AV_CODEC_ID_SRT),
	MICRODVD(avcodec.AV_CODEC_ID_MICRODVD),
	EIA_608(avcodec.AV_CODEC_ID_EIA_608),
	JACOSUB(avcodec.AV_CODEC_ID_JACOSUB),
	SAMI(avcodec.AV_CODEC_ID_SAMI),
	REALTEXT(avcodec.AV_CODEC_ID_REALTEXT),
	SUBVIEWER1(avcodec.AV_CODEC_ID_SUBVIEWER1),
	SUBVIEWER(avcodec.AV_CODEC_ID_SUBVIEWER),
	SUBRIP(avcodec.AV_CODEC_ID_SUBRIP),
	WEBVTT(avcodec.AV_CODEC_ID_WEBVTT),
	MPL2(avcodec.AV_CODEC_ID_MPL2),
	VPLAYER(avcodec.AV_CODEC_ID_VPLAYER),
	PJS(avcodec.AV_CODEC_ID_PJS),
	ASS(avcodec.AV_CODEC_ID_ASS), // /< ASS as defined in Matroska

	/* other specific kind of codecs 									(generally used for attachments) */
	FIRST_UNKNOWN(avcodec.AV_CODEC_ID_FIRST_UNKNOWN),
	TTF(avcodec.AV_CODEC_ID_TTF),
	BINTEXT(avcodec.AV_CODEC_ID_BINTEXT),
	XBIN(avcodec.AV_CODEC_ID_XBIN),
	IDF(avcodec.AV_CODEC_ID_IDF),
	OTF(avcodec.AV_CODEC_ID_OTF),
	SMPTE_KLV(avcodec.AV_CODEC_ID_SMPTE_KLV),
	DVD_NAV(avcodec.AV_CODEC_ID_DVD_NAV),

	/** codec_id is not known 				(like avcodec.AV_CODEC_ID_NONE) but lavf should attempt to identify it */
	PROBE(avcodec.AV_CODEC_ID_PROBE),

	/** < _FAKE_ codec to indicate a raw MPEG-2 TS stream 				(only used by libavformat) */
	MPEG2TS(avcodec.AV_CODEC_ID_MPEG2TS),

	/** < _FAKE_ codec to indicate a MPEG-4 Systems stream 				(only used by libavformat) */
	MPEG4SYSTEMS(avcodec.AV_CODEC_ID_MPEG4SYSTEMS),

	/** Dummy codec for streams containing only metadata information. */
	FFMETADATA(avcodec.AV_CODEC_ID_FFMETADATA);


	/** FFmpeg codec id. */
	private int id;


	/**
	 * Create a new {@code CodecID}.
	 *
	 * @param id FFmpeg codec id.
	 */
	private CodecID(int id) {
		JavaAV.loadLibrary();

		this.id = id;
	}

	/**
	 * Get the codec id defined in FFmpeg.
	 *
	 * @return FFmpeg codec id.
	 */
	public final int value() {
		return id;
	}

	/**
	 * Get a {@code CodecID} that matches to the specified FFmpeg id.
	 *
	 * @param id FFmpeg codec id.
	 *
	 * @return matching codec id, or {@code null} if id is not defined.
	 */
	public static CodecID byId(int id) {
		for (CodecID value : values()) {
			if (value.id == id)
				return value;
		}

		return null;
	}

}
