package com.paypilot.ledger.service;

import com.paypilot.common.api.ApiException;
import com.paypilot.ledger.feature.FeatureFlagService;
import com.paypilot.ledger.repo.LedgerEntryRepository;
import com.paypilot.ledger.storage.StorageException;
import com.paypilot.ledger.storage.StorageService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SettlementExportServiceImpl {
  private static final Logger log = LoggerFactory.getLogger(SettlementExportServiceImpl.class);
  private static final String FLAG = "settlement-export";
  private final LedgerEntryRepository repository;
  private final StorageService storageService;
  private final FeatureFlagService featureFlags;

  public SettlementExportServiceImpl(
      LedgerEntryRepository repository,
      StorageService storageService,
      FeatureFlagService featureFlags) {
    this.repository = repository;
    this.storageService = storageService;
    this.featureFlags = featureFlags;
  }

  public ExportResponse export(LocalDate date) {
    if (!featureFlags.isEnabled(FLAG)) {
      log.warn("Settlement export blocked because feature flag is disabled date={}", date);
      throw new ApiException(HttpStatus.CONFLICT, "FEATURE_DISABLED", "Settlement export feature is disabled");
    }

    String key = "settlements/" + date + "/ledger-export.csv";
    try {
      StringBuilder csv = new StringBuilder("entryId,merchantId,intentId,amount,currency,direction,entryTime\n");
      repository.findAll().forEach(entry -> csv.append(entry.getEntryId()).append(',')
          .append(entry.getMerchantId()).append(',')
          .append(entry.getIntentId()).append(',')
          .append(entry.getAmount()).append(',')
          .append(entry.getCurrency()).append(',')
          .append(entry.getDirection()).append(',')
          .append(entry.getEntryTime()).append('\n'));
      storageService.write(key, csv.toString().getBytes(StandardCharsets.UTF_8));
      int objectsForDate = storageService.list("settlements/" + date).size();
      log.info("Exported settlement ledger date={} storageKey={} objectsForDate={}", date, key, objectsForDate);
      return new ExportResponse(key, objectsForDate);
    } catch (DataAccessException ex) {
      log.error("Failed to read ledger entries for settlement export date={}", date, ex);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "SETTLEMENT_EXPORT_READ_FAILED",
          "Could not read ledger entries for settlement export", ex);
    } catch (StorageException ex) {
      log.error("Failed to write settlement export date={} storageKey={}", date, key, ex);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "SETTLEMENT_EXPORT_STORAGE_FAILED",
          "Could not store settlement export", ex);
    }
  }

  public record ExportResponse(String storageKey, int objectsForDate) {
  }
}
