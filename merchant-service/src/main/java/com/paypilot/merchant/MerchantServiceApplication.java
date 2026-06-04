package com.paypilot.merchant;

import com.paypilot.common.web.CorrelationIdFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.paypilot.merchant", "com.paypilot.common"})
public class MerchantServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(MerchantServiceApplication.class, args);
  }

  @Bean
  CorrelationIdFilter correlationIdFilter() {
    return new CorrelationIdFilter();
  }
}
