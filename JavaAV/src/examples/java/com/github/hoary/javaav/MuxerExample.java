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

public class MuxerExample {

	public static void main(String[] args) throws Exception {
		Muxer muxer = new Muxer("src/examples/resources/out.mp4");
		muxer.setVideoCodec(Codec.getEncoderById(CodecID.H264));
		muxer.setAudioCodec(Codec.getEncoderById(CodecID.MP3));
		muxer.setImageWidth(600);
		muxer.setImageHeight(350);
		muxer.setGOPSize(25);
		muxer.setPixelFormat(PixelFormat.YUV420P);
		muxer.setVideoBitrate(2000000);
		muxer.setAudioBitrate(128000);
		muxer.setFramerate(25);
		muxer.setSamplerate(24000);
		muxer.setAudioChannels(2);
		muxer.open();


		Demuxer demuxer = new Demuxer();
		demuxer.open("src/examples/resources/bunny.mp4");

		MediaFrame mediaFrame;
		while ((mediaFrame = demuxer.readFrame()) != null) {
			if (mediaFrame.getType() == MediaFrame.Type.VIDEO) {
				VideoFrame frame = (VideoFrame) mediaFrame;
				muxer.addImage(frame);
			}
			if (mediaFrame.getType() == MediaFrame.Type.AUDIO) {
				AudioFrame frame = (AudioFrame) mediaFrame;
				muxer.addSamples(frame);
			}
		}

		demuxer.close();
		muxer.close();
	}

}