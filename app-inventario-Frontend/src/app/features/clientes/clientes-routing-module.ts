import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientList } from './pages/client-list/client-list';
import { ClientForm } from './pages/client-form/client-form';

const routes: Routes = [
  {path:'', component: ClientList},
  {path:'nuevo', component: ClientForm},
  {path:'editar/:id', component: ClientForm}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientesRoutingModule { }
