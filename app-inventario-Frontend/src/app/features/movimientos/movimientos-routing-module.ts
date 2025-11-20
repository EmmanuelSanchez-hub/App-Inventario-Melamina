import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovList } from './pages/mov-list/mov-list';
import { MovEntrada } from './pages/mov-entrada/mov-entrada';
import { MovSalida } from './pages/mov-salida/mov-salida';
import { MovAjuste } from './pages/mov-ajuste/mov-ajuste';

const routes: Routes = [
  { path: '', component: MovList },
  { path: 'entrada', component: MovEntrada },
  { path: 'salida', component: MovSalida },
  { path: 'ajuste', component: MovAjuste },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MovimientosRoutingModule { }
