package webrtc;

import java.util.EnumSet;
/** Java wrapper for WebRTC & libjingle logging. */
public class Logging {
  static {
    System.loadLibrary("jingle_peerconnection_so");
  }
  // Keep in sync with webrtc/common_types.h:TraceLevel.
  public enum TraceLevel {
    TRACE_NONE(0x0000),
    TRACE_STATEINFO(0x0001),
    TRACE_WARNING(0x0002),
    TRACE_ERROR(0x0004),
    TRACE_CRITICAL(0x0008),
    TRACE_APICALL(0x0010),
    TRACE_DEFAULT(0x00ff),
    TRACE_MODULECALL(0x0020),
    TRACE_MEMORY(0x0100),
    TRACE_TIMER(0x0200),
    TRACE_STREAM(0x0400),
    TRACE_DEBUG(0x0800),
    TRACE_INFO(0x1000),
    TRACE_TERSEINFO(0x2000),
    TRACE_ALL(0xffff);
    public final int level;
    TraceLevel(int level) {
      this.level = level;
    }
  };
  // Keep in sync with talk/base/logging.h:LoggingSeverity.
  public enum Severity {
    LS_SENSITIVE, LS_VERBOSE, LS_INFO, LS_WARNING, LS_ERROR,
  };
  // Enable tracing to |path| of messages of |levels| and |severity|.
  // On Android, use "logcat:" for |path| to send output there.
  public static void enableTracing(
      String path, EnumSet<TraceLevel> levels, Severity severity) {
    int nativeLevel = 0;
    for (TraceLevel level : levels) {
      nativeLevel |= level.level;
    }
    nativeEnableTracing(path, nativeLevel, severity.ordinal());
  }
  private static native void nativeEnableTracing(
      String path, int nativeLevels, int nativeSeverity);
}