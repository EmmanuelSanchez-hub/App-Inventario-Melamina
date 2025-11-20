export interface Categoria {
  id: number;
  nombre: string;
  descripcion: string;
  activo: boolean;
  cantidadProductos: number;
  createdAt: string;
  updatedAt: string;
}

export interface Cliente {
  id: number;
  tipoDocumento: string;
  numeroDocumento: string;
  nombres: string;
  apellidos: string;
  razonSocial: string;
  telefono: string;
  email: string;
  direccion: string;
  ciudad: string;
  activo: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Estadisticas {
  totalProductos: number;
  productosActivos: number;
  productosSinStock: number;
  productosStockBajo: number;
  valorTotalInventario: number;
  totalVentasMes: number;
  totalComprasMes: number;
  totalClientes: number;
  totalProveedores: number;
  totalMovimientosMes: number;
}

export interface Inventario {
  id: number;
  productoId: number;
  codigoProducto: string;
  nombreProducto: string;
  categoria: string;
  cantidadActual: number;
  stockMinimo: number;
  ubicacion: string;
  estadoStock: 'NORMAL' | 'BAJO' | 'CRÍTICO' | 'SIN_STOCK';
  valorUnitario: number;
  valorTotal: number;
  ultimaActualizacion: string;
}

export interface Login {
  token: string;
  tipo: 'Bearer';
  id: number;
  username: string;
  email: string;
  nombres: string;
  apellidos: string;
  rol: string;
}

export interface Mensaje {
  mensaje: string;
}

export interface Movimiento {
  id: number;
  tipoMovimiento: string;
  fechaMovimiento: string;
  productoId: number;
  codigoProducto: string;
  nombreProducto: string;
  cantidad: number;
  precioUnitario: number;
  total: number;
  motivo: string;
  proveedor: string;
  cliente: string;
  usuario: string;
  observaciones: string;
  createdAt: string;
}

export interface Producto {
  id: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  categoria: string;
  categoriaId: number | null;
  color: string;
  textura: string;
  espesor: number;
  largo: number;
  ancho: number;
  unidadMedida: string;
  precioCompra: number;
  precioVenta: number;
  stockMinimo: number;
  stockActual: number | null;
  proveedor: string;
  proveedorId: number;
  activo: boolean;
  estadoStock: 'NORMAL' | 'BAJO' | 'SIN_STOCK';
  createdAt: string;
  updatedAt: string;
}

export interface Proveedor {
  id: number;
  razonSocial: string;
  ruc: string;
  contacto: string;
  telefono: string;
  email: string;
  direccion: string;
  ciudad: string;
  activo: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface StockAlerta {
  productoId: number;
  codigo: string;
  nombre: string;
  stockActual: number;
  stockMinimo: number;
  nivelAlerta: 'CRÍTICO' | 'BAJO';
  categoria: string;
}

export interface Usuario {
  id: number;
  username: string;
  email: string;
  nombres: string;
  apellidos: string;
  telefono: string;
  activo: boolean;
  rol: string;
  createdAt: string;
  updatedAt: string;
}