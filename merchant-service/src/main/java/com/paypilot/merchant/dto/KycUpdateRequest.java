package com.paypilot.merchant.dto;

import com.paypilot.merchant.domain.KycStatus;
import jakarta.validation.constraints.NotNull;

public record KycUpdateRequest(@NotNull KycStatus status, String reason) {
}
