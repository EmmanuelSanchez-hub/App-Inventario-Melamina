import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-contactar',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './contactar.html',
  styleUrl: './contactar.scss',
})
export class Contactar implements OnInit {
  form = new FormGroup({
    nombre: new FormControl<string>('', { 
      nonNullable: true, 
      validators: Validators.required 
    }),
    correo: new FormControl<string>('', { 
      nonNullable: true, 
      validators: [Validators.required, Validators.email] 
    }),
    telefono: new FormControl<string>('', {
      nonNullable: true 
    }),
    mensaje: new FormControl<string>('', { 
      nonNullable: true, 
      validators: Validators.required 
    })
  });

  constructor() {}

  ngOnInit(): void {}

  enviar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const data = this.form.value;
    
    console.log('CONTACTO ENVIADO:', data);

    alert('Mensaje enviado correctamente. Nos comunicaremos contigo pronto.');

    this.form.reset();
  }
}
