import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Producto } from '../../../../core/interfaces/interfaces';
import { Product } from '../../services/product.service';
import { CommonModule } from '@angular/common';
import { Observable, switchMap } from 'rxjs';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../../../core/services/api.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductList implements OnInit {
  productos$!: Observable<Producto[]>;
  cargando = true;
  error: string | null = null;

  constructor(private productService: Product, public api:ApiService) { }

  ngOnInit(): void {
    this.productos$ = this.productService.getAll();
  }



  eliminarProducto(id: number): void {
    if (!confirm('¿Está seguro de eliminar este producto?')) return;

    this.productos$ = this.productService.delete(id).pipe(
      switchMap(() => this.productService.getAll())
    );
  }



}
