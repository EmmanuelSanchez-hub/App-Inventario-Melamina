import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Inventario } from '../../../core/interfaces/interfaces';
import { Observable, of } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';

@Injectable({
  providedIn: 'root',
})
export class InventarioService {
  private api = `${environment.apiBaseUrl}/inventario`;

  constructor(private http: HttpClient, private apiService: ApiService) {}
  
  getAll(): Observable<Inventario[]> {
    if (!this.apiService.can('inventario_read')) return of([]);
    return this.http.get<Inventario[]>(this.api);
  }

  getById(id: number): Observable<Inventario> {
    if (!this.apiService.can('inventario_read')) return of(null as any);
    return this.http.get<Inventario>(`${this.api}/${id}`);
  }

  getByProducto(productoId: number): Observable<Inventario> {
    if (!this.apiService.can('inventario_read')) return of(null as any);
    return this.http.get<Inventario>(`${this.api}/producto/${productoId}`);
  }

  getConStock(): Observable<Inventario[]> {
    if (!this.apiService.can('inventario_read')) return of([]);
    return this.http.get<Inventario[]>(`${this.api}/con-stock`);
  }

  getSinStock(): Observable<Inventario[]> {
    if (!this.apiService.can('inventario_read')) return of([]);
    return this.http.get<Inventario[]>(`${this.api}/sin-stock`);
  }

  getValorTotal(): Observable<{ valorTotal: number }> {
    if (!this.apiService.can('inventario_read')) return of({ valorTotal: 0 });
    return this.http.get<{ valorTotal: number }>(`${this.api}/valor-total`);
  }

  getStock(productoId: number): Observable<{ productoId: number; stockActual: number }> {
    if (!this.apiService.can('inventario_read')) 
      return of({ productoId, stockActual: 0 });
    return this.http.get<{ productoId: number; stockActual: number }>(
      `${this.api}/stock/${productoId}`
    );
  }

  updateUbicacion(productoId: number, ubicacion: string): Observable<Inventario> {
    if (!this.apiService.can('inventario_write')) return of(null as any);
    return this.http.patch<Inventario>(
      `${this.api}/producto/${productoId}/ubicacion`,
      { ubicacion }
    );
  }

  ajustarStock(productoId: number, cantidad: number): Observable<any> {
    if (!this.apiService.can('ajustes_write')) return of(null);
    return this.http.patch<any>(`${this.api}/producto/${productoId}/ajustar`, { cantidad });
  }
}
