package com.paypilot.merchant.service;

import com.paypilot.common.api.ApiException;
import com.paypilot.merchant.dto.CreateMerchantRequest;
import com.paypilot.merchant.dto.KycUpdateRequest;
import com.paypilot.merchant.dto.MerchantResponse;
import com.paypilot.merchant.domain.Merchant;
import com.paypilot.merchant.repo.MerchantRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService{
  private final MerchantRepository repository;

  public MerchantServiceImpl(MerchantRepository repository) {
    this.repository = repository;
  }

  public MerchantResponse create(CreateMerchantRequest request) {
    return toResponse(repository.save(new Merchant(request.name(), request.category(), request.email(), request.phone())));
  }

  public MerchantResponse get(String merchantId) {
    return toResponse(find(merchantId));
  }

  public List<MerchantResponse> list() {
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public MerchantResponse updateKyc(String merchantId, KycUpdateRequest request) {
    Merchant merchant = find(merchantId);
    merchant.updateKyc(request.status(),request.reason());
    return toResponse(repository.save(merchant));
  }

  private Merchant find(String merchantId) {
    return repository.findById(merchantId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "MERCHANT_NOT_FOUND", "Merchant not found"));
  }

  private MerchantResponse toResponse(Merchant merchant) {
    return new MerchantResponse(merchant.getMerchantId(), merchant.getName(), merchant.getCategory(), merchant.getEmail(),
        merchant.getPhone(), merchant.getKycStatus(), merchant.getCreatedAt(), merchant.getUpdatedAt());
  }
}
