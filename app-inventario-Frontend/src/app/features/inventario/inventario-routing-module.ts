import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InventarioList } from './pages/inventario-list/inventario-list';
import { InventarioDetalle } from './pages/inventario-detalle/inventario-detalle';
import { InventarioAjuste } from './pages/inventario-ajuste/inventario-ajuste';

const routes: Routes = [
  {path: '', component: InventarioList},
  { path: 'detalle/:id', component: InventarioDetalle },
  { path: 'ajuste/:id', component: InventarioAjuste }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InventarioRoutingModule { }
