package com.paypilot.merchant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateMerchantRequest(
    @NotBlank String name,
    @NotBlank String category,
    @NotBlank @Email String email,
    String phone
) {
}
