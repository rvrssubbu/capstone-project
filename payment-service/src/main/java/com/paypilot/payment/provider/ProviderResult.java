package com.paypilot.payment.provider;

public record ProviderResult(String providerTxnId, ProviderStatus status, String declineReason) {
}
