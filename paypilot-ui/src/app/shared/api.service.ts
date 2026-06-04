import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments';
import { LedgerPage, Merchant, PaymentIntent } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private readonly http: HttpClient) {}

  listMerchants() {
    return this.http.get<Merchant[]>(`${this.baseUrl}/api/v1/merchants`);
  }

  createMerchant(payload: Partial<Merchant>) {
    return this.http.post<Merchant>(`${this.baseUrl}/api/v1/merchants`, payload);
  }

  updateKyc(merchantId: string, status: string, reason: string) {
    return this.http.put<Merchant>(`${this.baseUrl}/api/v1/merchants/${merchantId}/kyc`, { status, reason });
  }

  searchPayments(filters: { merchantId?: string; status?: string; page?: number; size?: number }) {
    let params = new HttpParams()
      .set('page', String(filters.page ?? 0))
      .set('size', String(filters.size ?? 20));
    if (filters.merchantId) params = params.set('merchantId', filters.merchantId);
    if (filters.status) params = params.set('status', filters.status);
    return this.http.get<{ content: PaymentIntent[]; totalElements: number }>(`${this.baseUrl}/api/v1/payments/intents`, { params });
  }

  authorizePayment(intentId: string) {
    return this.http.post<PaymentIntent>(`${this.baseUrl}/api/v1/payments/intents/${intentId}/authorize`, {});
  }

  getLedgerEntries(merchantId: string) {
    return this.http.get<LedgerPage>(`${this.baseUrl}/api/v1/ledger/entries`, {
      params: new HttpParams().set('merchantId', merchantId).set('page', '0').set('size', '20'),
    });
  }
}
