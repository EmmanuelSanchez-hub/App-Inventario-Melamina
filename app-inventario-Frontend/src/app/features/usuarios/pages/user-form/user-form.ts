import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-form',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './user-form.html',
  styleUrl: './user-form.scss',
})
export class UserForm {
  usuarioId!: number;

  editMode = false;
  roles = [
    { value: 'ROLE_ADMIN', label: 'Administrador' },
    { value: 'ROLE_VENDEDOR', label: 'Vendedor' },
    { value: 'ROLE_ALMACENERO', label: 'Almacenero' },
  ];

  form = new FormGroup({
    username: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
    email: new FormControl<string>('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    nombres: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
    apellidos: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
    telefono: new FormControl<string>('', { nonNullable: true }),

    rol: new FormControl<string>('ROLE_VENDEDOR', {
      nonNullable: true,
      validators: Validators.required,
    }),

    activo: new FormControl<boolean>(true, { nonNullable: true }),
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.editMode = true;
      this.usuarioId = Number(id);
      this.cargarUsuario(this.usuarioId);
    }
  }

  cargarUsuario(id: number) {
    this.userService.getById(id).subscribe({
      next: (usuario) => {
        this.form.patchValue(usuario);
      },
      error: (err) => console.error('Error cargando usuario:', err),
    });
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const data = this.form.value;

    if (this.editMode) {
      this.userService.update(this.usuarioId, data).subscribe({
        next: () => this.router.navigate(['/usuarios']),
        error: (err) => console.error('Error al actualizar usuario:', err)
      });
    } else {
      this.userService.create(data).subscribe({
        next: () => this.router.navigate(['/usuarios']),
        error: (err) => console.error('Error al crear usuario:', err)
      });
    }
  }
}