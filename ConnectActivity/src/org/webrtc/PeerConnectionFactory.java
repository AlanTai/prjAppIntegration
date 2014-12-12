package org.webrtc;

import java.util.List;
/**
 * Java wrapper for a C++ PeerConnectionFactoryInterface.  Main entry point to
 * the PeerConnection API for clients.
 */
public class PeerConnectionFactory {
  static {
    System.loadLibrary("jingle_peerconnection_so");
  }
  private final long nativeFactory;
  // |context| is an android.content.Context object, but we keep it untyped here
  // to allow building on non-Android platforms.
  public static native boolean initializeAndroidGlobals(Object context);
  public PeerConnectionFactory() {
    nativeFactory = nativeCreatePeerConnectionFactory();
    if (nativeFactory == 0) {
      throw new RuntimeException("Failed to initialize PeerConnectionFactory!");
    }
  }
  public PeerConnection createPeerConnection(
      List<PeerConnection.IceServer> iceServers,
      MediaConstraints constraints,
      PeerConnection.Observer observer) {
    long nativeObserver = nativeCreateObserver(observer);
    if (nativeObserver == 0) {
      return null;
    }
    long nativePeerConnection = nativeCreatePeerConnection(
        nativeFactory, iceServers, constraints, nativeObserver);
    if (nativePeerConnection == 0) {
      return null;
    }
    return new PeerConnection(nativePeerConnection, nativeObserver);
  }
  public MediaStream createLocalMediaStream(String label) {
    return new MediaStream(
        nativeCreateLocalMediaStream(nativeFactory, label));
  }
  public VideoSource createVideoSource(
      VideoCapturer capturer, MediaConstraints constraints) {
    return new VideoSource(nativeCreateVideoSource(
        nativeFactory, capturer.takeNativeVideoCapturer(), constraints));
  }
  public VideoTrack createVideoTrack(String id, VideoSource source) {
    return new VideoTrack(nativeCreateVideoTrack(
        nativeFactory, id, source.nativeSource));
  }
  public AudioTrack createAudioTrack(String id) {
    return new AudioTrack(nativeCreateAudioTrack(nativeFactory, id));
  }
  public void dispose() {
    freeFactory(nativeFactory);
  }
  private static native long nativeCreatePeerConnectionFactory();
  private static native long nativeCreateObserver(
      PeerConnection.Observer observer);
  private static native long nativeCreatePeerConnection(
      long nativeFactory, List<PeerConnection.IceServer> iceServers,
      MediaConstraints constraints, long nativeObserver);
  private static native long nativeCreateLocalMediaStream(
      long nativeFactory, String label);
  private static native long nativeCreateVideoSource(
      long nativeFactory, long nativeVideoCapturer,
      MediaConstraints constraints);
  private static native long nativeCreateVideoTrack(
      long nativeFactory, String id, long nativeVideoSource);
  private static native long nativeCreateAudioTrack(
      long nativeFactory, String id);
  private static native void freeFactory(long nativeFactory);
}