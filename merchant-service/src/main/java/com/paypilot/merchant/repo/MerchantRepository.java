package com.paypilot.merchant.repo;

import com.paypilot.merchant.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, String> {
}
