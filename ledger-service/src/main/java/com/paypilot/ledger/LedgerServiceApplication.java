package com.paypilot.ledger;

import com.paypilot.common.web.CorrelationIdFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.paypilot.ledger", "com.paypilot.common"})
public class LedgerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(LedgerServiceApplication.class, args);
  }

  @Bean
  CorrelationIdFilter correlationIdFilter() {
    return new CorrelationIdFilter();
  }
}
