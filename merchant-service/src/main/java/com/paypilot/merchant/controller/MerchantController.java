package com.paypilot.merchant.controller;

import com.paypilot.merchant.dto.CreateMerchantRequest;
import com.paypilot.merchant.dto.KycUpdateRequest;
import com.paypilot.merchant.dto.MerchantResponse;
import com.paypilot.merchant.service.MerchantServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/merchants")
class MerchantController {
  private final MerchantServiceImpl service;

  MerchantController(MerchantServiceImpl service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  MerchantResponse create(@Valid @RequestBody CreateMerchantRequest request) {
    return service.create(request);
  }

  @GetMapping("/{merchantId}")
  MerchantResponse get(@PathVariable("merchantId") String merchantId) {
    return service.get(merchantId);
  }

  @GetMapping
  List<MerchantResponse> list() {
    return service.list();
  }

  @PutMapping("/{merchantId}/kyc")
  MerchantResponse updateKyc(@PathVariable("merchantId") String merchantId, @Valid @RequestBody KycUpdateRequest request) {
    return service.updateKyc(merchantId, request);
  }
}
