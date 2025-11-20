import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error-conexion',
  imports: [],
  templateUrl: './error-conexion.html',
  styleUrl: './error-conexion.scss',
})
export class ErrorConexion {
  intentando = false;
  mensaje = '';

  constructor(
    private pingService: ApiService,
    private router: Router
  ) {}

  recargar() {
    this.intentando = true;
    this.mensaje = 'Intentando reconectar...';

    this.pingService.ping().subscribe(resp => {

      this.intentando = false;

      if (resp) {
        this.mensaje = 'Conexión restablecida. Redirigiendo...';
        
        const lastUrl = localStorage.getItem('last-url') || '/dashboard';

        setTimeout(() => {
          this.router.navigate([lastUrl]);
        }, 1000);

      } else {
        this.mensaje = 'No se pudo reconectar. Intenta más tarde.';
      }
    });
  }
}
