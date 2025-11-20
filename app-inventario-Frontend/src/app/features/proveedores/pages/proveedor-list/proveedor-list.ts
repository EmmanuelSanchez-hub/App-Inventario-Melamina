import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { catchError, Observable, of, switchMap } from 'rxjs';
import { Proveedor } from '../../../../core/interfaces/interfaces';
import { ProveedorService } from '../../services/proveedor.service';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../core/services/api.service';

@Component({
  selector: 'app-proveedor-list',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './proveedor-list.html',
  styleUrl: './proveedor-list.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProveedorList {
  proveedores$!: Observable<Proveedor[]>;
  cargando = true;
  termino = '';
  error: string | null = null;

  constructor(private proveedorService: ProveedorService, public api: ApiService) { }

  ngOnInit(): void {
    this.cargar();
  }

  cargar() {
    this.cargando = true;

    this.proveedores$ = this.proveedorService.getProveedores().pipe(
      catchError(err => {
        this.error = 'Error al cargar proveedores:', err;
        return of([]);
      })
    );
  }

  buscar() {
    if (this.termino.trim().length === 0) {
      this.cargar();
      return;
    }

    this.error = null;

    this.proveedores$ = this.proveedorService.getByRazonSocial(this.termino).pipe(
      catchError(err => {
        this.error = 'No se encontraron resultados';
        return of([]);
      })
    );
  }

  eliminar(id: number) {
    if (!confirm('¿Desea desactivar este proveedor?')) return;

    this.proveedores$ = this.proveedorService.eliminar(id).pipe(
      switchMap(() => this.proveedorService.getProveedores()),
      catchError(() => {
        this.error = 'No se pudo eliminar el proveedor.';
        return of([]);
      })
    );
  }

}
