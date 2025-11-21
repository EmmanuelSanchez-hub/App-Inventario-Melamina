import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Product } from '../../services/product.service';
import { ApiService } from '../../../../core/services/api.service';
import { Observable } from 'rxjs';
import { Categoria, Proveedor } from '../../../../core/interfaces/interfaces';
import { ProveedorService } from '../../../proveedores/services/proveedor.service';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './product-form.html',
  styleUrls: ['./product-form.scss'],
})
export class ProductForm implements OnInit {

  productoId!: number;
  editMode = false;

  categorias: Categoria[] = [];
  categoriasCargadas = false;

  categorias$!: Observable<Categoria[]>;
  proveedores$!: Observable<Proveedor[]>;

  form = new FormGroup({
    codigo: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
    nombre: new FormControl<string>('', { nonNullable: true, validators: Validators.required }),
    descripcion: new FormControl<string>('', { nonNullable: true }),

    categoriaId: new FormControl<number | null>(null, { validators: Validators.required }),
    proveedorId: new FormControl<number>(0, { nonNullable: true }),

    color: new FormControl<string>('', { nonNullable: true }),
    textura: new FormControl<string>('', { nonNullable: true }),

    espesor: new FormControl<number>(0, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^(0|[1-9][0-9]*)$/),
        Validators.min(1)
      ]
    }),

    largo: new FormControl<number>(0, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^(0|[1-9][0-9]*)$/),
        Validators.min(1)
      ]
    }),
    
    ancho: new FormControl<number>(0, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/^(0|[1-9][0-9]*)$/),
        Validators.min(1)
      ]
    }),

    unidadMedida: new FormControl<string>('UNIDAD', { nonNullable: true }),

    precioCompra: new FormControl<number>(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0.01)],
    }),
    precioVenta: new FormControl<number>(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0.01)],
    }),

    stockMinimo: new FormControl<number>(5, { nonNullable: true }),
    activo: new FormControl<boolean>(true, { nonNullable: true }),
  });   // ← ← ← ESTA LLAVE FALTABA


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: Product,
    private proveedorService: ProveedorService,
    private apiService: ApiService
  ) {
    this.apiService.getCategorias().subscribe((cats: Categoria[]) => {
      this.categorias = cats;
      this.categoriasCargadas = true;
    });
  }

  ngOnInit(): void {
    this.categorias$ = this.apiService.getCategorias();
    this.proveedores$ = this.proveedorService.getProveedores();

    const codigo = this.route.snapshot.paramMap.get('codigo');

    this.form.get('categoriaId')?.valueChanges.subscribe(() => this.onCodeFieldChange());
    this.form.get('color')?.valueChanges.subscribe(() => this.onCodeFieldChange());
    this.form.get('espesor')?.valueChanges.subscribe(() => this.onCodeFieldChange());
    this.form.get('largo')?.valueChanges.subscribe(() => this.onCodeFieldChange());
    this.form.get('ancho')?.valueChanges.subscribe(() => this.onCodeFieldChange());

    if (codigo) {
      this.editMode = true;
      this.cargarProducto(codigo);
    }
  }

  cargarProducto(codigo: string) {
    this.productService.getByCodigo(codigo).subscribe({
      next: (producto) => {
        this.form.patchValue(producto);
        this.productoId = producto.id;
      },
      error: (err) => console.error(err),
    });
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const data = this.form.value;

    if (this.editMode) {
      this.productService.update(this.productoId, data).subscribe({
        next: () => this.router.navigate(['/products']),
        error: (err) => console.error(err),
      });
    } else {
      this.productService.create(data).subscribe({
        next: () => this.router.navigate(['/products']),
        error: (err) => console.error(err),
      });
    }
  }

  private onCodeFieldChange() {
    if (!this.categoriasCargadas) return;
    this.generarCodigo();
  }

  private generarCodigo() {
    if (!this.categorias || this.categorias.length === 0) return;

    const categoriaId = this.form.get('categoriaId')?.value;
    const color = this.form.get('color')?.value || '';
    const espesor = this.form.get('espesor')?.value || 0;
    const largo = this.form.get('largo')?.value;
    const ancho = this.form.get('ancho')?.value;

    const cat = this.categorias.find(c => c.id === categoriaId);
    if (!cat) return;

    const palabras = cat.nombre.trim().split(' ');
    const omitidas = ['de', 'del', 'la', 'las', 'el', 'los', 'para', 'en', 'y'];
    const utiles = palabras.filter(w => !omitidas.includes(w.toLowerCase()));
    const palabraCodigo = utiles.length > 1 ? utiles[utiles.length - 1] : utiles[0];

    const codCategoria = palabraCodigo.substring(0, 3).toUpperCase();
    const codColor = color.substring(0, 3).toUpperCase();
    const codEspesor = Number(espesor).toString();

    const tieneLargo = largo && Number(largo) > 0;
    const tieneAncho = ancho && Number(ancho) > 0;

    let codigoFinal = `${codCategoria}-${codColor}-${codEspesor}`;

    if (tieneLargo && tieneAncho) {
      codigoFinal += `-${Number(largo)}-${Number(ancho)}`;
    }

    this.form.get('codigo')?.setValue(codigoFinal, { emitEvent: false });
  }
}
