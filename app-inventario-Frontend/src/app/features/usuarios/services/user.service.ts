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
  private apiAuth = `${environment.apiBaseUrl}/auth`;

  constructor(private http: HttpClient) { }

  // Obtener todos
  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.api);
  }

  // Obtener por ID
  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.api}/${id}`);
  }

  // Obtener por username
  getByUsername(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.api}/username/${username}`);
  }

  // Registrar usuario (CORREGIDO)
  create(data: Partial<Usuario>): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiAuth}/registro`, data);
  }

  // Actualizar usuario
  update(id: number, data: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.api}/${id}`, data);
  }

  // Cambiar rol
  changeRole(id: number, rol: string): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.api}/${id}/rol`, rol);
  }

  // Cambiar contraseña
  changePassword(id: number, nuevaPassword: string) {
    return this.http.patch(`${this.api}/${id}/password`, { nuevaPassword });
  }

  // Desactivar
  deactivate(id: number) {
    return this.http.patch(`${this.api}/${id}/desactivar`, {});
  }

  // Activar
  activate(id: number) {
    return this.http.patch(`${this.api}/${id}/activar`, {});
  }
}
