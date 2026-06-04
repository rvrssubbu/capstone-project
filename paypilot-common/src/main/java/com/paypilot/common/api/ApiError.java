package com.paypilot.common.api;

import java.time.Instant;
import java.util.List;

public record ApiError(
    Instant timestamp,
    String traceId,
    String code,
    String message,
    List<String> details
) {
  public static ApiError of(String traceId, String code, String message, List<String> details) {
    return new ApiError(Instant.now(), traceId, code, message, details);
  }
}
