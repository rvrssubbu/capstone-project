package com.paypilot.ledger.config;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class GracefulShutdownLogger {
  private static final Logger log = LoggerFactory.getLogger(GracefulShutdownLogger.class);

  @PreDestroy
  void onShutdown() {
    log.info("Ledger service is closing consumers and allowing in-flight requests to finish.");
  }
}
