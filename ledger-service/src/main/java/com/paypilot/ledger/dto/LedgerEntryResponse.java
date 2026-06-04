package com.paypilot.ledger.dto;

import com.paypilot.ledger.domain.Direction;
import java.math.BigDecimal;
import java.time.Instant;

public record LedgerEntryResponse(
    String entryId,
    String merchantId,
    String intentId,
    BigDecimal amount,
    String currency,
    Direction direction,
    String reference,
    Instant entryTime
) {
}
