package com.github.hoary.javaav;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.util.Arrays;

/**
 * Unit test for codecs.
 */
public class CodecTest extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public CodecTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CodecTest.class);
	}

	/**
	 * Test codec capabilities.
	 */
	public void testCodec() throws JavaAVException {
		Codec codec = Codec.getEncoderById(CodecID.MP3);

		System.out.println("Testing audio codec " + codec.getID());
		System.out.println("Sample rates: " + Arrays.toString(codec.getSupportedSampleRates()));
		System.out.println("Channel layouts: " + Arrays.toString(codec.getSupportedChannelLayouts()));
		System.out.println("Pixel formats: " + Arrays.toString(codec.getSupportedPixelFormats()));

		Assert.assertNotNull(codec.getSupportedSampleRates());
		Assert.assertNotNull(codec.getSupportedChannelLayouts());
		Assert.assertNull(codec.getSupportedPixelFormats());

		codec = Codec.getEncoderById(CodecID.H264);

		System.out.println();
		System.out.println("Testing video codec " + codec.getID());
		System.out.println("Pixel formats: " + Arrays.toString(codec.getSupportedPixelFormats()));

		Assert.assertNotNull(codec.getSupportedPixelFormats());
	}

}
