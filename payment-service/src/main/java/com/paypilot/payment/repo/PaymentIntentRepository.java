package com.paypilot.payment.repo;

import com.paypilot.payment.domain.PaymentIntent;
import com.paypilot.payment.domain.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentIntentRepository extends JpaRepository<PaymentIntent, String> {
  Page<PaymentIntent> findByMerchantId(String merchantId, Pageable pageable);

  Page<PaymentIntent> findByStatus(PaymentStatus status, Pageable pageable);

  Page<PaymentIntent> findByMerchantIdAndStatus(String merchantId, PaymentStatus status, Pageable pageable);
}
