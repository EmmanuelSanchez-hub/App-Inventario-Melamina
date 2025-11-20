-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 13-10-2025 a las 16:01:02
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cesarcort_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id`, `nombre`, `descripcion`, `activo`, `created_at`, `updated_at`) VALUES
(1, 'Tableros Melamina', 'Tableros de melamina de diferentes colores y texturas', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(2, 'Tableros MDF', 'Tableros de fibra de densidad media', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(3, 'Cantos', 'Cantos de melamina para acabados', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(4, 'Herrajes', 'Bisagras, correderas, tiradores y accesorios', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(5, 'Maderas Naturales', 'Maderas naturales diversas', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(6, 'Insumos', 'Pegamentos, tornillos y otros insumos', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(7, 'prueba de post', 'solo prueba', 1, '2025-10-13 04:15:35', '2025-10-13 04:15:35');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` bigint(20) NOT NULL,
  `tipo_documento` varchar(20) DEFAULT 'DNI' COMMENT 'DNI, RUC, CE',
  `numero_documento` varchar(20) NOT NULL,
  `nombres` varchar(100) DEFAULT NULL,
  `apellidos` varchar(100) DEFAULT NULL,
  `razon_social` varchar(255) DEFAULT NULL COMMENT 'Para clientes empresas',
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` text DEFAULT NULL,
  `ciudad` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `tipo_documento`, `numero_documento`, `nombres`, `apellidos`, `razon_social`, `telefono`, `email`, `direccion`, `ciudad`, `activo`, `created_at`, `updated_at`) VALUES
(1, 'DNI', '12345678', 'Pedro', 'Ramírez', NULL, '987654321', 'pedro.ramirez@email.com', 'Av. Principal 123', 'Lima', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(2, 'RUC', '20555666777', NULL, NULL, NULL, '014445566', 'ventas@construcasa.com', 'Jr. Construcción 456', 'Lima', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(3, 'DNI', '87654321', 'Laura', 'Flores', NULL, '998877665', 'laura.flores@email.com', 'Calle Las Rosas 789', 'Callao', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario`
--

CREATE TABLE `inventario` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `cantidad_actual` int(11) NOT NULL DEFAULT 0,
  `ubicacion` varchar(100) DEFAULT NULL COMMENT 'Ubicación física en almacén',
  `ultima_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `inventario`
--

INSERT INTO `inventario` (`id`, `producto_id`, `cantidad_actual`, `ubicacion`, `ultima_actualizacion`) VALUES
(1, 1, 25, 'Estante A1', '2025-10-12 23:50:53'),
(2, 2, 15, 'Estante A2', '2025-10-12 23:50:53'),
(3, 3, 12, 'Estante A3', '2025-10-12 23:50:53'),
(4, 4, 30, 'Estante B1', '2025-10-12 23:50:53'),
(5, 5, 150, 'Estante C1', '2025-10-12 23:50:53'),
(6, 6, 200, 'Estante D1', '2025-10-12 23:50:53'),
(7, 7, 80, 'Estante D2', '2025-10-12 23:50:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientos`
--

CREATE TABLE `movimientos` (
  `id` bigint(20) NOT NULL,
  `tipo_movimiento` enum('ENTRADA','SALIDA') NOT NULL,
  `fecha_movimiento` timestamp NOT NULL DEFAULT current_timestamp(),
  `producto_id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `motivo` varchar(255) DEFAULT NULL COMMENT 'Compra, Venta, Ajuste, Devolución, Merma',
  `proveedor_id` bigint(20) DEFAULT NULL COMMENT 'Para entradas',
  `cliente_id` bigint(20) DEFAULT NULL COMMENT 'Para salidas',
  `usuario_id` bigint(20) NOT NULL COMMENT 'Usuario que registró el movimiento',
  `observaciones` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Disparadores `movimientos`
--
DELIMITER $$
CREATE TRIGGER `trg_entrada_inventario` AFTER INSERT ON `movimientos` FOR EACH ROW BEGIN
    IF NEW.tipo_movimiento = 'ENTRADA' THEN
        -- Si existe el producto en inventario, actualizar
        IF EXISTS (SELECT 1 FROM inventario WHERE producto_id = NEW.producto_id) THEN
            UPDATE inventario 
            SET cantidad_actual = cantidad_actual + NEW.cantidad
            WHERE producto_id = NEW.producto_id;
        ELSE
            -- Si no existe, crear registro
            INSERT INTO inventario (producto_id, cantidad_actual)
            VALUES (NEW.producto_id, NEW.cantidad);
        END IF;
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_salida_inventario` AFTER INSERT ON `movimientos` FOR EACH ROW BEGIN
    IF NEW.tipo_movimiento = 'SALIDA' THEN
        UPDATE inventario 
        SET cantidad_actual = cantidad_actual - NEW.cantidad
        WHERE producto_id = NEW.producto_id;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL,
  `codigo` varchar(50) NOT NULL COMMENT 'Código interno del producto',
  `nombre` varchar(255) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `categoria_id` bigint(20) NOT NULL,
  `color` varchar(100) DEFAULT NULL COMMENT 'Ej: Blanco, Cerezo, Roble',
  `textura` varchar(100) DEFAULT NULL COMMENT 'Ej: Lisa, Rugosa, Veteada',
  `espesor` decimal(10,2) DEFAULT NULL COMMENT 'Espesor en mm',
  `largo` decimal(10,2) DEFAULT NULL COMMENT 'Largo en cm o metros',
  `ancho` decimal(10,2) DEFAULT NULL COMMENT 'Ancho en cm o metros',
  `unidad_medida` varchar(20) DEFAULT 'UNIDAD' COMMENT 'UNIDAD, M2, ML, KG',
  `precio_compra` decimal(10,2) NOT NULL DEFAULT 0.00,
  `precio_venta` decimal(10,2) NOT NULL DEFAULT 0.00,
  `stock_minimo` int(11) DEFAULT 5,
  `proveedor_id` bigint(20) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `codigo`, `nombre`, `descripcion`, `categoria_id`, `color`, `textura`, `espesor`, `largo`, `ancho`, `unidad_medida`, `precio_compra`, `precio_venta`, `stock_minimo`, `proveedor_id`, `activo`, `created_at`, `updated_at`) VALUES
(1, 'MEL-BLA-18-244-183', 'Tablero Melamina Blanco 18mm', 'Tablero de melamina color blanco 2.44x1.83m', 1, 'Blanco', 'Lisa', 18.00, 244.00, 183.00, 'UNIDAD', 85.00, 120.00, 10, 1, 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(2, 'MEL-CER-18-244-183', 'Tablero Melamina Cerezo 18mm', 'Tablero de melamina color cerezo 2.44x1.83m', 1, 'Cerezo', 'Veteada', 18.00, 244.00, 183.00, 'UNIDAD', 95.00, 135.00, 8, 1, 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(3, 'MEL-ROB-18-244-183', 'Tablero Melamina Roble 18mm', 'Tablero de melamina color roble 2.44x1.83m', 1, 'Roble', 'Veteada', 18.00, 244.00, 183.00, 'UNIDAD', 95.00, 135.00, 8, 1, 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(4, 'MDF-15-244-183', 'Tablero MDF 15mm', 'Tablero MDF crudo 2.44x1.83m', 2, 'Natural', 'Lisa', 15.00, 244.00, 183.00, 'UNIDAD', 55.00, 80.00, 15, 1, 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(5, 'CAN-BLA-22', 'Canto Blanco 22mm', 'Canto de melamina blanco 22mm x 50m', 3, 'Blanco', 'Lisa', 22.00, 5000.00, 2.20, 'ML', 0.80, 1.50, 50, 2, 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(6, 'HER-BIS-35', 'Bisagra Cazoleta 35mm', 'Bisagra de cazoleta para puertas', 4, NULL, NULL, NULL, NULL, NULL, 'UNIDAD', 2.50, 4.50, 100, 3, 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(7, 'HER-COR-45', 'Corredera Telescópica 45cm', 'Corredera telescópica para cajones', 4, NULL, NULL, NULL, NULL, NULL, 'PAR', 8.00, 15.00, 50, 3, 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedores`
--

CREATE TABLE `proveedores` (
  `id` bigint(20) NOT NULL,
  `razon_social` varchar(255) NOT NULL,
  `ruc` varchar(20) DEFAULT NULL,
  `contacto` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` text DEFAULT NULL,
  `ciudad` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `proveedores`
--

INSERT INTO `proveedores` (`id`, `razon_social`, `ruc`, `contacto`, `telefono`, `email`, `direccion`, `ciudad`, `activo`, `created_at`, `updated_at`) VALUES
(1, 'Tableros Peruanos SAC', '20123456789', 'Carlos Rojas', '014567890', 'ventas@tablerosperuanos.com', 'Av. Industrial 123', 'Lima', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(2, 'Melaminas del Norte EIRL', '20987654321', 'Ana Torres', '014561234', 'contacto@melaminasnorte.com', 'Jr. Comercio 456', 'Trujillo', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(3, 'Distribuidora Maderas SA', '20456789123', 'Jorge Mendoza', '014445566', 'info@dismaderas.com', 'Calle Los Pinos 789', 'Arequipa', 1, '2025-10-12 23:50:53', '2025-10-12 23:50:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(50) NOT NULL COMMENT 'ROLE_ADMIN, ROLE_VENDEDOR, ROLE_ALMACENERO',
  `descripcion` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`, `descripcion`, `created_at`, `updated_at`) VALUES
(1, 'ROLE_ADMIN', 'Administrador con acceso completo al sistema', '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(2, 'ROLE_VENDEDOR', 'Vendedor con acceso a ventas y consultas', '2025-10-12 23:50:53', '2025-10-12 23:50:53'),
(3, 'ROLE_ALMACENERO', 'Almacenero con acceso a inventario y movimientos', '2025-10-12 23:50:53', '2025-10-12 23:50:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL COMMENT 'Contraseña encriptada con BCrypt',
  `nombres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `rol_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `username`, `email`, `password`, `nombres`, `apellidos`, `telefono`, `activo`, `rol_id`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin@cesarcort.com', '$2a$10$QkHDebzfyhRzfWzNtqTTteQQ6sluK3h702EKFU7Utl.memZWvmSpS', 'Administrador', 'Sistema', '999888777', 1, 1, '2025-10-12 23:50:53', '2025-10-13 04:05:36'),
(2, 'vendedor1', 'vendedor@cesarcort.com', '$2a$10$IgxgXd9.nGSA0y/ppRYRg.5BgNuj8cz5xM/NB2eNBvNLyOlFkR/f2', 'Juan', 'Pérez', '987654321', 1, 2, '2025-10-12 23:50:53', '2025-10-13 04:05:36'),
(3, 'almacen1', 'almacen@cesarcort.com', '$2a$10$LlbmPr6yOPSWGtSFaO8xXuqKO9r0deMu.X6Tu15zvgUFlDY/EokHO', 'María', 'González', '987123456', 1, 3, '2025-10-12 23:50:53', '2025-10-13 04:05:36');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_movimientos_recientes`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_movimientos_recientes` (
`id` bigint(20)
,`tipo_movimiento` enum('ENTRADA','SALIDA')
,`fecha_movimiento` timestamp
,`producto_codigo` varchar(50)
,`producto_nombre` varchar(255)
,`cantidad` int(11)
,`precio_unitario` decimal(10,2)
,`total` decimal(10,2)
,`motivo` varchar(255)
,`usuario` varchar(50)
,`tercero` varchar(255)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_productos_stock`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_productos_stock` (
`id` bigint(20)
,`codigo` varchar(50)
,`nombre` varchar(255)
,`categoria` varchar(100)
,`color` varchar(100)
,`textura` varchar(100)
,`precio_venta` decimal(10,2)
,`stock_actual` int(11)
,`stock_minimo` int(11)
,`estado_stock` varchar(7)
,`ubicacion` varchar(100)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `v_movimientos_recientes`
--
DROP TABLE IF EXISTS `v_movimientos_recientes`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_movimientos_recientes`  AS SELECT `m`.`id` AS `id`, `m`.`tipo_movimiento` AS `tipo_movimiento`, `m`.`fecha_movimiento` AS `fecha_movimiento`, `p`.`codigo` AS `producto_codigo`, `p`.`nombre` AS `producto_nombre`, `m`.`cantidad` AS `cantidad`, `m`.`precio_unitario` AS `precio_unitario`, `m`.`total` AS `total`, `m`.`motivo` AS `motivo`, `u`.`username` AS `usuario`, coalesce(`pr`.`razon_social`,`c`.`razon_social`,concat(`c`.`nombres`,' ',`c`.`apellidos`)) AS `tercero` FROM ((((`movimientos` `m` join `productos` `p` on(`m`.`producto_id` = `p`.`id`)) join `usuarios` `u` on(`m`.`usuario_id` = `u`.`id`)) left join `proveedores` `pr` on(`m`.`proveedor_id` = `pr`.`id`)) left join `clientes` `c` on(`m`.`cliente_id` = `c`.`id`)) ORDER BY `m`.`fecha_movimiento` DESC ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_productos_stock`
--
DROP TABLE IF EXISTS `v_productos_stock`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_productos_stock`  AS SELECT `p`.`id` AS `id`, `p`.`codigo` AS `codigo`, `p`.`nombre` AS `nombre`, `c`.`nombre` AS `categoria`, `p`.`color` AS `color`, `p`.`textura` AS `textura`, `p`.`precio_venta` AS `precio_venta`, coalesce(`i`.`cantidad_actual`,0) AS `stock_actual`, `p`.`stock_minimo` AS `stock_minimo`, CASE WHEN coalesce(`i`.`cantidad_actual`,0) <= `p`.`stock_minimo` THEN 'CRÍTICO' WHEN coalesce(`i`.`cantidad_actual`,0) <= `p`.`stock_minimo` * 2 THEN 'BAJO' ELSE 'NORMAL' END AS `estado_stock`, `i`.`ubicacion` AS `ubicacion` FROM ((`productos` `p` left join `categorias` `c` on(`p`.`categoria_id` = `c`.`id`)) left join `inventario` `i` on(`p`.`id` = `i`.`producto_id`)) WHERE `p`.`activo` = 1 ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_documento` (`numero_documento`);

--
-- Indices de la tabla `inventario`
--
ALTER TABLE `inventario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `producto_id` (`producto_id`);

--
-- Indices de la tabla `movimientos`
--
ALTER TABLE `movimientos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `proveedor_id` (`proveedor_id`),
  ADD KEY `cliente_id` (`cliente_id`),
  ADD KEY `usuario_id` (`usuario_id`),
  ADD KEY `idx_movimientos_producto` (`producto_id`),
  ADD KEY `idx_movimientos_fecha` (`fecha_movimiento`),
  ADD KEY `idx_movimientos_tipo` (`tipo_movimiento`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codigo` (`codigo`),
  ADD KEY `idx_productos_categoria` (`categoria_id`),
  ADD KEY `idx_productos_proveedor` (`proveedor_id`),
  ADD KEY `idx_productos_codigo` (`codigo`);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ruc` (`ruc`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `rol_id` (`rol_id`),
  ADD KEY `idx_usuarios_username` (`username`),
  ADD KEY `idx_usuarios_email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `inventario`
--
ALTER TABLE `inventario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `movimientos`
--
ALTER TABLE `movimientos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `inventario`
--
ALTER TABLE `inventario`
  ADD CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `movimientos`
--
ALTER TABLE `movimientos`
  ADD CONSTRAINT `movimientos_ibfk_1` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `movimientos_ibfk_2` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`),
  ADD CONSTRAINT `movimientos_ibfk_3` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `movimientos_ibfk_4` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  ADD CONSTRAINT `productos_ibfk_2` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
