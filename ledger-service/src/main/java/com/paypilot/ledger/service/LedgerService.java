package com.paypilot.ledger.service;

import com.paypilot.ledger.domain.LedgerEntry;
import com.paypilot.ledger.dto.CreateLedgerEntryRequest;
import com.paypilot.ledger.dto.LedgerEntryResponse;
import com.paypilot.ledger.dto.PageResponse;
import com.paypilot.ledger.repo.LedgerEntryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

public interface LedgerService {

  public LedgerEntryResponse append(CreateLedgerEntryRequest request);

  public PageResponse<LedgerEntryResponse> findByMerchant(String merchantId, int page, int size);

}
