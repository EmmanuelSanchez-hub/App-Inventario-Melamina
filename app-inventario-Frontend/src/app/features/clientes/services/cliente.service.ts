import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Cliente } from '../../../core/interfaces/interfaces';
import { Observable, of } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private api = `${environment.apiBaseUrl}/clientes`;

  constructor(private http: HttpClient, private apiService: ApiService) { }

  obtenerTodos(): Observable<Cliente[]> {
    if (!this.apiService.can('clientes_read')) return of([]);
    return this.http.get<Cliente[]>(this.api);
  }

  obtenerActivos(): Observable<Cliente[]> {
    if (!this.apiService.can('clientes_read')) return of([]);
    return this.http.get<Cliente[]>(`${this.api}/activos`);
  }

  obtenerPorId(id: number): Observable<Cliente> {
    if (!this.apiService.can('clientes_read')) return of(null as any);
    return this.http.get<Cliente>(`${this.api}/${id}`);
  }

  buscarPorDocumento(documento: string): Observable<Cliente> {
    if (!this.apiService.can('clientes_read')) return of(null as any);
    return this.http.get<Cliente>(`${this.api}/documento/${documento}`);
  }

  crear(data: Partial<Cliente>): Observable<Cliente> {
    if (!this.apiService.can('clientes_write')) return of(null as any);
    return this.http.post<Cliente>(this.api, data);
  }

  actualizar(id: number, data: Partial<Cliente>): Observable<Cliente> {
    if (!this.apiService.can('clientes_write')) return of(null as any);
    return this.http.put<Cliente>(`${this.api}/${id}`, data);
  }

  eliminar(id: number): Observable<any> {
    if (!this.apiService.can('clientes_write')) return of(null);
    return this.http.delete(`${this.api}/${id}`);
  }
}
