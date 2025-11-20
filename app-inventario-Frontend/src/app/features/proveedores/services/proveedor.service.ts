import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable, of } from 'rxjs';
import { Proveedor } from '../../../core/interfaces/interfaces';
import { ApiService } from '../../../core/services/api.service';

@Injectable({
  providedIn: 'root',
})
export class ProveedorService {

  private api = `${environment.apiBaseUrl}/proveedores`;
  
  constructor(private http: HttpClient, private apiService:ApiService ){}

  getProveedores(): Observable<Proveedor[]>{
    if (!this.apiService.can('proveedores_read')) return of([]);
    return this.http.get<Proveedor[]>(`${this.api}`)
  }

  getActivos(): Observable<Proveedor[]> {
    if (!this.apiService.can('proveedores_read')) return of([]);
    return this.http.get<Proveedor[]>(`${this.api}/activos`);
  }

  getByRazonSocial(razon: string): Observable<Proveedor[]>{
    if (!this.apiService.can('proveedores_read')) return of([]);
    return this.http.get<Proveedor[]>(`${this.api}/buscar?razonSocial=${razon}`)
  }

  getById(id: number): Observable<Proveedor>{
    if (!this.apiService.can('proveedores_read')) return of(null as any);
    return this.http.get<Proveedor>(`${this.api}/${id}`);
  }
  create(data: Partial<Proveedor>): Observable<Proveedor>{
    if (!this.apiService.can('proveedores_write')) return of(null as any);
    return this.http.post<Proveedor>(`${this.api}`,data);
  }

  update(id: number, data: Partial<Proveedor>): Observable<Proveedor>{
    if (!this.apiService.can('proveedores_write')) return of(null as any);
    return this.http.put<Proveedor>(`${this.api}/${id}`, data);
  }

  eliminar(id: number): Observable<Proveedor> {
    if (!this.apiService.can('proveedores_write')) return of(null as any);
    return this.http.delete<Proveedor>(`${this.api}/${id}`);
  }
}
