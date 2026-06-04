package com.paypilot.ledger.feature;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "paypilot.features")
public class FeatureFlagService {
  private boolean settlementExport;

  public boolean isEnabled(String flagName) {
    return switch (flagName) {
      case "settlement-export" -> settlementExport;
      default -> false;
    };
  }

  public boolean isSettlementExport() {
    return settlementExport;
  }

  public void setSettlementExport(boolean settlementExport) {
    this.settlementExport = settlementExport;
  }
}
