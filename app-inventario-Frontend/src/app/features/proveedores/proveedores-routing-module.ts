import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProveedorList } from './pages/proveedor-list/proveedor-list';
import { ProveedorForm } from './pages/proveedor-form/proveedor-form';

const routes: Routes = [
  { path: '', component: ProveedorList },
  { path: 'nuevo', component: ProveedorForm },
  { path: 'editar/:id', component: ProveedorForm }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProveedoresRoutingModule { }
