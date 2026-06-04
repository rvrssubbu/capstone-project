import 'zone.js';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import { ApiErrorInterceptor } from './app/shared/api-error.interceptor';
import { MerchantConsoleComponent } from './app/merchant-console/merchant-console.component';
import { PaymentsOpsComponent } from './app/payments-ops/payments-ops.component';

const routes: Routes = [
  { path: '', redirectTo: 'merchants', pathMatch: 'full' },
  { path: 'merchants', component: MerchantConsoleComponent },
  { path: 'payments', component: PaymentsOpsComponent },
];

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(HttpClientModule),
    provideAnimations(),
    provideRouter(routes),
    { provide: HTTP_INTERCEPTORS, useClass: ApiErrorInterceptor, multi: true },
  ],
}).catch((error) => console.error(error));
