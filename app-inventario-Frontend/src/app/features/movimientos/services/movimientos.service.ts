import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Movimiento } from '../../../core/interfaces/interfaces';
import { Observable, of } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';

@Injectable({
  providedIn: 'root',
})
export class MovimientosService {
  
  private api = `${environment.apiBaseUrl}/movimientos`;

  constructor(private http: HttpClient, private apiService: ApiService) {}

  getAll(): Observable<Movimiento[]> {
    if (!this.apiService.can('movimientos_read')) return of([]);
    return this.http.get<Movimiento[]>(this.api);
  }

  getById(id: number): Observable<Movimiento> {
    if (!this.apiService.can('movimientos_read')) return of(null as any);
    return this.http.get<Movimiento>(`${this.api}/${id}`);
  }

  getByTipo(tipo: 'ENTRADA' | 'SALIDA' | 'AJUSTE'): Observable<Movimiento[]> {
    if (!this.apiService.can('movimientos_read')) return of([]);
    return this.http.get<Movimiento[]>(`${this.api}/tipo/${tipo}`);
  }

  getByProducto(productoId: number): Observable<Movimiento[]> {
    if (!this.apiService.can('movimientos_read')) return of([]);
    return this.http.get<Movimiento[]>(`${this.api}/producto/${productoId}`);
  }

  getByRango(fechaInicio: string, fechaFin: string): Observable<Movimiento[]> {
    if (!this.apiService.can('movimientos_read')) return of([]);
    return this.http.get<Movimiento[]>(
      `${this.api}/rango?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`
    );
  }

  registrarEntrada(data: Partial<Movimiento>): Observable<Movimiento> {
    if (!this.apiService.can('compras_write')) return of(null as any);
    return this.http.post<Movimiento>(`${this.api}/entrada`, data);
  }

  registrarSalida(data: Partial<Movimiento>): Observable<Movimiento> {
    if (!this.apiService.can('ventas_write')) return of(null as any);
    return this.http.post<Movimiento>(`${this.api}/salida`, data);
  }

  registrarAjuste(data: Partial<Movimiento>): Observable<Movimiento> {
    if (!this.apiService.can('ajustes_write')) return of(null as any);
    return this.http.post<Movimiento>(`${this.api}/ajuste`, data);
  }

  reporteVentas(fechaInicio: string, fechaFin: string): Observable<any> {
    if (!this.apiService.can('ventas_read')) return of(null);
    return this.http.get(
      `${this.api}/reportes/ventas?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`
    );
  }

  reporteCompras(fechaInicio: string, fechaFin: string): Observable<any> {
    if (!this.apiService.can('compras_read')) return of(null);
    return this.http.get(
      `${this.api}/reportes/compras?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`
    );
  }
}
