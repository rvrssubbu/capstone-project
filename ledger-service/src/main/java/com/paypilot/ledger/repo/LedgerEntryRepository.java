package com.paypilot.ledger.repo;

import com.paypilot.ledger.domain.LedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, String> {
  Page<LedgerEntry> findByMerchantId(String merchantId, Pageable pageable);
}
