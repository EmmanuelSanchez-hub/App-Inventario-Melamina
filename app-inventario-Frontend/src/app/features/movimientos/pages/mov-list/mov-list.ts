import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MovimientosService } from '../../services/movimientos.service';
import { Observable } from 'rxjs';
import { Movimiento } from '../../../../core/interfaces/interfaces';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../../../core/services/api.service';

@Component({
  selector: 'app-mov-list',
  imports: [CommonModule, RouterModule],
  templateUrl: './mov-list.html',
  styleUrl: './mov-list.scss',
})
export class MovList {
  movimientos$!: Observable<Movimiento[]>;

  constructor(private movService: MovimientosService, public api: ApiService) {}

  ngOnInit(): void {
    this.movimientos$ = this.movService.getAll();
  }
  convertirTipo(tipo: string): string {
    switch (tipo) {
      case 'ENTRADA':
        return 'Compra';
      case 'SALIDA':
        return 'Venta';
      case 'AJUSTE':
        return 'Ajuste';
      default:
        return tipo;
    }
  }
  getTipoMovimiento(tipo: string): string {
    switch (tipo) {
      case 'ENTRADA': return 'entrada';
      case 'SALIDA': return 'salida';
      case 'AJUSTE': return 'ajuste';
      default: return '';
    }
  }
}
