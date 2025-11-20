import { Component, OnInit } from '@angular/core';
import { Inventario } from '../../../../core/interfaces/interfaces';
import { catchError, Observable, of, switchMap } from 'rxjs';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { InventarioService } from '../../services/inventario.service';
import { CommonModule } from '@angular/common';
import { Auth } from '../../../../core/auth/services/auth';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-inventario-detalle',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './inventario-detalle.html',
  styleUrl: './inventario-detalle.scss',
})
export class InventarioDetalle implements OnInit{
  inventario$!: Observable<Inventario>;
  error: string | null = null;
  puedeAjustar = false;

  editandoUbicacion = false;
  nuevaUbicacion: string = '';

  constructor(
    private route: ActivatedRoute,
    private inventarioService: InventarioService,
    private router: Router,
    private authService: Auth
  ) {}

  ngOnInit(): void {

    const rol = this.authService.getRole();
    console.log(`${rol}`)
    this.puedeAjustar = rol === 'ROLE_ADMIN' || rol === 'ROLE_ALMACENERO';
    console.log(`${this.puedeAjustar}`);

    this.inventario$ = this.route.paramMap.pipe(
      switchMap(params => {
        const id = Number(params.get('id'));
        return this.inventarioService.getById(id);
      }),
      catchError(err => {
        this.error = 'No se pudo cargar los detalles del inventario';
        console.error(err);
        return of();
      })
    );
  }

  activarEdicionUbicacion(actual: string) {
    this.editandoUbicacion = true;
    this.nuevaUbicacion = actual || '';
  }

  cancelarEdicion() {
    this.editandoUbicacion = false;
  }

  guardarUbicacion(item: Inventario) {

    const confirmar = confirm(
      `¿Guardar nueva ubicación para "${item.nombreProducto}"?`
    );

    if (!confirmar) return;

    this.inventarioService
      .updateUbicacion(item.productoId, this.nuevaUbicacion)
      .subscribe({
        next: () => {
          alert('Ubicación actualizada');
          this.editandoUbicacion = false;
          // recargar vista
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            this.router.navigate(['inventario/detalle', item.id]);
          });
        },
        error: () => alert('No se pudo actualizar la ubicación.')
      });
  }

  ajustar(item: Inventario) {
    
    const rol = this.authService.getRole();
    const puede = rol === 'ROLE_ADMIN' || rol === 'ALMACENERO';

    if (!puede) {
      alert('No tiene permiso para ajustar el stock.');
      return;
    }
    if (!item || !item.id) {
      alert('Error: datos no válidos.');
      return;
    }
    const confirmar = confirm(`¿Desea ajustar el stock de "${item.nombreProducto}"?`);
    if (!confirmar) return;
    this.router.navigate(['inventario/ajuste', item.id]);
  }

  volver() {
    this.router.navigate(['inventario']);
  }
}
