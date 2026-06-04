package com.paypilot.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class PaymentIntent {
  @Id
  private String intentId;
  private String merchantId;
  private BigDecimal amount;
  private String currency;
  private String referenceId;
  @Enumerated(EnumType.STRING)
  private PaymentStatus status;
  private Instant createdAt;
  private Instant updatedAt;

  protected PaymentIntent() {
  }

  public PaymentIntent(String merchantId, BigDecimal amount, String currency, String referenceId) {
    this.intentId = UUID.randomUUID().toString();
    this.merchantId = merchantId;
    this.amount = amount;
    this.currency = currency;
    this.referenceId = referenceId;
    this.status = PaymentStatus.CREATED;
    this.createdAt = Instant.now();
  }

  public void markAuthorized() {
    this.status = PaymentStatus.AUTHORIZED;
    this.updatedAt = Instant.now();
  }

  public void markFailed() {
    this.status = PaymentStatus.FAILED;
    this.updatedAt = Instant.now();
  }

  public String getIntentId() { return intentId; }
  public String getMerchantId() { return merchantId; }
  public BigDecimal getAmount() { return amount; }
  public String getCurrency() { return currency; }
  public String getReferenceId() { return referenceId; }
  public PaymentStatus getStatus() { return status; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
