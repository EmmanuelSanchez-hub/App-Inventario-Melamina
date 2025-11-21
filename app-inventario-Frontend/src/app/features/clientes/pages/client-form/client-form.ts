import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ClienteService } from '../../services/cliente.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client-form',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './client-form.html',
  styleUrl: './client-form.scss',
})
export class ClientForm {
  clienteId!: number;
  editMode = false;

  form = new FormGroup({
  tipoDocumento: new FormControl<string>('DNI', {
    nonNullable: true,
    validators: Validators.required
  }),

  numeroDocumento: new FormControl<string>('', {
    nonNullable: true,
    validators: [
      Validators.required,
      this.validarDocumentoPorTipo
    ]
  }),

  nombres: new FormControl<string>('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.minLength(2)
    ]
  }),

  apellidos: new FormControl<string>('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.minLength(2)
    ]
  }),

  razonSocial: new FormControl<string>('', {
    nonNullable: true,
    validators: this.validarRazonSocialRuc
  }),

  telefono: new FormControl<string>('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.pattern(/^[0-9]{9}$/)
    ]
  }),

  email: new FormControl<string>('', {
    nonNullable: true,
    validators: [
      Validators.required,
      Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.com$/)
    ]
  }),


  direccion: new FormControl<string>('', { nonNullable: true }),

  // 👉 Aquí el valor por defecto
  ciudad: new FormControl<string>('Chimbote', { nonNullable: true }),

  activo: new FormControl<boolean>(true, { nonNullable: true })
});


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clienteService: ClienteService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.editMode = true;
      this.cargarCliente(Number(id));
    }

    // Forzar revalidación cuando cambie el tipo de documento
    this.form.get('tipoDocumento')?.valueChanges.subscribe(() => {
      this.form.get('numeroDocumento')?.updateValueAndValidity();
      this.form.get('razonSocial')?.updateValueAndValidity();
    });
  }

  // Valida DNI / CE / RUC según tipoDocumento
  validarDocumentoPorTipo(control: AbstractControl) {
    const value: string = (control.value || '').toString().trim();
    const tipo = control.parent?.get('tipoDocumento')?.value;

    if (!tipo) return null;
    if (!value) return { requerido: true };

    // DNI → 8 dígitos
    if (tipo === 'DNI') {
      return /^[0-9]{8}$/.test(value) ? null : { dniInvalido: true };
    }

    // CE → 8 a 12 dígitos
    if (tipo === 'CE') {
      return /^[0-9]{8,12}$/.test(value) ? null : { ceInvalido: true };
    }

    // RUC → 11 dígitos
    if (tipo === 'RUC') {
      if (!/^[0-9]{11}$/.test(value)) {
        return { rucLongitudInvalida: true };
      }
      if (!/^(10|15|16|17|20)/.test(value)) {
        return { rucPrefijoInvalido: true };
      }
      return null;
    }

    return null;
  }

  

  // Razón social obligatoria solo si tipoDocumento = RUC
  validarRazonSocialRuc(control: AbstractControl) {
    const parent = control.parent;
    if (!parent) return null;

    const tipo = parent.get('tipoDocumento')?.value;
    const value = (control.value || '').toString().trim();

    if (tipo === 'RUC') {
      return value.length >= 3 ? null : { razonSocialRequerida: true };
    }

    return null;
  }

  cargarCliente(id: number) {
    this.clienteService.obtenerPorId(id).subscribe({
      next: (cliente) => {
        this.form.patchValue(cliente);
        this.clienteId = cliente.id!;
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
      this.clienteService.actualizar(this.clienteId, data).subscribe({
        next: () => this.router.navigate(['/clientes']),
        error: err => console.error(err)
      });
    } else {
      this.clienteService.crear(data).subscribe({
        next: () => this.router.navigate(['/clientes']),
        error: err => console.error(err)
      });
    }
  }
}
