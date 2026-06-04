package com.paypilot.ledger.service;

import com.paypilot.common.api.ApiException;
import com.paypilot.ledger.domain.LedgerEntry;
import com.paypilot.ledger.dto.CreateLedgerEntryRequest;
import com.paypilot.ledger.dto.LedgerEntryResponse;
import com.paypilot.ledger.dto.PageResponse;
import com.paypilot.ledger.repo.LedgerEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LedgerServiceImpl implements LedgerService {
  private static final Logger log = LoggerFactory.getLogger(LedgerServiceImpl.class);
  private final LedgerEntryRepository repository;

  public LedgerServiceImpl(LedgerEntryRepository repository) {
    this.repository = repository;
  }

  public LedgerEntryResponse append(CreateLedgerEntryRequest request) {
    try {
      LedgerEntry entry = new LedgerEntry(request.merchantId(), request.intentId(), request.amount(), request.currency(),
          request.direction(), request.reference());
      LedgerEntry saved = repository.save(entry);
      log.info("Appended ledger entry entryId={} merchantId={} intentId={} direction={} amount={}", saved.getEntryId(),
          saved.getMerchantId(), saved.getIntentId(), saved.getDirection(), saved.getAmount());
      return toResponse(saved);
    } catch (DataAccessException ex) {
      log.error("Failed to append ledger entry merchantId={} intentId={} direction={} amount={}", request.merchantId(),
          request.intentId(), request.direction(), request.amount(), ex);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "LEDGER_APPEND_FAILED", "Could not append ledger entry", ex);
    }
  }

  public PageResponse<LedgerEntryResponse> findByMerchant(String merchantId, int page, int size) {
    try {
      int cappedSize = Math.min(Math.max(size, 1), 100);
      var pageable = PageRequest.of(Math.max(page, 0), cappedSize,
          Sort.by("entryTime").descending().and(Sort.by("entryId").descending()));
      var result = repository.findByMerchantId(merchantId, pageable);
      log.debug("Listed ledger entries merchantId={} page={} size={} total={}", merchantId, result.getNumber(),
          result.getSize(), result.getTotalElements());
      return new PageResponse<>(result.getContent().stream().map(this::toResponse).toList(), result.getNumber(),
          result.getSize(), result.getTotalElements());
    } catch (DataAccessException ex) {
      log.error("Failed to list ledger entries merchantId={} page={} size={}", merchantId, page, size, ex);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "LEDGER_LIST_FAILED", "Could not list ledger entries", ex);
    }
  }

  private LedgerEntryResponse toResponse(LedgerEntry entry) {
    return new LedgerEntryResponse(entry.getEntryId(), entry.getMerchantId(), entry.getIntentId(), entry.getAmount(),
        entry.getCurrency(), entry.getDirection(), entry.getReference(), entry.getEntryTime());
  }
}
