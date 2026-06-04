package com.paypilot.merchant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Merchant {
  @Id
  private String merchantId;
  private String name;
  private String category;
  private String email;
  private String phone;
  @Enumerated(EnumType.STRING)
  private KycStatus kycStatus;
  private String reason;
  private Instant createdAt;
  private Instant updatedAt;

  protected Merchant() {
  }

  public Merchant(String name, String category, String email, String phone) {
    this.merchantId = UUID.randomUUID().toString();
    this.name = name;
    this.category = category;
    this.email = email;
    this.phone = phone;
    this.kycStatus = KycStatus.PENDING;
    this.createdAt = Instant.now();
  }

  public void updateKyc(KycStatus status,String reason) {
    this.kycStatus = status;
    this.reason=reason;
    this.updatedAt = Instant.now();
  }

  public String getMerchantId() { return merchantId; }
  public String getName() { return name; }
  public String getCategory() { return category; }
  public String getEmail() { return email; }
  public String getPhone() { return phone; }
  public KycStatus getKycStatus() { return kycStatus; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public String getReason() { return reason;}
}
