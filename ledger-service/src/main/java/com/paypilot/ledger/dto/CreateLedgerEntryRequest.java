package com.paypilot.ledger.dto;

import com.paypilot.ledger.domain.Direction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateLedgerEntryRequest(
    @NotBlank String merchantId,
    @NotBlank String intentId,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotBlank String currency,
    @NotNull Direction direction,
    String reference
) {
}
