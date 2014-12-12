package webrtc;

/** Interface for observing SDP-related events. */
public interface SdpObserver {
  /** Called on success of Create{Offer,Answer}(). */
  public void onCreateSuccess(SessionDescription sdp);
  /** Called on success of Set{Local,Remote}Description(). */
  public void onSetSuccess();
  /** Called on error of Create{Offer,Answer}(). */
  public void onCreateFailure(String error);
  /** Called on error of Set{Local,Remote}Description(). */
  public void onSetFailure(String error);
}