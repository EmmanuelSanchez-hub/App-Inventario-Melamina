import { Component } from '@angular/core';
import { Auth } from '../../auth/services/auth';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  username: string = '';
  openCatalogo = false;
  openPersonas = false;
  openInventario = false;

  constructor(private authService: Auth, private router: Router, public api: ApiService) {
    const user = this.authService.getUser();
    this.username = user?.nombres || '';
  }

  toggleCatalogo() {
    this.openCatalogo = !this.openCatalogo;
    this.openPersonas = false;
    this.openInventario = false;
  }

  togglePersonas() {
    this.openPersonas = !this.openPersonas;
    this.openCatalogo = false;
    this.openInventario = false;
  }

  toggleInventario() {
    this.openInventario = !this.openInventario;
    this.openCatalogo = false;
    this.openPersonas = false;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
