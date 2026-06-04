package com.paypilot.payment.service;

import com.paypilot.common.api.ApiException;
import com.paypilot.payment.domain.PaymentIntent;
import com.paypilot.payment.domain.PaymentStatus;
import com.paypilot.payment.dto.CreatePaymentIntentRequest;
import com.paypilot.payment.dto.PaymentIntentResponse;
import com.paypilot.payment.provider.ProviderAdapter;
import com.paypilot.payment.provider.ProviderStatus;
import com.paypilot.payment.repo.PaymentIntentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

public interface PaymentIntentService {
    public PaymentIntentResponse create(CreatePaymentIntentRequest request);

    public PaymentIntentResponse get(String intentId);

    public Page<PaymentIntentResponse> search(String merchantId, PaymentStatus status, int page, int size);

    public PaymentIntentResponse authorize(String intentId);
}
