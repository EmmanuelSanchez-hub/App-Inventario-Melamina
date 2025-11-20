import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductForm } from './pages/product-form/product-form';
import { ProductList } from './pages/product-list/product-list';

const routes: Routes = [
  { path: '', component: ProductList },
  { path: 'nuevo', component: ProductForm },
  { path: 'editar/:codigo', component: ProductForm }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductsRoutingModule { }
