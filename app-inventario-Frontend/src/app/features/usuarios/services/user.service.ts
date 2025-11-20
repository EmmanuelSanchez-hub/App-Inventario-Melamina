import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Usuario } from '../../../core/interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private api = `${environment.apiBaseUrl}/usuarios`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.api);
  }

  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.api}/${id}`);
  }

  getByUsername(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.api}/username/${username}`);
  }

  create(data: Partial<Usuario>): Observable<Usuario> {
    return this.http.post<Usuario>(this.api, data);
  }

  update(id: number, data: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.api}/${id}`, data);
  }

  changeRole(id: number, rol: string): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.api}/${id}/rol`, rol);
  }

  changePassword(id: number, nuevaPassword: string) {
    return this.http.patch(`${this.api}/${id}/password`, {
      nuevaPassword
    });
  }

  deactivate(id: number) {
    return this.http.patch(`${this.api}/${id}/desactivar`, {});
  }

  activate(id: number) {
    return this.http.patch(`${this.api}/${id}/activar`, {});
  }
}
