package com.paypilot.ledger.controller;

import com.paypilot.ledger.service.SettlementExportServiceImpl;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/settlement")
class SettlementAdminController {
  private final SettlementExportServiceImpl exportService;

  SettlementAdminController(SettlementExportServiceImpl exportService) {
    this.exportService = exportService;
  }

  @PostMapping("/export")
  SettlementExportServiceImpl.ExportResponse export(@RequestParam(required = false) LocalDate date) {
    return exportService.export(date == null ? LocalDate.now() : date);
  }
}
