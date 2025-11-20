import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { catchError, forkJoin, map, Observable, of, tap } from 'rxjs';
import { Cliente, Producto, Proveedor } from '../../../core/interfaces/interfaces';
import { ApiService } from '../../../core/services/api.service';

export interface KpiData {
  valorInventarioTotal: number;
  productosStockBajo: number;
  productosSinStock: number;
  clientesActivos: number;
  proveedoresActivos: number;
  ventasPeriodo: number;
}


@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private api = environment.apiBaseUrl;

  constructor(private http: HttpClient, private apiService: ApiService) {}

  getValorInventarioTotal(): Observable<number> {
    if (!this.apiService.can('inventario_read')) return of(0);

    return this.http
      .get<{ valorTotal: number }>(`${this.api}/inventario/valor-total`)
      .pipe(
        map(res => res.valorTotal ?? 0),
        catchError(() => of(0))
      );
  }

  getAllProductos(): Observable<Producto[]> {
    if (!this.apiService.can('productos_read')) return of([]);

    return this.http
      .get<Producto[]>(`${this.api}/productos`)
      .pipe(catchError(() => of([])));
  }

  getProductosStockBajo(): Observable<Producto[]> {
    if (!this.apiService.can('productos_read')) return of([]);

    return this.getAllProductos().pipe(
      map(productos => productos.filter(p => p.estadoStock === 'BAJO' || 'CRÍTICO'))
    );
  }

  getProductosSinStock(): Observable<Producto[]> {
    if (!this.apiService.can('productos_read')) return of([]);

    return this.getAllProductos().pipe(
      map(productos => productos.filter(p => p.estadoStock === 'SIN_STOCK'))
    );
  }

  getClientesActivos(): Observable<Cliente[]> {
    if (!this.apiService.can('clientes_read')) return of([]);

    return this.http
      .get<Cliente[]>(`${this.api}/clientes/activos`)
      .pipe(catchError(() => of([])));
  }

  getProveedoresActivos(): Observable<Proveedor[]> {
    if (!this.apiService.can('proveedores_read')) return of([]);

    return this.http
      .get<Proveedor[]>(`${this.api}/proveedores/activos`)
      .pipe(catchError(() => of([])));
  }

  getVentasPeriodo(fechaInicio: string, fechaFin: string): Observable<number> {
    if (!this.apiService.can('ventas_read')) return of(0);

    return this.http
      .get<{ totalVentas: number }>(`${this.api}/movimientos/reportes/ventas`, {
        params: { fechaInicio, fechaFin },
      })
      .pipe(
        map(res => res.totalVentas ?? 0),
        catchError(() => of(0))
      );
  }

  getVentasHistoricas(
    fechaInicio: string,
    fechaFin: string
  ): Observable<{ fechas: string[]; valores: number[] }> {

    if (!this.apiService.can('ventas_read')) {
      return of({ fechas: [], valores: [] });
    }

    const inicio = new Date(fechaInicio);
    const fin = new Date(fechaFin);

    const llamadas: Observable<number>[] = [];
    const fechas: string[] = [];

    for (let d = new Date(inicio); d <= fin; d.setDate(d.getDate() + 1)) {
      const diaInicio = new Date(d);
      diaInicio.setHours(0, 0, 0, 0);

      const diaFin = new Date(d);
      diaFin.setHours(23, 59, 59, 999);

      const inicioIso = diaInicio.toISOString();
      const finIso = diaFin.toISOString();

      fechas.push(inicioIso.split('T')[0]);

      llamadas.push(
        this.getVentasPeriodo(inicioIso, finIso)
      );
    }

    return forkJoin(llamadas).pipe(
      map(valores => ({ fechas, valores })),
      catchError(() => of({ fechas: [], valores: [] }))
    );
  }

  loadKpis(fechaInicio: string, fechaFin: string): Observable<KpiData> {
    return forkJoin({
      valorInventarioTotal: this.getValorInventarioTotal(),
      productosStockBajo: this.getProductosStockBajo().pipe(map(r => r.length)),
      productosSinStock: this.getProductosSinStock().pipe(map(r => r.length)),
      clientesActivos: this.getClientesActivos().pipe(map(r => r.length)),
      proveedoresActivos: this.getProveedoresActivos().pipe(map(r => r.length)),
      ventasPeriodo: this.getVentasPeriodo(fechaInicio, fechaFin),
    }).pipe(
      tap(k => console.log('KPIs recibidos:', k))
    );
  }
}
