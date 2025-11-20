import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { catchError, Observable, of, switchMap } from 'rxjs';
import { Usuario } from '../../../../core/interfaces/interfaces';
import { UserService } from '../../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-list',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './user-list.html',
  styleUrl: './user-list.scss',
})
export class UserList {
  usuarios$!: Observable<Usuario[]>;
  cargando = true;
  termino = '';
  error: string | null = null;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargar();
  }
  
  cargar() {
    this.cargando = true;
    this.error = null;

    this.usuarios$ = this.userService.getAll().pipe(
      catchError(err => {
        console.error('Error al cargar usuarios:', err);
        this.error = 'Error al cargar usuarios';
        return of([]);
      })
    );
  }

  buscar() {
    if (this.termino.trim().length === 0) {
      this.cargar();
      return;
    }

    this.error = null;

    this.usuarios$ = this.userService.getByUsername(this.termino).pipe(
      switchMap(usuario => of([usuario])),
      catchError(err => {
        console.error('Error en búsqueda:', err);
        this.error = 'No se encontraron usuarios con ese username';
        return of([]);
      })
    );
  }

  desactivar(id: number) {
    if (!confirm('¿Desea desactivar este usuario?')) return;

    this.error = null;

    this.usuarios$ = this.userService.deactivate(id).pipe(
      switchMap(() => this.userService.getAll()),
      catchError(err => {
        console.error('Error al desactivar usuario:', err);
        this.error = 'No se pudo desactivar el usuario';
        return of([]);
      })
    );
  }

  activar(id: number) {
    this.error = null;

    this.usuarios$ = this.userService.activate(id).pipe(
      switchMap(() => this.userService.getAll()),
      catchError(err => {
        console.error('Error al activar usuario:', err);
        this.error = 'No se pudo activar el usuario';
        return of([]);
      })
    );
  }
}
