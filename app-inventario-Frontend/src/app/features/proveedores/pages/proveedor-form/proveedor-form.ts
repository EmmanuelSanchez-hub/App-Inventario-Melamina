import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProveedorService } from '../../services/proveedor.service';

@Component({
  selector: 'app-proveedor-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './proveedor-form.html',
  styleUrl: './proveedor-form.scss',
})
export class ProveedorForm {

  proveedorId!: number;
  editMode = false;

  ciudades = [
    'Chimbote',
    'Lima',
    'Arequipa',
    'Trujillo',
    'Cusco',
    'Piura',
    'Chiclayo',
    'Iquitos',
    'Huancayo',
    'Tacna'
  ];

  form = new FormGroup({
    razonSocial: new FormControl<string>('', {
      nonNullable: true,
      validators: Validators.required
    }),

    // VALIDACIÓN RUC CORREGIDA
    ruc: new FormControl<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^[0-9]{11}$/),  // Solo números y exactamente 11
        this.validarPrefijoRuc
      ]
    }),

    contacto: new FormControl<string>('', { nonNullable: true }),

    // TELÉFONO SOLO NUMEROS
    telefono: new FormControl<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^[0-9]{9}$/) // Solo 9 números
      ]
    }),

    // EMAIL SOLO FORMATO VALIDO
    email: new FormControl<string>('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.com$/)
      ]
    }),

    direccion: new FormControl<string>('', { nonNullable: true }),

    ciudad: new FormControl<string>('Chimbote', {
      nonNullable: true,
      validators: Validators.required
    }),

    activo: new FormControl<boolean>(true, { nonNullable: true })
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private proveedorService: ProveedorService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.editMode = true;
      this.cargarProveedor(Number(id));
    }
  }

  // VALIDADOR PERSONALIZADO DEL RUC (Prefijo SUNAT)
  validarPrefijoRuc(control: AbstractControl) {
    const value: string = control.value || '';

    if (value.length === 11) {
      const prefijo = value.substring(0, 2);

      const prefijosValidos = ['10', '15', '16', '17', '20'];

      if (!prefijosValidos.includes(prefijo)) {
        return { prefijoInvalido: true };
      }
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
      this.proveedorService.update(this.proveedorId, data).subscribe({
        next: () => this.router.navigate(['/proveedores']),
        error: err => console.error(err)
      });

    } else {
      this.proveedorService.create(data).subscribe({
        next: () => this.router.navigate(['/proveedores']),
        error: err => console.error(err)
      });
    }
  }
}
