package com.paypilot.payment.controller;

import com.paypilot.payment.dto.CreatePaymentIntentRequest;
import com.paypilot.payment.dto.PaymentIntentResponse;
import com.paypilot.payment.domain.PaymentStatus;
import com.paypilot.payment.service.PaymentIntentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments/intents")
class PaymentIntentController {
  private final PaymentIntentServiceImpl service;

  PaymentIntentController(PaymentIntentServiceImpl service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  PaymentIntentResponse create(@Valid @RequestBody CreatePaymentIntentRequest request) {
    return service.create(request);
  }

  @GetMapping("/{intentId}")
  PaymentIntentResponse get(@PathVariable ("intentId") String intentId) {
    return service.get(intentId);
  }

  @GetMapping
  Page<PaymentIntentResponse> search(
      @RequestParam(name="merchantId", required = false) String merchantId,
      @RequestParam(name="status",required = false) PaymentStatus status,
      @RequestParam(name="page",defaultValue = "0") int page,
      @RequestParam(name="size",defaultValue = "20") int size) {
    return service.search(merchantId, status, page, size);
  }

  @PostMapping("/{intentId}/authorize")
  PaymentIntentResponse authorize(@PathVariable ("intentId") String intentId) {
    return service.authorize(intentId);
  }
}
