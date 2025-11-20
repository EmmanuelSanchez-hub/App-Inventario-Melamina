import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Inventario } from '../../../../core/interfaces/interfaces';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tarjeta-inventario',
  imports: [CommonModule],
  templateUrl: './tarjeta-inventario.html',
  styleUrl: './tarjeta-inventario.scss',
})
export class TarjetaInventario implements OnInit {
  @Input() item!: Inventario;

  @Output() verDetalle = new EventEmitter<number>();
  @Output() ajustar = new EventEmitter<number>();
  
  puedeAjustar = false;

  ngOnInit() {
    const rol = localStorage.getItem('rol');

    this.puedeAjustar =
      rol === 'ADMIN' ||
      rol === 'ALMACENERO';
  }

  estadoClase(estado: string): string {
    switch (estado) {
      case 'NORMAL':
        return 'normal';
      case 'BAJO':
        return 'bajo';
      case 'CRÍTICO':
        return 'critico';
      case 'SIN_STOCK':
        return 'sin-stock';
      default:
        return '';
    }
  }
}
