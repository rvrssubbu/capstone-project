package com.paypilot.ledger.controller;

import com.paypilot.ledger.dto.CreateLedgerEntryRequest;
import com.paypilot.ledger.dto.LedgerEntryResponse;
import com.paypilot.ledger.dto.PageResponse;
import com.paypilot.ledger.service.LedgerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ledger/entries")
class LedgerController {
  private final LedgerServiceImpl service;

  LedgerController(LedgerServiceImpl service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  LedgerEntryResponse append(@Valid @RequestBody CreateLedgerEntryRequest request) {
    return service.append(request);
  }

  @GetMapping
  PageResponse<LedgerEntryResponse> findByMerchant(
      @RequestParam ("merchantId") String merchantId,
      @RequestParam(name="page",defaultValue = "0") int page,
      @RequestParam(name="size",defaultValue = "20") int size) {
    return service.findByMerchant(merchantId, page, size);
  }
}
