import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `
    <nav class="shell-nav">
      <strong>PayPilot</strong>
      <a routerLink="/merchants">Merchants</a>
      <a routerLink="/payments">Payments</a>
    </nav>
    <router-outlet></router-outlet>
  `,
  styles: [`
    .shell-nav {
      display: flex;
      gap: 18px;
      align-items: center;
      min-height: 56px;
      padding: 0 24px;
      border-bottom: 1px solid #d9dee7;
      background: #ffffff;
    }

    a {
      color: #126b5f;
      text-decoration: none;
    }
  `],
})
export class AppComponent {}
