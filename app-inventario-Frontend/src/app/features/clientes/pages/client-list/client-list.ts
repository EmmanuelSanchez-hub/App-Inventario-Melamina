import { Component } from '@angular/core';
import { catchError, Observable, of, switchMap } from 'rxjs';
import { Cliente } from '../../../../core/interfaces/interfaces';
import { ClienteService } from '../../services/cliente.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../core/services/api.service';

@Component({
  selector: 'app-client-list',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './client-list.html',
  styleUrl: './client-list.scss',
})
export class ClientList {

  clientes$!: Observable<Cliente[]>;
  cargando = true;
  termino = '';
  error: string | null = null;
  
  constructor(private clienteService: ClienteService, public api: ApiService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar() {
    this.cargando = true;
    this.error = null;

    this.clientes$ = this.clienteService.obtenerTodos().pipe(
      catchError(err => {
        console.error('Error al cargar clientes:', err);
        this.error = 'Error al cargar clientes';
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

    this.clientes$ = this.clienteService.buscarPorDocumento(this.termino).pipe(
      switchMap(cliente => of([cliente])),
      catchError(err => {
        console.error('Error en búsqueda:', err);
        this.error = 'No se encontraron resultados';
        return of([]);
      })
    );
  }

  eliminar(id: number) {
    if (!confirm('¿Desea desactivar este cliente?')) return;

    this.error = null;

    this.clientes$ = this.clienteService.eliminar(id).pipe(
      switchMap(() => this.clienteService.obtenerTodos()),
      catchError(err => {
        console.error('Error al eliminar cliente:', err);
        this.error = 'No se pudo eliminar el cliente';
        return of([]);
      })
    );
  }
}