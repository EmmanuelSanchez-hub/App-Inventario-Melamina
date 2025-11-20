import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InventarioRoutingModule } from './inventario-routing-module';
import { TarjetaInventario } from './components/tarjeta-inventario/tarjeta-inventario';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    InventarioRoutingModule,
    TarjetaInventario
  ]
})
export class InventarioModule { }
