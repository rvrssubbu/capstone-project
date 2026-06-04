import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../shared/api.service';
import { LedgerEntry, PaymentIntent } from '../shared/models';

@Component({
  selector: 'app-payments-ops',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <main class="page">
      <div class="toolbar">
        <div>
          <h1>Payments Ops</h1>
          <p>Search, inspect timeline, and retry authorization</p>
        </div>

      </div>

      <section class="panel">
        <form class="grid" [formGroup]="filters" (ngSubmit)="search()">
          <div class="field">
            <label>Merchant ID</label>
            <input formControlName="merchantId" placeholder="merchant UUID">
          </div>
          <div class="field">
            <label>Status</label>
            <select formControlName="status">
              <option value="">All</option>
              <option value="CREATED">Created</option>
              <option value="AUTHORIZED">Authorized</option>
              <option value="FAILED">Failed</option>
            </select>
          </div>
          <div class="actions">
            <button class="primary" type="submit">Search</button>
          </div>
        </form>
        <p class="error" *ngIf="error">{{ error }}</p>
      </section>

      <section class="panel">
        <table class="table">
          <thead>
            <tr>
              <th>Intent</th>
              <th>Merchant</th>
              <th>Amount</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let payment of payments" (click)="select(payment)">
              <td>{{ payment.intentId }}</td>
              <td>{{ payment.merchantId }}</td>
              <td>{{ payment.amount }} {{ payment.currency }}</td>
              <td><span class="badge" [ngClass]="badgeClass(payment.status)">{{ payment.status }}</span></td>
              <td>
                <button class="secondary" type="button" *ngIf="payment.status === 'FAILED'" (click)="retry(payment); $event.stopPropagation()">Retry</button>
              </td>
            </tr>
            <tr *ngIf="payments.length === 0">
              <td colspan="5">No payment intents match the current filters.</td>
            </tr>
          </tbody>
        </table>
      </section>

      <section class="panel" *ngIf="selected">
        <h2>Timeline</h2>
        <p><strong>{{ selected.intentId }}</strong> · {{ selected.status }}</p>
        <ol>
          <li>Intent created for {{ selected.amount }} {{ selected.currency }}</li>
          <li *ngFor="let entry of ledgerEntries">
            {{ entry.direction }} posted at {{ entry.entryTime }} for {{ entry.amount }} {{ entry.currency }}
          </li>
        </ol>
      </section>
    </main>
  `,
})
export class PaymentsOpsComponent implements OnInit {
  payments: PaymentIntent[] = [];
  ledgerEntries: LedgerEntry[] = [];
  selected?: PaymentIntent;
  error = '';

  filters = this.fb.nonNullable.group({
    merchantId: [''],
    status: [''],
  });

  constructor(private readonly fb: FormBuilder, private readonly api: ApiService) {}

  ngOnInit() {
    this.search();
  }

  search() {
    const filters = this.filters.getRawValue();
    this.error = '';
    this.api.searchPayments({
      merchantId: filters.merchantId || undefined,
      status: filters.status || undefined,
    }).subscribe({
      next: (page) => this.payments = page.content,
      error: (error: Error) => this.error = error.message,
    });
  }

  select(payment: PaymentIntent) {
    this.selected = payment;
    this.api.getLedgerEntries(payment.merchantId).subscribe({
      next: (page) => this.ledgerEntries = page.content.filter((entry) => entry.intentId === payment.intentId),
      error: (error: Error) => this.error = error.message,
    });
  }

  retry(payment: PaymentIntent) {
    this.api.authorizePayment(payment.intentId).subscribe({
      next: (updated) => {
        this.payments = this.payments.map((item) => item.intentId === updated.intentId ? updated : item);
        this.select(updated);
      },
      error: (error: Error) => this.error = error.message,
    });
  }

  badgeClass(status: PaymentIntent['status']) {
    return {
      ok: status === 'AUTHORIZED',
      warn: status === 'CREATED',
      bad: status === 'FAILED',
    };
  }
}
