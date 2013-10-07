package com.github.hoary.javaav;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Unit test for encoders.
 */
public class EncoderTest extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public EncoderTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(EncoderTest.class);
	}

	/**
	 * Test encoder.
	 */
	public void testEncoder() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/test/resources/test.png"));
		}
		catch (IOException e) {
			Assert.fail("Image could not be loaded.");
		}

		ByteBuffer imageBuffer = Image.createImageBuffer(image);
		PixelFormat format = Image.getPixelFormat(image);

		Assert.assertNotNull("No image data available.", imageBuffer);
		Assert.assertEquals("Invalid image data configuration.", 0, imageBuffer.position());
		Assert.assertNotNull("Invalid pixel format.", format);

		VideoFrame frame = VideoFrame.create(image);

		Encoder encoder = null;
		try {
			encoder = new Encoder(CodecID.H264);
			encoder.setMediaType(MediaType.VIDEO);
			encoder.setPixelFormat(PixelFormat.YUV420P);
			encoder.setImageWidth(640);
			encoder.setImageHeight(480);
			encoder.setGOPSize(25);
			encoder.setBitrate(400000);
			encoder.setFramerate(25);
			encoder.open(null);
		}
		catch (JavaAVException e) {
			Assert.fail("Encoder could not be initialized: " + e.getMessage());
		}

		try {
			for (int i = 0; i < 100; i++) {
				encoder.encodeVideo(frame);
			}
		}
		catch (JavaAVException e) {
			Assert.fail("Could not encode video frame: " + e.getMessage());
		}

		encoder.close();
	}
}
