import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Producto } from '../../../core/interfaces/interfaces';
import { Observable, of } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';

@Injectable({
  providedIn: 'root',
})
export class Product {

  private api = `${environment.apiBaseUrl}/productos`;

  constructor(private http: HttpClient, private apiService: ApiService) { }

  getAll(): Observable<Producto[]> {
    if (!this.apiService.can('productos_read')) return of([]);
    return this.http.get<Producto[]>(`${this.api}`);
  }

  getActivos(): Observable<Producto[]> {
    if (!this.apiService.can('productos_read')) return of([]);
    return this.http.get<Producto[]>(`${this.api}/activos`);
  }

  buscar(nombre: string): Observable<Producto[]> {
    if (!this.apiService.can('productos_read')) return of([]);
    return this.http.get<Producto[]>(`${this.api}/buscar?nombre=${nombre}`);
  }

  getByCodigo(codigo: string): Observable<Producto> {
    if (!this.apiService.can('productos_read')) return of(null as any);
    return this.http.get<Producto>(`${this.api}/codigo/${codigo}`);
  }

  create(data: Partial<Producto>): Observable<Producto> {
    if (!this.apiService.can('productos_write')) return of(null as any);
    return this.http.post<Producto>(`${this.api}`, data);
  }

  update(id: number, data: Partial<Producto>): Observable<Producto> {
    if (!this.apiService.can('productos_write')) return of(null as any);
    return this.http.put<Producto>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<Product> {
    if (!this.apiService.can('productos_write')) return of(null as any);
    return this.http.delete<Product>(`${this.api}/${id}`);
  }

}
