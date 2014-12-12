package webrtc;

/** Java version of cricket::VideoCapturer. */
public class VideoCapturer {
  private long nativeVideoCapturer;
  private VideoCapturer(long nativeVideoCapturer) {
    this.nativeVideoCapturer = nativeVideoCapturer;
  }
  public static VideoCapturer create(String deviceName) {
    long nativeVideoCapturer = nativeCreateVideoCapturer(deviceName);
    if (nativeVideoCapturer == 0) {
      return null;
    }
    return new VideoCapturer(nativeVideoCapturer);
  }
  // Package-visible for PeerConnectionFactory.
  long takeNativeVideoCapturer() {
    if (nativeVideoCapturer == 0) {
      throw new RuntimeException("Capturer can only be taken once!");
    }
    long ret = nativeVideoCapturer;
    nativeVideoCapturer = 0;
    return ret;
  }
  public void dispose() {
    // No-op iff this capturer is owned by a source (see comment on
    // PeerConnectionFactoryInterface::CreateVideoSource()).
    if (nativeVideoCapturer != 0) {
      free(nativeVideoCapturer);
    }
  }
  private static native long nativeCreateVideoCapturer(String deviceName);
  private static native void free(long nativeVideoCapturer);
}