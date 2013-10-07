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

import java.io.IOException;
import java.nio.ByteBuffer;

public class RingBuffer {

	/** Internal data storage. */
	private ByteBuffer[] buffer;

	/** Position from where to start reading from the buffer. */
	private int[] readPointer;

	/** Position from where to start writing into the buffer. */
	private int[] writePointer;

	/** The number of bytes written to the buffer. */
	private int[] bytesToWrite;

	/** The number of bytes read from the buffer. */
	private int[] bytesToRead;

	/** The number of channels. */
	private int planes;


	/**
	 * Creates a RingBuffer with a default buffer size of 1024 bytes.
	 */
	public RingBuffer(int planes) {
		this(1024, planes);
	}

	/**
	 * Creates a RingBuffer with the specified buffer size.
	 *
	 * @param capacity buffer size in bytes
	 */
	public RingBuffer(int capacity, int planes) {
		if (capacity < 2) {
			throw new IllegalArgumentException("Buffer should have at least the capacity of 2 bytes.");
		}

		this.planes = planes;
		this.buffer = new ByteBuffer[planes];
		this.readPointer = new int[planes];
		this.writePointer = new int[planes];
		this.bytesToRead = new int[planes];
		this.bytesToWrite = new int[planes];

		for (int i = 0; i < planes; i++) {
			buffer[i] = ByteBuffer.allocateDirect(capacity);
		}

		clear();
	}

	/**
	 * Resets the read and write pointers. The internal buffer remains unaffected.
	 */
	public synchronized void clear() {
		for (int i = 0; i < planes; i++) {
			readPointer[i] = writePointer[i] = 0;
			bytesToRead[i] = 0;
			bytesToWrite[i] = buffer[i].capacity();
		}
	}

	/**
	 * Return the readable amount of bytes in buffer. Note: It is not
	 * necessarily valid when data is written to the buffer or read from the
	 * buffer. Another thread might have filled the buffer or emptied it in the
	 * mean time.
	 * 
	 * @return currently available bytes to read
	 */
	public int available() {
		return bytesToRead[0];
	}

	/**
	 * Write as much data as possible to this buffer.
	 *
	 * @param plane  the audio channel.
	 * @param buffer data to be written.
	 *
	 * @return amount of data actually written
	 */
	public synchronized int write(int plane, ByteBuffer buffer) {
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);

		return write(plane, data);
	}

	/**
	 * Write as much data as possible to this buffer.
	 *
	 * @param plane the audio channel.
	 * @param data  data to be written.
	 *
	 * @return amount of data actually written
	 */
	public synchronized int write(int plane, byte data[]) {
		return write(plane, data, 0, data.length);
	}

	/**
	 * Write as much data as possible to this buffer.
	 *
	 * @param plane  the audio channel.
	 * @param data   array holding data to be written
	 * @param offset offset of data in array
	 * @param length amount of data to write, starting from off.
	 *
	 * @return amount of data actually written
	 */
	public synchronized int write(int plane, byte data[], int offset, int length) {
		if (offset < 0 || length < 0 || length > data.length - offset) {
			throw new IndexOutOfBoundsException();
		}

		if (bytesToWrite[plane] == 0)
			return 0;

		if (bytesToWrite[plane] < length)
			length = bytesToWrite[plane];

		buffer[plane].position(writePointer[plane]);
		int partLength = buffer[plane].capacity() - writePointer[plane];

		if (partLength > length) {
			buffer[plane].put(data, offset, length);
			writePointer[plane] += length;
		}
		else {
			buffer[plane].put(data, offset, partLength);
			buffer[plane].position(0);
			buffer[plane].put(data, offset + partLength, length - partLength);
			writePointer[plane] = length - partLength;
		}

		bytesToRead[plane] += length;
		bytesToWrite[plane] -= length;

		return length;
	}

	/**
	 * Read as much data as possible from this buffer.
	 *
	 * @param plane the audio channel.
	 * @param data  where to store the data.
	 *
	 * @return number of bytes read.
	 *
	 * @throws IOException if data array could not be filled.
	 */
	public synchronized int read(int plane, byte data[]) throws IOException {
		return read(plane, data, 0, data.length);
	}

	/**
	 * Read as much data as possible from this buffer.
	 *
	 * @param plane  the audio channel.
	 * @param data   where to store the read data.
	 * @param offset offset of data in array.
	 * @param length amount of data to read.
	 *
	 * @return Amount of data read.
	 */
	public synchronized int read(int plane, byte data[], int offset, int length) throws IOException {
		if (offset < 0 || length < 0 || length > data.length - offset)
			throw new IndexOutOfBoundsException();

		if (bytesToRead[plane] == 0)
			return 0;

		if (bytesToRead[plane] < length)
			length = bytesToRead[plane];

		buffer[plane].position(readPointer[plane]);
		int partLength = buffer[plane].capacity() - readPointer[plane];

		if (partLength > length) {
			buffer[plane].get(data, offset, length);
			readPointer[plane] += length;
		}
		else {
			buffer[plane].get(data, offset, partLength);
			buffer[plane].position(0);
			buffer[plane].get(data, partLength, length - partLength);
			readPointer[plane] = length - partLength;
		}

		bytesToRead[plane] -= length;
		bytesToWrite[plane] += length;

		return length;
	}

}
