package webrtc;

import java.util.LinkedList;
/** Java version of VideoTrackInterface. */
public class VideoTrack extends MediaStreamTrack {
  private final LinkedList<VideoRenderer> renderers =
      new LinkedList<VideoRenderer>();
  public VideoTrack(long nativeTrack) {
    super(nativeTrack);
  }
  public void addRenderer(VideoRenderer renderer) {
    renderers.add(renderer);
    nativeAddRenderer(nativeTrack, renderer.nativeVideoRenderer);
  }
  public void removeRenderer(VideoRenderer renderer) {
    if (!renderers.remove(renderer)) {
      return;
    }
    nativeRemoveRenderer(nativeTrack, renderer.nativeVideoRenderer);
    renderer.dispose();
  }
  public void dispose() {
    while (!renderers.isEmpty()) {
      removeRenderer(renderers.getFirst());
    }
    super.dispose();
  }
  private static native void free(long nativeTrack);
  private static native void nativeAddRenderer(
      long nativeTrack, long nativeRenderer);
  private static native void nativeRemoveRenderer(
      long nativeTrack, long nativeRenderer);
}