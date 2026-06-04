package com.paypilot.payment.dto;

import com.paypilot.payment.domain.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record PaymentIntentResponse(
    String intentId,
    String merchantId,
    BigDecimal amount,
    String currency,
    String referenceId,
    PaymentStatus status,
    Instant createdAt,
    Instant updatedAt
) {
}
