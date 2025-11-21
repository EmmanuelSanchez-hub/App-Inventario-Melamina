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

  // FORMULARIO FLEXIBLE PARA EVITAR ERRORES TS2769
  form: FormGroup = new FormGroup<any>({
    username: new FormControl<string>('', {
      nonNullable: true,
      validators: Validators.required,
    }),

    email: new FormControl<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^[\w.-]+@[\w.-]+\.com$/)
      ],
    }),

    nombres: new FormControl<string>('', {
      nonNullable: true,
      validators: Validators.required,
    }),

    apellidos: new FormControl<string>('', {
      nonNullable: true,
      validators: Validators.required,
    }),

    telefono: new FormControl<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^[0-9]{9}$/),
      ],
    }),

    // password solo se usa en creación
    password: new FormControl<string>('', {
      nonNullable: true,
      validators: Validators.required,
    }),

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

      // En modo edición NO se usa password
      this.form.removeControl('password');
    }

    // Sanitizador SOLO NÚMEROS en teléfono
    this.form.controls['telefono'].valueChanges.subscribe(value => {
      if (value) {
        const soloNumeros = value.replace(/\D/g, '');
        if (soloNumeros !== value) {
          this.form.controls['telefono'].setValue(soloNumeros, { emitEvent: false });
        }
      }
    });
  }

  cargarUsuario(id: number) {
    this.userService.getById(id).subscribe({
      next: (usuario) => this.form.patchValue(usuario),
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
        error: (err) => console.error('Error al actualizar usuario:', err),
      });
    } else {
      // crear usuario → incluye password
      this.userService.create(data).subscribe({
        next: () => this.router.navigate(['/usuarios']),
        error: (err) => console.error('Error al crear usuario:', err),
      });
    }
  }
}
