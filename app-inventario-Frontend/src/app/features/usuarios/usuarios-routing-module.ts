import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserList } from './pages/user-list/user-list';
import { UserForm } from './pages/user-form/user-form';

const routes: Routes = [
  {path: '', component: UserList},
  {path: 'nuevo', component: UserForm},
  {path: 'editar/:id', component: UserForm}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuariosRoutingModule { }
