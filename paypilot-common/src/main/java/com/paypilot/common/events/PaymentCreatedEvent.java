package com.paypilot.common.events;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentCreatedEvent(
    String version,
    String eventId,
    String intentId,
    String merchantId,
    BigDecimal amount,
    String currency,
    Instant createdAt
) {
}
