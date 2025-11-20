import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Categoria } from '../interfaces/interfaces';
import { Auth } from '../auth/services/auth';

type Role = 'ROLE_ADMIN' | 'ROLE_VENDEDOR' | 'ROLE_ALMACENERO';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private api = environment.apiBaseUrl;

  constructor(private http: HttpClient, private auth: Auth) { }

  private permissions: Record<Role, {

    // PRODUCTOS
    productos_read: boolean;
    productos_write: boolean;

    // CLIENTES
    clientes_read: boolean;
    clientes_write: boolean;

    // PROVEEDORES
    proveedores_read: boolean;
    proveedores_write: boolean;

    // INVENTARIO
    inventario_read: boolean;
    inventario_write: boolean;

    // MOVIMIENTOS
    movimientos_read: boolean;
    movimientos_write: boolean;

    // REPORTES
    ventas_read: boolean;
    ventas_write: boolean;

    // COMPRAS
    compras_read: boolean;
    compras_write: boolean;

    // AJUSTES
    ajustes_read: boolean;
    ajustes_write: boolean;

    // USUARIOS
    usuarios_read: boolean;
    usuarios_write: boolean;

  }> = {
      ROLE_ADMIN: {
        productos_read: true, productos_write: true,
        clientes_read: true, clientes_write: true,
        proveedores_read: true, proveedores_write: true,
        inventario_read: true, inventario_write: true,
        movimientos_read: true, movimientos_write: true,
        ventas_read: true, ventas_write: true,
        compras_read: true, compras_write: true,
        ajustes_read: true, ajustes_write: true,
        usuarios_read: true, usuarios_write: true,
      },

      ROLE_VENDEDOR: {
        productos_read: true, productos_write: false,
        clientes_read: true, clientes_write: false,
        proveedores_read: false, proveedores_write: false,
        inventario_read: false, inventario_write: false,
        movimientos_read: true, movimientos_write: true,
        ventas_read: true, ventas_write: true,
        compras_read: false, compras_write: false,
        ajustes_read: false, ajustes_write: false,
        usuarios_read: false, usuarios_write: false,
      },
      ROLE_ALMACENERO: {
        productos_read: true, productos_write: true,
        clientes_read: false, clientes_write: false,
        proveedores_read: true, proveedores_write: true,
        inventario_read: true, inventario_write: true,
        movimientos_read: true, movimientos_write: true,
        ventas_read: true, ventas_write: false,
        compras_read: true, compras_write: true,
        ajustes_read: true, ajustes_write: true,
        usuarios_read: false, usuarios_write: false,
      }
    };



  can(permission: keyof (typeof this.permissions)['ROLE_ADMIN']): boolean {
    const role = this.auth.getRole() as Role;
    return this.permissions[role]?.[permission] === true;
  }

  ping() {
    return this.http.get(`${this.api}/auth/test`).pipe(
      catchError(() => of(null))
    );
  }

  getCategorias(): Observable<Categoria[]> {
    if (!this.can('productos_read')) { return of([]); }
    return this.http.get<any[]>(`${this.api}/categorias`);
  }
}
