# JavaAV

The aim of this project is to provide an easy to use interface to [FFmpeg]. This project is not command line based like
many others do. In addition JavaAV is easily maintainable. First, the native FFmpeg library is accessed through JNI.
There is no other extra layer of native code like in Xuggler. Second, all the JNI classes are automatically generated
with [JavaCPP]. Even the compilation into native code is controlled by JavaCPP. This way there is less effort to react
to new FFmpeg updates. To improve JavaAV or fix bugs you do not need to touch the native code. 

## Supported Platforms
* Android ARM
* Linux 	x86 / x64
* Mac OS X	x64
* Windows 	x86 / x64

## Features

* Encoding / Decoding with any codec that was compiled into FFmpeg
* Muxing / Demuxing
* Audio / Video resampling
* Log access through callback

## Code Snippets
Below some code snippets are shown to demonstrate the API usage. For more complete code see the examples section.

**Enumerate installed codecs** (each string contains _name_, _type_ and _description_):
```java
String[] codecs = Codec.getInstalledCodecs();
```
**Create and use a _real-time_ video encoder:**
```java
Options options = new Options();
options.put("tune", "zerolatency");
options.put("preset", "ultrafast");
		
Encoder encoder = new Encoder(CodecID.H264);
encoder.setPixelFormat(PixelFormat.YUV420P);
encoder.setImageWidth(1280);
encoder.setImageHeight(720);
encoder.setGOPSize(25);
encoder.setBitrate(2000000);
encoder.setFramerate(25);
encoder.open(options);

BufferedImage image = ...;
VideoFrame frame = VideoFrame.create(image);

MediaPacket packet = encoder.encodeVideo(frame);

... send packet over a network, write it to a file or whatever

encoder.close();
			
```

## Examples
Below is the list of some basic examples. All examples can be found in the projects src/examples folder.

* [Demuxer][DemuxerExample]
* [Muxer + Transcoding][MuxerExample]
* [Camera][CameraExample]

## Installation
### Maven

For those who use Maven may include this project as dependency:

```xml
<dependency>
	<groupId>com.github.hoary</groupId>
	<artifactId>JavaAV</artifactId>
	<version>0.5</version>
</dependency>
```

### Manually
You can also [download] JavaAV manually. In doing so, JavaAV and the necessary dependencies must be added to the classpath.

## License

[GPLv2 with Classpath Exception][GPLv2]

[FFmpeg]: http://www.ffmpeg.org/
[JavaCPP]: http://code.google.com/p/javacpp/
[DemuxerExample]: https://github.com/hoary/JavaAV/blob/master/JavaAV/src/examples/java/com/github/hoary/javaav/DemuxerExample.java
[MuxerExample]: https://github.com/hoary/JavaAV/blob/master/JavaAV/src/examples/java/com/github/hoary/javaav/MuxerExample.java
[CameraExample]: https://github.com/hoary/JavaAV/blob/master/JavaAV/src/examples/java/com/github/hoary/javaav/CameraExample.java
[GPLv2]: https://raw.github.com/hoary/JavaAV/master/LICENSE
[download]: https://github.com/hoary/JavaAV/blob/master
