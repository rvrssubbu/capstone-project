package com.paypilot.ledger.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.paypilot.common.api.ApiException;
import com.paypilot.ledger.domain.Direction;
import com.paypilot.ledger.domain.LedgerEntry;
import com.paypilot.ledger.dto.CreateLedgerEntryRequest;
import com.paypilot.ledger.feature.FeatureFlagService;
import com.paypilot.ledger.repo.LedgerEntryRepository;
import com.paypilot.ledger.storage.StorageService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class LedgerServiceImplTest {
  @Mock
  private LedgerEntryRepository repository;

  @Mock
  private StorageService storageService;

  @Mock
  private FeatureFlagService featureFlags;

  private LedgerServiceImpl ledgerService;
  private SettlementExportServiceImpl exportService;

  @BeforeEach
  void setUp() {
    ledgerService = new LedgerServiceImpl(repository);
    exportService = new SettlementExportServiceImpl(repository, storageService, featureFlags);
  }

  @Test
  void appendPersistsLedgerEntry() {
    when(repository.save(any(LedgerEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var response = ledgerService.append(new CreateLedgerEntryRequest("merchant-1", "intent-1", new BigDecimal("42.00"),
        "INR", Direction.CREDIT, "payment authorized"));

    assertThat(response.entryId()).isNotBlank();
    assertThat(response.merchantId()).isEqualTo("merchant-1");
    assertThat(response.intentId()).isEqualTo("intent-1");
    assertThat(response.amount()).isEqualByComparingTo("42.00");
    assertThat(response.direction()).isEqualTo(Direction.CREDIT);
    verify(repository).save(any(LedgerEntry.class));
  }

  @Test
  void findByMerchantReturnsPagedLedgerEntries() {
    LedgerEntry entry = new LedgerEntry("merchant-1", "intent-1", new BigDecimal("42.00"), "INR", Direction.CREDIT,
        "payment authorized");
    when(repository.findByMerchantId(eq("merchant-1"), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(entry)));

    var response = ledgerService.findByMerchant("merchant-1", 0, 20);

    assertThat(response.content()).hasSize(1);
    assertThat(response.totalElements()).isEqualTo(1);
    assertThat(response.content().get(0).merchantId()).isEqualTo("merchant-1");
  }

  @Test
  void exportThrowsApiExceptionWhenFeatureFlagIsDisabled() {
    when(featureFlags.isEnabled("settlement-export")).thenReturn(false);

    assertThatThrownBy(() -> exportService.export(LocalDate.of(2026, 6, 4)))
        .isInstanceOf(ApiException.class)
        .extracting("status", "code")
        .containsExactly(HttpStatus.CONFLICT, "FEATURE_DISABLED");
  }

  @Test
  void exportWritesCsvToStorageWhenFeatureFlagIsEnabled() {
    LedgerEntry entry = new LedgerEntry("merchant-1", "intent-1", new BigDecimal("42.00"), "INR", Direction.CREDIT,
        "payment authorized");
    when(featureFlags.isEnabled("settlement-export")).thenReturn(true);
    when(repository.findAll()).thenReturn(List.of(entry));
    when(storageService.list("settlements/2026-06-04")).thenReturn(List.of("settlements/2026-06-04/ledger-export.csv"));
    ArgumentCaptor<byte[]> csvCaptor = ArgumentCaptor.forClass(byte[].class);

    var response = exportService.export(LocalDate.of(2026, 6, 4));

    verify(storageService).write(eq("settlements/2026-06-04/ledger-export.csv"), csvCaptor.capture());
    assertThat(new String(csvCaptor.getValue())).contains("entryId,merchantId,intentId,amount,currency,direction,entryTime")
        .contains("merchant-1")
        .contains("intent-1")
        .contains("CREDIT");
    assertThat(response.storageKey()).isEqualTo("settlements/2026-06-04/ledger-export.csv");
    assertThat(response.objectsForDate()).isEqualTo(1);
  }
}