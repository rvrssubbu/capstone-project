package com.paypilot.ledger.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class LedgerEntry {
  @Id
  private String entryId;
  private String merchantId;
  private String intentId;
  private BigDecimal amount;
  private String currency;
  @Enumerated(EnumType.STRING)
  private Direction direction;
  private String reference;
  private Instant entryTime;

  protected LedgerEntry() {
  }

  public LedgerEntry(String merchantId, String intentId, BigDecimal amount, String currency, Direction direction, String reference) {
    this.entryId = UUID.randomUUID().toString();
    this.merchantId = merchantId;
    this.intentId = intentId;
    this.amount = amount;
    this.currency = currency;
    this.direction = direction;
    this.reference = reference;
    this.entryTime = Instant.now();
  }

  public String getEntryId() { return entryId; }
  public String getMerchantId() { return merchantId; }
  public String getIntentId() { return intentId; }
  public BigDecimal getAmount() { return amount; }
  public String getCurrency() { return currency; }
  public Direction getDirection() { return direction; }
  public String getReference() { return reference; }
  public Instant getEntryTime() { return entryTime; }
}
