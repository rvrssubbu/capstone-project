package com.paypilot.payment.service;

import com.paypilot.common.api.ApiException;
import com.paypilot.payment.dto.CreatePaymentIntentRequest;
import com.paypilot.payment.dto.PaymentIntentResponse;
import com.paypilot.payment.domain.PaymentIntent;
import com.paypilot.payment.domain.PaymentStatus;
import com.paypilot.payment.provider.ProviderAdapter;
import com.paypilot.payment.provider.ProviderStatus;
import com.paypilot.payment.repo.PaymentIntentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentIntentServiceImpl implements PaymentIntentService{
  private final PaymentIntentRepository repository;
  private final ProviderAdapter providerAdapter;

  public PaymentIntentServiceImpl(PaymentIntentRepository repository, ProviderAdapter providerAdapter) {
    this.repository = repository;
    this.providerAdapter = providerAdapter;
  }

  public PaymentIntentResponse create(CreatePaymentIntentRequest request) {
    PaymentIntent intent = new PaymentIntent(request.merchantId(), request.amount(), request.currency(), request.referenceId());
    return toResponse(repository.save(intent));
  }

  public PaymentIntentResponse get(String intentId) {
    return toResponse(find(intentId));
  }

  public Page<PaymentIntentResponse> search(String merchantId, PaymentStatus status, int page, int size) {
    var pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), Sort.by("createdAt").descending());
    Page<PaymentIntent> result;
    if (merchantId != null && status != null) {
      result = repository.findByMerchantIdAndStatus(merchantId, status, pageable);
    } else if (merchantId != null) {
      result = repository.findByMerchantId(merchantId, pageable);
    } else if (status != null) {
      result = repository.findByStatus(status, pageable);
    } else {
      result = repository.findAll(pageable);
    }
    return result.map(this::toResponse);
  }

  public PaymentIntentResponse authorize(String intentId) {
    PaymentIntent intent = find(intentId);
    var result = providerAdapter.authorize(intent.getIntentId(), intent.getMerchantId(), intent.getAmount(), intent.getCurrency());
    if (result.status() == ProviderStatus.SUCCESS) {
      intent.markAuthorized();
    } else {
      intent.markFailed();
    }
    return toResponse(repository.save(intent));
  }

  private PaymentIntent find(String intentId) {
    return repository.findById(intentId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "PAYMENT_INTENT_NOT_FOUND", "Payment intent not found"));
  }

  private PaymentIntentResponse toResponse(PaymentIntent intent) {
    return new PaymentIntentResponse(intent.getIntentId(), intent.getMerchantId(), intent.getAmount(), intent.getCurrency(),
        intent.getReferenceId(), intent.getStatus(), intent.getCreatedAt(), intent.getUpdatedAt());
  }
}
