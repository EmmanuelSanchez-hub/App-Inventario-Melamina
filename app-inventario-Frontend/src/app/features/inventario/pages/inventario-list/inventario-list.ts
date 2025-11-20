import { Component, OnInit } from '@angular/core';
import { catchError, Observable, of, switchMap } from 'rxjs';
import { InventarioService } from '../../services/inventario.service';
import { Router, RouterModule } from '@angular/router';
import { Inventario } from '../../../../core/interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { TarjetaInventario } from '../../components/tarjeta-inventario/tarjeta-inventario';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../../core/services/api.service';

@Component({
  selector: 'app-inventario-list',
  imports: [CommonModule, TarjetaInventario, FormsModule, RouterModule],
  templateUrl: './inventario-list.html',
  styleUrl: './inventario-list.scss',
})
export class InventarioList implements OnInit {
  inventario$!: Observable<Inventario[]>;
  cargando = true;
  termino = '';
  error: string | null = null;

  // Filtro
  filtro: 'todos' | 'con-stock' | 'sin-stock' | 'crítico' | 'bajo' = 'todos';

  mostrarFiltros = false;

  constructor(
    private inventarioService: InventarioService,
    private router: Router,
    public api:ApiService
  ) {}

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.cargando = true;
    this.error = null;

    this.inventario$ = this.inventarioService.getAll().pipe(
      catchError(err => {
        console.error('Error al cargar inventario:', err);
        this.error = 'Error al cargar inventario';
        return of([]);
      })
    );
  }

  buscar() {
    if (this.termino.trim().length === 0) {
      this.cargar();
      return;
    }

    const txt = this.termino.toLowerCase();

    this.error = null;

    this.inventario$ = this.inventarioService.getAll().pipe(
      switchMap(lista => {
        const filtrado = lista.filter(
          i =>
            i.nombreProducto.toLowerCase().includes(txt) ||
            i.codigoProducto.toLowerCase().includes(txt)
        );
        return of(filtrado);
      }),
      catchError(err => {
        console.error('Error en búsqueda:', err);
        this.error = 'No se encontraron resultados';
        return of([]);
      })
    );
  }

  setFiltro(tipo: 'todos' | 'con-stock' | 'sin-stock' | 'crítico'| 'bajo') {
    this.filtro = tipo;

    this.error = null;

    this.inventario$ = this.inventarioService.getAll().pipe(
      switchMap(lista => {
        let result = lista;

        if (tipo === 'con-stock') {
          result = lista.filter(i => i.estadoStock === 'NORMAL');
        }

        if (tipo === 'sin-stock') {
          result = lista.filter(i => i.estadoStock === 'SIN_STOCK');
        }

        if(tipo === 'crítico'){
          result = lista.filter(i => i.estadoStock === 'CRÍTICO')
        }

        if(tipo === 'bajo'){
          result = lista.filter(i => i.estadoStock === 'BAJO')
        }

        return of(result);
      }),
      catchError(err => {
        console.error('Error en filtrado:', err);
        this.error = 'No se pudo filtrar';
        return of([]);
      })
    );
  }

  toggleFiltros() {
    this.mostrarFiltros = !this.mostrarFiltros;
  }

  irDetalle(id: number) {
    this.router.navigate(['inventario/detalle', id]);
  }

  irAjuste(id: number) {
    this.router.navigate(['inventario/ajuste', id]);
  }
}
