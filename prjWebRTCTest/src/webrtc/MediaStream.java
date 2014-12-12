package webrtc;

import java.util.LinkedList;
/** Java wrapper for a C++ MediaStreamInterface. */
public class MediaStream {
  public final LinkedList<AudioTrack> audioTracks;
  public final LinkedList<VideoTrack> videoTracks;
  // Package-protected for PeerConnection.
  final long nativeStream;
  public MediaStream(long nativeStream) {
    audioTracks = new LinkedList<AudioTrack>();
    videoTracks = new LinkedList<VideoTrack>();
    this.nativeStream = nativeStream;
  }
  public boolean addTrack(AudioTrack track) {
    if (nativeAddAudioTrack(nativeStream, track.nativeTrack)) {
      audioTracks.add(track);
      return true;
    }
    return false;
  }
  public boolean addTrack(VideoTrack track) {
    if (nativeAddVideoTrack(nativeStream, track.nativeTrack)) {
      videoTracks.add(track);
      return true;
    }
    return false;
  }
  public boolean removeTrack(AudioTrack track) {
    if (nativeRemoveAudioTrack(nativeStream, track.nativeTrack)) {
      audioTracks.remove(track);
      return true;
    }
    return false;
  }
  public boolean removeTrack(VideoTrack track) {
    if (nativeRemoveVideoTrack(nativeStream, track.nativeTrack)) {
      videoTracks.remove(track);
      return true;
    }
    return false;
  }
  public void dispose() {
    while (!audioTracks.isEmpty()) {
      AudioTrack track = audioTracks.getFirst();
      removeTrack(track);
      track.dispose();
    }
    while (!videoTracks.isEmpty()) {
      VideoTrack track = videoTracks.getFirst();
      removeTrack(track);
      track.dispose();
    }
    free(nativeStream);
  }
  public String label() {
    return nativeLabel(nativeStream);
  }
  public String toString() {
    return "[" + label() + ":A=" + audioTracks.size() +
        ":V=" + videoTracks.size() + "]";
  }
  private static native boolean nativeAddAudioTrack(
      long nativeStream, long nativeAudioTrack);
  private static native boolean nativeAddVideoTrack(
      long nativeStream, long nativeVideoTrack);
  private static native boolean nativeRemoveAudioTrack(
      long nativeStream, long nativeAudioTrack);
  private static native boolean nativeRemoveVideoTrack(
      long nativeStream, long nativeVideoTrack);
  private static native String nativeLabel(long nativeStream);
  private static native void free(long nativeStream);
}