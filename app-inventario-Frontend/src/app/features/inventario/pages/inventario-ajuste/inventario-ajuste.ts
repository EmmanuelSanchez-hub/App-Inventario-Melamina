import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Inventario } from '../../../../core/interfaces/interfaces';
import { catchError, of, switchMap } from 'rxjs';
import { InventarioService } from '../../services/inventario.service';
import { Auth } from '../../../../core/auth/services/auth';

@Component({
  selector: 'app-inventario-ajuste',
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './inventario-ajuste.html',
  styleUrl: './inventario-ajuste.scss',
})
export class InventarioAjuste implements OnInit {
  form!: FormGroup;
  inventario!: Inventario;
  cargando = true;
  error: string | null = null;
  mensaje: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private auth: Auth,
    private inventarioService: InventarioService,
    private router: Router
  ) {}

  ngOnInit(): void {

    const rol = this.auth.getRole();
    const puede = rol === 'ROLE_ADMIN' || rol === 'ROLE_ALMACENERO';

    if (!puede) {
      this.error = 'No tiene permiso para ajustar el stock.';
      this.cargando = false;
      return;
    }
    
    this.route.paramMap.pipe(
      switchMap(params => {
        const productoId = Number(params.get('id'));
        if (!productoId) {
          this.error = 'Producto no válido.';
          this.cargando = false;
          return of(null);
        }
        return this.inventarioService.getByProducto(productoId);
      }),
      catchError(err => {
        console.error(err);
        this.error = 'No se pudo cargar la información de inventario.';
        this.cargando = false;
        return of(null);
      })
    ).subscribe(inv => {
      if (!inv) return;

      this.inventario = inv;
      this.cargando = false;

      this.form = new FormGroup({
        cantidad: new FormControl<number>(
          this.inventario.cantidadActual,
          { nonNullable: true, validators: [Validators.required, Validators.min(0)] }
        ),
      });
    });
  }

  guardar() {
    if (!this.form || this.form.invalid || !this.inventario) {
      this.form.markAllAsTouched();
      return;
    }

    const nuevaCantidad = this.form.get('cantidad')!.value;

    const confirmar = confirm(
      `¿Confirmar ajuste de stock para "${this.inventario.nombreProducto}" a ${nuevaCantidad} unidades?`
    );

    if (!confirmar) return;

    this.error = null;
    this.mensaje = null;

    this.inventarioService.ajustarStock(this.inventario.productoId, nuevaCantidad).subscribe({
      next: () => {
        this.mensaje = 'Stock ajustado correctamente.';
        this.router.navigate(['inventario/detalle', this.inventario.id]);
      },
      error: (err: unknown) => {
        console.error(err);
        this.error = 'No se pudo ajustar el stock.';
      }
    });
  }

  cancelar() {
    this.router.navigate(['inventario']);
  }
}
