package webrtc;

/**
 * Description of an RFC 4566 Session.
 * SDPs are passed as serialized Strings in Java-land and are materialized
 * to SessionDescriptionInterface as appropriate in the JNI layer.
 */
public class SessionDescription {
  /** Java-land enum version of SessionDescriptionInterface's type() string. */
  public static enum Type {
    OFFER, PRANSWER, ANSWER;
    public String canonicalForm() {
      return name().toLowerCase();
    }
    public static Type fromCanonicalForm(String canonical) {
      return Type.valueOf(Type.class, canonical.toUpperCase());
    }
  }
  public final Type type;
  public final String description;
  public SessionDescription(Type type, String description) {
    this.type = type;
    this.description = description;
  }
}