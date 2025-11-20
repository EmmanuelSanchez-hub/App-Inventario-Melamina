import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { MainLayout } from './core/layout/main-layout/main-layout';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, MainLayout],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {

  private router = inject(Router);
  isLoginPage = signal(false);

  constructor() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.isLoginPage.set(event.urlAfterRedirects === '/login');
      }
    });
  }

  protected readonly title = signal('app-inventario');
}
