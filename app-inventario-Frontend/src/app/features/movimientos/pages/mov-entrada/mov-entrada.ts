import { Component, OnInit } from '@angular/core';
import { MovimientosService } from '../../services/movimientos.service';
import { Producto, Proveedor } from '../../../../core/interfaces/interfaces';
import { ProveedorService } from '../../../proveedores/services/proveedor.service';
import { Router, RouterModule } from '@angular/router';
import { Product } from '../../../products/services/product.service';
import { debounceTime, Observable, switchMap } from 'rxjs';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../../../core/services/api.service';

@Component({
  selector: 'app-mov-entrada',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './mov-entrada.html',
  styleUrl: './mov-entrada.scss',
})
export class MovEntrada implements OnInit {
  productos$!: Observable<Producto[]>;
  proveedores$!: Observable<Proveedor[]>;

  form = new FormGroup({
    productoId: new FormControl<number>(0, {
      nonNullable: true,
      validators: [Validators.required],
    }),

    proveedorId: new FormControl<number>(0, {
      nonNullable: true,
      validators: [Validators.required],
    }),

    cantidad: new FormControl<number>(1, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(1)],
    }),

    precioUnitario: new FormControl<number>(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0.01)],
    }),

    observaciones: new FormControl<string>('', {
      nonNullable: true,
    }),
  });


  buscarProducto = new FormControl('');
  buscarProveedor = new FormControl('');
  
  productos: Producto[] = [];
  proveedores: Proveedor[] = [];

  constructor(
    private productosService: Product,
    private proveedorService: ProveedorService,
    private movimientosService: MovimientosService,
    private router: Router,
    public api: ApiService
  ) { }

  ngOnInit(): void {

    this.buscarProducto.valueChanges
      .pipe(
        debounceTime(300),
        switchMap(txt => this.productosService.buscar(txt || ''))
      )
      .subscribe(res => this.productos = res);

    this.buscarProveedor.valueChanges
      .pipe(
        debounceTime(300),
        switchMap(txt => this.proveedorService.getByRazonSocial(txt || ''))
      )
      .subscribe(res => this.proveedores = res);
  }

  seleccionarProducto(p: Producto) {
    this.form.get('productoId')?.setValue(p.id);
    this.buscarProducto.setValue(`${p.codigo} - ${p.nombre}`, { emitEvent: false });
    this.productos = [];
  }
  seleccionarProveedor(p: Proveedor) {
    this.form.get('proveedorId')?.setValue(p.id);
    this.buscarProveedor.setValue(p.razonSocial, { emitEvent: false });
    this.proveedores = [];
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = {
      tipoMovimiento: 'ENTRADA',
      ...this.form.value
    };

    this.movimientosService.registrarEntrada(payload)
      .subscribe(() => this.router.navigate(['/movimientos']));
  }

}
