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

import org.bytedeco.javacpp.avcodec.AVPacket;

import java.nio.ByteBuffer;

import static org.bytedeco.javacpp.avcodec.av_free_packet;

public class MediaPacket {

	private AVPacket avPacket;

	private ByteBuffer packetData;

	private boolean keyFrame;


	MediaPacket(AVPacket avPacket) {
		this.avPacket = avPacket;
	}

	AVPacket getAVPacket() {
		return avPacket;
	}

	public MediaPacket(ByteBuffer data) {
		this.packetData = data;
	}

	public ByteBuffer getData() {
		if (packetData == null && avPacket != null)
			packetData = avPacket.data().limit(avPacket.size()).asByteBuffer();

		return packetData;
	}

	public void setKeyFrame(boolean keyFrame) {
		this.keyFrame = keyFrame;
	}

	public boolean isKeyFrame() {
		return keyFrame;
	}

	public void clear() {
		packetData = null;

		if (avPacket != null && !avPacket.isNull())
			av_free_packet(avPacket);
	}

}
