-- ============================================
-- SCRIPT DE DATOS PARA BD COMERCIALIZADORA
-- ============================================
-- Orden de inserción: electrodomestico -> usuario -> factura -> detalle_factura

-- ============================================
-- LIMPIEZA DE DATOS EXISTENTES (opcional)
-- ============================================
-- DELETE FROM detalle_factura;
-- DELETE FROM factura;
-- DELETE FROM usuario;
-- DELETE FROM electrodomestico;

-- ============================================
-- TABLA: electrodomestico (10 registros)
-- ============================================
INSERT INTO electrodomestico (codigo, nombre, PRECIOVENTA) VALUES
('REF-001', 'Refrigeradora Samsung 18 pies', 1250.00),
('LAV-001', 'Lavadora LG 15kg Carga Frontal', 850.00),
('MIC-001', 'Microondas Panasonic 1.2 pies', 180.00),
('COC-001', 'Cocina Indurama 4 Quemadores', 450.00),
('LIC-001', 'Licuadora Oster 600W', 65.00),
('TEL-001', 'Televisor Sony Smart TV 55 pulgadas', 980.00),
('AIR-001', 'Aire Acondicionado Split 12000 BTU', 720.00),
('ASP-001', 'Aspiradora Electrolux 1800W', 220.00),
('CAF-001', 'Cafetera Oster 12 Tazas', 45.00),
('VEN-001', 'Ventilador de Pedestal Samurai', 38.00);

-- ============================================
-- TABLA: usuario (10 registros)
-- ============================================
INSERT INTO usuario (username, password, rol, activo) VALUES
('vendedor1', 'vend123', 'USER', 1),
('vendedor2', 'vend123', 'USER', 1),
('vendedor3', 'vend123', 'USER', 1),
('supervisor', 'super123', 'ADMIN', 1),
('cajero1', 'cajero123', 'USER', 1),
('cajero2', 'cajero123', 'USER', 1),
('gerente', 'gerente123', 'ADMIN', 1),
('inventario', 'invent123', 'USER', 1),
('contabilidad', 'conta123', 'ADMIN', 0);

-- ============================================
-- TABLA: factura (10 registros)
-- ============================================
-- Facturas con EFECTIVO (sin id_credito)
INSERT INTO factura (fecha, cedula_cliente, nombre_cliente, forma_pago, total_bruto, descuento, total_neto, id_credito) VALUES
('2024-09-15', '1234567890', 'María Fernanda González López', 'EFECTIVO', 2100.00, 0.00, 2100.00, NULL),
('2024-09-18', '0987654321', 'Carlos Alberto Mendoza Torres', 'EFECTIVO', 850.00, 42.50, 807.50, NULL),
('2024-09-22', '1122334455', 'Ana Patricia Morales Ruiz', 'EFECTIVO', 495.00, 0.00, 495.00, NULL);

-- Facturas a CREDITO (con id_credito) - SIN DESCUENTO
INSERT INTO factura (fecha, cedula_cliente, nombre_cliente, forma_pago, total_bruto, descuento, total_neto, id_credito) VALUES
('2024-10-05', '5544332211', 'Luis Fernando Castillo Pérez', 'CREDITO', 2230.00, 0.00, 2230.00, 3),
('2024-10-10', '6677889900', 'Sandra Elizabeth Ramírez Vega', 'CREDITO', 3850.00, 0.00, 3850.00, 5),
('2024-10-15', '9988776655', 'Jorge Andrés Salazar Díaz', 'CREDITO', 1700.00, 0.00, 1700.00, 6),
('2024-10-20', '1357924680', 'Patricia Alejandra Ortiz Castro', 'CREDITO', 5460.00, 0.00, 5460.00, 7),
('2024-10-25', '2468013579', 'Roberto Miguel Herrera Núñez', 'CREDITO', 1030.00, 0.00, 1030.00, 8),
('2024-11-01', '3692581470', 'Gabriela Cristina Silva Flores', 'CREDITO', 2390.00, 0.00, 2390.00, 9),
('2024-11-05', '7894561230', 'Diego Sebastián Vargas Paredes', 'CREDITO', 3610.00, 0.00, 3610.00, 10);

-- ============================================
-- TABLA: detalle_factura (30 registros - múltiples detalles por factura)
-- ============================================
-- Factura 1 (EFECTIVO: Refrigeradora + Microondas)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 1250.00, 1250.00),
(1, 3, 2, 180.00, 360.00),
(1, 5, 1, 65.00, 65.00),
(1, 9, 5, 45.00, 225.00),
(1, 10, 5, 38.00, 190.00);

-- Factura 2 (EFECTIVO: Lavadora)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(2, 2, 1, 850.00, 850.00);

-- Factura 3 (EFECTIVO: Cocina + Licuadora + Cafetera)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(3, 4, 1, 450.00, 450.00),
(3, 5, 1, 65.00, 65.00),
(3, 9, 1, 45.00, 45.00);

-- Factura 4 (CREDITO: Refrigeradora + Televisor)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(4, 1, 1, 1250.00, 1250.00),
(4, 6, 1, 980.00, 980.00);

-- Factura 5 (CREDITO: Lavadora + Refrigeradora + Televisor)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(5, 2, 1, 850.00, 850.00),
(5, 1, 1, 1250.00, 1250.00),
(5, 6, 1, 980.00, 980.00),
(5, 7, 1, 720.00, 720.00);

-- Factura 6 (CREDITO: Aire Acondicionado + Televisor)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(6, 7, 1, 720.00, 720.00),
(6, 6, 1, 980.00, 980.00);

-- Factura 7 (CREDITO: Refrigeradora x2 + Lavadora x2 + Televisor)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(7, 1, 2, 1250.00, 2500.00),
(7, 2, 2, 850.00, 1700.00),
(7, 6, 1, 980.00, 980.00),
(7, 3, 1, 180.00, 180.00);

-- Factura 8 (CREDITO: Televisor + Microondas)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(8, 6, 1, 980.00, 980.00),
(8, 3, 1, 180.00, 180.00);

-- Factura 9 (CREDITO: Lavadora + Refrigeradora + Cocina)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(9, 2, 1, 850.00, 850.00),
(9, 1, 1, 1250.00, 1250.00),
(9, 4, 1, 450.00, 450.00);

-- Factura 10 (CREDITO: Refrigeradora + Lavadora + Aire + Aspiradora)
INSERT INTO detalle_factura (id_factura, id_electrodomestico, cantidad, precio_unitario, subtotal) VALUES
(10, 1, 1, 1250.00, 1250.00),
(10, 2, 1, 850.00, 850.00),
(10, 7, 1, 720.00, 720.00),
(10, 8, 1, 220.00, 220.00),
(10, 5, 1, 65.00, 65.00);

-- ============================================
-- FIN DEL SCRIPT
-- ============================================
