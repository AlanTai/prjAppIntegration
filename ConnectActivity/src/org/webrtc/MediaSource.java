package org.webrtc;

/** Java wrapper for a C++ MediaSourceInterface. */
public class MediaSource {
  /** Tracks MediaSourceInterface.SourceState */
  public enum State {
    INITIALIZING, LIVE, ENDED, MUTED
  }
  final long nativeSource;  // Package-protected for PeerConnectionFactory.
  public MediaSource(long nativeSource) {
    this.nativeSource = nativeSource;
  }
  public State state() {
    return nativeState(nativeSource);
  }
  public void dispose() {
    free(nativeSource);
  }
  private static native State nativeState(long pointer);
  private static native void free(long nativeSource);
}