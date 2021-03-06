package org.webrtc;

/** Java wrapper for a C++ MediaStreamTrackInterface. */
public class MediaStreamTrack {
  /** Tracks MediaStreamTrackInterface.TrackState */
  public enum State {
    INITIALIZING, LIVE, ENDED, FAILED
  }
  final long nativeTrack;
  public MediaStreamTrack(long nativeTrack) {
    this.nativeTrack = nativeTrack;
  }
  public String id() {
    return nativeId(nativeTrack);
  }
  public String kind() {
    return nativeKind(nativeTrack);
  }
  public boolean enabled() {
    return nativeEnabled(nativeTrack);
  }
  public boolean setEnabled(boolean enable) {
    return nativeSetEnabled(nativeTrack, enable);
  }
  public State state() {
    return nativeState(nativeTrack);
  }
  public boolean setState(State newState) {
    return nativeSetState(nativeTrack, newState.ordinal());
  }
  public void dispose() {
    free(nativeTrack);
  }
  private static native String nativeId(long nativeTrack);
  private static native String nativeKind(long nativeTrack);
  private static native boolean nativeEnabled(long nativeTrack);
  private static native boolean nativeSetEnabled(
      long nativeTrack, boolean enabled);
  private static native State nativeState(long nativeTrack);
  private static native boolean nativeSetState(
      long nativeTrack, int newState);
  private static native void free(long nativeTrack);
}
