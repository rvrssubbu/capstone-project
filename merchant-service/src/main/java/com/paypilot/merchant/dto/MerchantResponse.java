package com.paypilot.merchant.dto;

import com.paypilot.merchant.domain.KycStatus;
import java.time.Instant;

public record MerchantResponse(
    String merchantId,
    String name,
    String category,
    String email,
    String phone,
    KycStatus kycStatus,
    Instant createdAt,
    Instant updatedAt
) {
}
