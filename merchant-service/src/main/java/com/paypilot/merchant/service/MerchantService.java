package com.paypilot.merchant.service;

import com.paypilot.common.api.ApiException;
import com.paypilot.merchant.domain.Merchant;
import com.paypilot.merchant.dto.CreateMerchantRequest;
import com.paypilot.merchant.dto.KycUpdateRequest;
import com.paypilot.merchant.dto.MerchantResponse;
import com.paypilot.merchant.repo.MerchantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MerchantService {

  public MerchantResponse create(CreateMerchantRequest request);

  public MerchantResponse get(String merchantId);

  public List<MerchantResponse> list();
}
