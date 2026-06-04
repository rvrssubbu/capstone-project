package com.paypilot.common.web;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.MDC;

public final class CorrelationId {
  public static final String HEADER = "X-Correlation-Id";
  private static final String MDC_KEY = "correlationId";

  private CorrelationId() {
  }

  public static String current() {
    return Optional.ofNullable(MDC.get(MDC_KEY)).orElseGet(() -> {
      String generated = UUID.randomUUID().toString();
      MDC.put(MDC_KEY, generated);
      return generated;
    });
  }

  public static void set(String correlationId) {
    MDC.put(MDC_KEY, correlationId);
  }

  public static void clear() {
    MDC.remove(MDC_KEY);
  }
}
