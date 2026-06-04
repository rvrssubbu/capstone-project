export interface Merchant {
  merchantId: string;
  name: string;
  category: string;
  email: string;
  phone?: string;
  kycStatus: 'PENDING' | 'VERIFIED' | 'REJECTED';
}

export interface PaymentIntent {
  intentId: string;
  merchantId: string;
  amount: number;
  currency: string;
  referenceId?: string;
  status: 'CREATED' | 'AUTHORIZED' | 'FAILED';
  createdAt: string;
}

export interface LedgerEntry {
  entryId: string;
  merchantId: string;
  intentId: string;
  amount: number;
  currency: string;
  direction: 'CREDIT' | 'DEBIT';
  entryTime: string;
}

export interface LedgerPage {
  content: LedgerEntry[];
  page: number;
  size: number;
  totalElements: number;
}
