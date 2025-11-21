import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Landing } from './pages/landing/landing';
import { Contactar } from './pages/contactar/contactar';

const routes: Routes = [
  { path: '', component: Landing },
  { path: 'contactar', component: Contactar }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LandingRoutingModule { }
