import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Cliente, Producto } from '../../../../core/interfaces/interfaces';
import { MovimientosService } from '../../services/movimientos.service';
import { Product } from '../../../products/services/product.service';
import { ClienteService } from '../../../clientes/services/cliente.service';
import { debounceTime, switchMap } from 'rxjs';
import { ApiService } from '../../../../core/services/api.service';

@Component({
  selector: 'app-mov-salida',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './mov-salida.html',
  styleUrl: './mov-salida.scss',
})
export class MovSalida implements OnInit {
  buscarCliente = new FormControl('');
  buscarProducto = new FormControl('');

  clientes: Cliente[] = [];
  productos: Producto[] = [];

  clienteSeleccionado: Cliente | null = null;

  carrito: {
    productoId: number;
    nombre: string;
    precioUnitario: number;
    cantidad: number;
    subtotal: number;
  }[] = [];

  form = new FormGroup({
    pago: new FormControl<number>(0, { nonNullable: true, validators: [Validators.min(0)] }),
    observaciones: new FormControl<string>('', { nonNullable: true }),
  });

  totalVenta = 0;
  vuelto = 0;

  constructor(
    private clientesService: ClienteService,
    private productosService: Product,
    private movService: MovimientosService,
    private router: Router,
    public api: ApiService
  ) { }

  ngOnInit(): void {

    this.buscarCliente.valueChanges
      .pipe(
        debounceTime(300),
        switchMap(txt => this.clientesService.buscarPorDocumento(txt || ''))
      )
      .subscribe(cliente => {
        this.clientes = cliente ? [cliente] : [];
      });

    this.buscarProducto.valueChanges
      .pipe(
        debounceTime(300),
        switchMap(txt => this.productosService.buscar(txt || ''))
      )
      .subscribe(res => this.productos = res);

    this.form.get('pago')?.valueChanges.subscribe(valor => {
      this.vuelto = valor - this.totalVenta;
    });
  }

  seleccionarCliente(c: Cliente) {
    this.clienteSeleccionado = c;
    this.buscarCliente.setValue(`${c.nombres} ${c.apellidos}`, { emitEvent: false });
    this.clientes = [];
  }

  seleccionarProducto(p: Producto) {
    this.buscarProducto.setValue('', { emitEvent: false });
    this.productos = [];

    const item = {
      productoId: p.id,
      nombre: p.nombre,
      precioUnitario: p.precioVenta,
      cantidad: 1,
      subtotal: p.precioVenta
    };

    this.carrito.push(item);
    this.recalcularTotal();
  }

  cambiarCantidad(item: any, cantidad: number) {
    item.cantidad = cantidad;
    item.subtotal = item.precioUnitario * cantidad;
    this.recalcularTotal();
  }

  eliminarItem(i: number) {
    this.carrito.splice(i, 1);
    this.recalcularTotal();
  }

  recalcularTotal() {
    this.totalVenta = this.carrito.reduce((acc, item) => acc + item.subtotal, 0);
    const pago = this.form.get('pago')?.value || 0;
    this.vuelto = pago - this.totalVenta;
  }

  guardar() {
    if (!this.clienteSeleccionado) return;
    if (this.carrito.length === 0) return;

    const detalle = this.carrito.map(item => ({
      tipoMovimiento: 'SALIDA',
      productoId: item.productoId,
      cantidad: item.cantidad,
      precioUnitario: item.precioUnitario,
      clienteId: this.clienteSeleccionado!.id,
      observaciones: this.form.value.observaciones
    }));

    detalle.forEach(item => {
      this.movService.registrarSalida(item).subscribe();
    });

    this.router.navigate(['/movimientos']);
  }
}
