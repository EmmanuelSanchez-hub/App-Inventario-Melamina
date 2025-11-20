import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProveedorService } from '../../services/proveedor.service';

@Component({
  selector: 'app-proveedor-form',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './proveedor-form.html',
  styleUrl: './proveedor-form.scss',
})
export class ProveedorForm {

  proveedorId!: number;
  editMode = false;

  form = new FormGroup({
    razonSocial: new FormControl<string>('', {
      nonNullable: true,
      validators: Validators.required
    }),

    ruc: new FormControl<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        this.validarLongitudRuc,
        this.validarPrefijoRuc
      ]
    }),

    contacto: new FormControl<string>('', { nonNullable: true }),

    telefono: new FormControl<string>('', { 
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^[0-9]{9}$/)
      ]
    }),

    email: new FormControl<string>('', {
      nonNullable: true,
      validators: Validators.email
    }),

    direccion: new FormControl<string>('', { nonNullable: true }),

    ciudad: new FormControl<string>('', { nonNullable: true }),

    activo: new FormControl<boolean>(true, { nonNullable: true })
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private proveedorService: ProveedorService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.editMode = true;
      this.cargarProveedor(Number(id));
    }
  }

  validarLongitudRuc(control: AbstractControl) {
    const value = control.value || '';
    // Solo números y 11 dígitos
    return /^[0-9]{11}$/.test(value) ? null : { longitudInvalida: true };
  }

  validarPrefijoRuc(control: AbstractControl) {
    
    const value = control.value || '';

    // Solo revisar si empieza con prefijo válido
    if (value.length === 11 && !/^(10|15|16|17|20)/.test(value)) {
      return { prefijoInvalido: true };
    }
    return null;
  }


  cargarProveedor(id: number) {
    this.proveedorService.getById(id).subscribe({
      next: (proveedor) => {
        this.form.patchValue(proveedor);
        this.proveedorId = proveedor.id!;
      },
      error: err => console.error(err)
    });
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const data = this.form.value;

    if (this.editMode) {
      // EDITAR
      this.proveedorService.update(this.proveedorId, data).subscribe({
        next: () => this.router.navigate(['/proveedores']),
        error: err => console.error(err)
      });

    } else {
      // CREAR
      this.proveedorService.create(data).subscribe({
        next: () => this.router.navigate(['/proveedores']),
        error: err => console.error(err)
      });
    }
  }

}
