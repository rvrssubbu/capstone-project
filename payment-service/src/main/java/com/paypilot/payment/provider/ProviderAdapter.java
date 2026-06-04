package com.paypilot.payment.provider;

import java.math.BigDecimal;

public interface ProviderAdapter {
  ProviderResult authorize(String intentId, String merchantId, BigDecimal amount, String currency);
}
