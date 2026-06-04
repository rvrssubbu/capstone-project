package com.paypilot.payment.provider;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class SimulatedProviderAdapter implements ProviderAdapter {
  @Override
  public ProviderResult authorize(String intentId, String merchantId, BigDecimal amount, String currency) {
    if (amount.compareTo(BigDecimal.valueOf(10_000)) > 0) {
      return new ProviderResult(null, ProviderStatus.DECLINED, "Amount exceeds simulated provider limit");
    }
    return new ProviderResult(UUID.randomUUID().toString(), ProviderStatus.SUCCESS, null);
  }
}
