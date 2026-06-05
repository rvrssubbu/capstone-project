import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../shared/api.service';
import { Merchant } from '../shared/models';

@Component({
  selector: 'app-merchant-console',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <main class="page">
      <div class="toolbar">
        <div>

          <h1>Merchant Onboarding and KYC operations</h1>
        </div>

      </div>

      <section class="panel">
        <form class="grid" [formGroup]="merchantForm" (ngSubmit)="createMerchant()">
          <div class="field">
            <label>Name</label>
            <input formControlName="name" placeholder="Merchant Name">
          </div>
          <div class="field">
            <label>Category</label>
            <select formControlName="category">
              <option value="RETAIL">Retail</option>
              <option value="FOOD_DELIVERY">Food delivery</option>
              <option value="TRAVEL">Travel</option>
              <option value="HEALTH">Health</option>
              <option value="EDUCATION">Education</option>
            </select>
          </div>
          <div class="field">
            <label>Email</label>
            <input formControlName="email" placeholder="ops@example.com">
          </div>
          <div class="field">
            <label>Phone</label>
            <input formControlName="phone" placeholder="+91 9876543210">

            <!-- Validation error message -->
            <div style="color:red" *ngIf="merchantForm.get('phone')?.touched && merchantForm.get('phone')?.invalid">
              Phone number is required and must start with +91 followed by 10 digits
            </div>
          </div>
          <div class="actions">
            <button class="primary" type="submit" [disabled]="merchantForm.invalid || loading">Create</button>
          </div>
        </form>
        <p class="error" *ngIf="error">{{ error }}</p>
      </section>

      <section class="panel">
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Category</th>
              <th>KYC</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let merchant of merchants">
              <td>{{ merchant.merchantId }}</td>
              <td>{{ merchant.name }}</td>
              <td>{{ merchant.category }}</td>
              <td><span class="badge" [ngClass]="badgeClass(merchant.kycStatus)">{{ merchant.kycStatus }}</span></td>
              <td>
                <select #status [disabled]="merchant.kycStatus === 'VERIFIED'">
                  <option value="VERIFIED">Verify</option>
                  <option value="REJECTED">Reject</option>
                  <option value="PENDING">Pending</option>
                </select>
                <input type="text" #reasonInput placeholder="Enter reason"  [disabled]="merchant.kycStatus === 'VERIFIED'">

                <button class="secondary" type="button" (click)="updateKyc(merchant, status.value,reasonInput.value)" [disabled]="merchant.kycStatus === 'VERIFIED'">Apply</button>
              </td>
            </tr>
            <tr *ngIf="!loading && merchants.length === 0">
              <td colspan="5">No merchants yet.</td>
            </tr>
          </tbody>
        </table>
      </section>
    </main>
  `,
})
export class MerchantConsoleComponent implements OnInit {
  merchants: Merchant[] = [];
  loading = false;
  error = '';

 merchantForm = this.fb.nonNullable.group({
   name: ['', Validators.required],
   category: ['RETAIL', Validators.required],
   email: ['', [Validators.required, Validators.email]],
   phone: [
     '',
     [
       Validators.required,
       Validators.pattern(/^\+91\d{10}$/)
     ]
   ],
 });

  constructor(private readonly fb: FormBuilder, private readonly api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.error = '';
    this.api.listMerchants().subscribe({
      next: (merchants) => {
        this.merchants = merchants;
        this.loading = false;
      },
      error: (error: Error) => {
        this.error = error.message;
        this.loading = false;
      },
    });
  }

  createMerchant() {
    if (this.merchantForm.invalid) return;
    this.api.createMerchant(this.merchantForm.getRawValue()).subscribe({
      next: () => {
        this.merchantForm.reset({ name: '', category: 'RETAIL', email: '', phone: '' });
        this.load();
      },
      error: (error: Error) => this.error = error.message,
    });
  }

  updateKyc(merchant: Merchant, status: string,reason:string) {
    this.api.updateKyc(merchant.merchantId, status, reason).subscribe({
      next: () => this.load(),
      error: (error: Error) => this.error = error.message,
    });
  }

  badgeClass(status: Merchant['kycStatus']) {
    return {
      ok: status === 'VERIFIED',
      warn: status === 'PENDING',
      bad: status === 'REJECTED',
    };
  }
}
