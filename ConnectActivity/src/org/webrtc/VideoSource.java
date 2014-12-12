package org.webrtc;

/**
 * Java version of VideoSourceInterface, extended with stop/restart
 * functionality to allow explicit control of the camera device on android,
 * where there is no support for multiple open capture devices and the cost of
 * holding a camera open (even if MediaStreamTrack.setEnabled(false) is muting
 * its output to the encoder) can be too high to bear.
 */
public class VideoSource extends MediaSource {
  private long nativeVideoFormatAtStop;
  public VideoSource(long nativeSource) {
    super(nativeSource);
  }
  // Stop capture feeding this source.
  public void stop() {
    nativeVideoFormatAtStop = stop(nativeSource);
  }
  // Restart capture feeding this source.  stop() must have been called since
  // the last call to restart() (if any).  Note that this isn't "start()";
  // sources are started by default at birth.
  public void restart() {
    restart(nativeSource, nativeVideoFormatAtStop);
    nativeVideoFormatAtStop = 0;
  }
  @Override
  public void dispose() {
    if (nativeVideoFormatAtStop != 0) {
      freeNativeVideoFormat(nativeVideoFormatAtStop);
      nativeVideoFormatAtStop = 0;
    }
    super.dispose();
  }
  // This stop() returns an owned C++ VideoFormat pointer for use in restart()
  // and dispose().
  private static native long stop(long nativeSource);
  private static native void restart(
      long nativeSource, long nativeVideoFormatAtStop);
  private static native void freeNativeVideoFormat(long nativeVideoFormat);
}