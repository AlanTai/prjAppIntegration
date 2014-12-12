package webrtc;

/** Interface for observing Stats reports (see webrtc::StatsObservers). */
public interface StatsObserver {
  /** Called when the reports are ready.*/
  public void onComplete(StatsReport[] reports);
}