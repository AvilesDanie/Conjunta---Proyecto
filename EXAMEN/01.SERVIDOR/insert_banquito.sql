-- ============================================
-- SCRIPT DE DATOS PARA BD BANQUITO
-- ============================================
-- Orden de inserción: cliente -> usuario -> cuenta -> credito -> cuota_amortizacion -> movimiento

-- ============================================
-- LIMPIEZA DE DATOS EXISTENTES (opcional)
-- ============================================
-- DELETE FROM movimiento;
-- DELETE FROM cuota_amortizacion;
-- DELETE FROM credito;
-- DELETE FROM cuenta;
-- DELETE FROM usuario;
-- DELETE FROM cliente;

-- ============================================
-- TABLA: cliente (10 registros)
-- ============================================
INSERT INTO cliente (cedula, nombre, fecha_nacimiento, estado_civil) VALUES
('1234567890', 'María Fernanda González López', '1985-03-15', 'CASADO'),
('0987654321', 'Carlos Alberto Mendoza Torres', '1990-07-22', 'SOLTERO'),
('1122334455', 'Ana Patricia Morales Ruiz', '1988-11-30', 'CASADO'),
('5544332211', 'Luis Fernando Castillo Pérez', '1992-05-18', 'SOLTERO'),
('6677889900', 'Sandra Elizabeth Ramírez Vega', '1987-09-10', 'CASADO'),
('9988776655', 'Jorge Andrés Salazar Díaz', '1995-01-25', 'SOLTERO'),
('1357924680', 'Patricia Alejandra Ortiz Castro', '1983-12-08', 'CASADO'),
('2468013579', 'Roberto Miguel Herrera Núñez', '1991-04-14', 'SOLTERO'),
('3692581470', 'Gabriela Cristina Silva Flores', '1989-08-20', 'CASADO'),
('7894561230', 'Diego Sebastián Vargas Paredes', '1993-06-05', 'SOLTERO');

-- ============================================
-- TABLA: usuario (10 registros)
-- ============================================
INSERT INTO usuario (username, password, rol, activo) VALUES
('gerente1', 'gerente123', 'ADMIN', 1),
('cajero1', 'cajero123', 'USER', 1),
('cajero2', 'cajero123', 'USER', 1),
('asesor1', 'asesor123', 'USER', 1),
('asesor2', 'asesor123', 'USER', 1),
('supervisor', 'super123', 'ADMIN', 1),
('analista1', 'analista123', 'USER', 1),
('contabilidad', 'conta123', 'ADMIN', 1),
('soporte', 'soporte123', 'USER', 0);

-- ============================================
-- TABLA: cuenta (10 registros)
-- ============================================
INSERT INTO cuenta (num_cuenta, cedula_cliente, tipo_cuenta, saldo) VALUES
('CTA-0001-2024', '1234567890', 'AHORROS', 5500.00),
('CTA-0002-2024', '0987654321', 'CORRIENTE', 12300.50),
('CTA-0003-2024', '1122334455', 'AHORROS', 8750.25),
('CTA-0004-2024', '5544332211', 'CORRIENTE', 3200.00),
('CTA-0005-2024', '6677889900', 'AHORROS', 15000.00),
('CTA-0006-2024', '9988776655', 'CORRIENTE', 6800.75),
('CTA-0007-2024', '1357924680', 'AHORROS', 22500.00),
('CTA-0008-2024', '2468013579', 'CORRIENTE', 4500.00),
('CTA-0009-2024', '3692581470', 'AHORROS', 9200.50),
('CTA-0010-2024', '7894561230', 'CORRIENTE', 7300.00);

-- ============================================
-- TABLA: credito (10 registros)
-- ============================================
INSERT INTO credito (cedula_cliente, num_cuenta_asociada, monto, plazo_meses, tasa_anual, fecha_aprobacion, estado) VALUES
('1234567890', 'CTA-0001-2024', 5000.00, 12, 16.00, '2024-01-15', 'ACTIVO'),
('0987654321', 'CTA-0002-2024', 10000.00, 24, 16.00, '2024-02-20', 'ACTIVO'),
('1122334455', 'CTA-0003-2024', 7500.00, 18, 16.00, '2024-03-10', 'ACTIVO'),
('5544332211', 'CTA-0004-2024', 3000.00, 12, 16.00, '2024-04-05', 'CANCELADO'),
('6677889900', 'CTA-0005-2024', 15000.00, 36, 16.00, '2024-05-12', 'ACTIVO'),
('9988776655', 'CTA-0006-2024', 8000.00, 24, 16.00, '2024-06-18', 'ACTIVO'),
('1357924680', 'CTA-0007-2024', 12000.00, 30, 16.00, '2024-07-22', 'ACTIVO'),
('2468013579', 'CTA-0008-2024', 4500.00, 12, 16.00, '2024-08-14', 'APROBADO'),
('3692581470', 'CTA-0009-2024', 6000.00, 18, 16.00, '2024-09-08', 'ACTIVO'),
('7894561230', 'CTA-0010-2024', 9000.00, 24, 16.00, '2024-10-25', 'ACTIVO');

-- ============================================
-- TABLA: cuota_amortizacion (30 registros - 3 cuotas por cada uno de los primeros 10 créditos)
-- ============================================
-- Crédito 1 (ID=1, monto=5000, 12 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(1, 1, 449.89, 66.67, 383.22, 4616.78, '2024-02-15', 'PAGADA'),
(1, 2, 449.89, 61.56, 388.33, 4228.45, '2024-03-15', 'PAGADA'),
(1, 3, 449.89, 56.38, 393.51, 3834.94, '2024-04-15', 'PENDIENTE');

-- Crédito 2 (ID=2, monto=10000, 24 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(2, 1, 493.99, 133.33, 360.66, 9639.34, '2024-03-20', 'PAGADA'),
(2, 2, 493.99, 128.52, 365.47, 9273.87, '2024-04-20', 'PAGADA'),
(2, 3, 493.99, 123.65, 370.34, 8903.53, '2024-05-20', 'PENDIENTE');

-- Crédito 3 (ID=3, monto=7500, 18 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(3, 1, 466.53, 100.00, 366.53, 7133.47, '2024-04-10', 'PAGADA'),
(3, 2, 466.53, 95.11, 371.42, 6762.05, '2024-05-10', 'PAGADA'),
(3, 3, 466.53, 90.16, 376.37, 6385.68, '2024-06-10', 'PENDIENTE');

-- Crédito 4 (ID=4, monto=3000, 12 meses) - CANCELADO
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(4, 1, 269.94, 40.00, 229.94, 2770.06, '2024-05-05', 'PAGADA'),
(4, 2, 269.94, 36.93, 233.01, 2537.05, '2024-06-05', 'PAGADA'),
(4, 3, 269.94, 33.83, 236.11, 2300.94, '2024-07-05', 'ANULADA');

-- Crédito 5 (ID=5, monto=15000, 36 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(5, 1, 528.25, 200.00, 328.25, 14671.75, '2024-06-12', 'PAGADA'),
(5, 2, 528.25, 195.62, 332.63, 14339.12, '2024-07-12', 'PAGADA'),
(5, 3, 528.25, 191.19, 337.06, 14002.06, '2024-08-12', 'PENDIENTE');

-- Crédito 6 (ID=6, monto=8000, 24 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(6, 1, 395.19, 106.67, 288.52, 7711.48, '2024-07-18', 'PAGADA'),
(6, 2, 395.19, 102.82, 292.37, 7419.11, '2024-08-18', 'PAGADA'),
(6, 3, 395.19, 98.92, 296.27, 7122.84, '2024-09-18', 'PENDIENTE');

-- Crédito 7 (ID=7, monto=12000, 30 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(7, 1, 476.89, 160.00, 316.89, 11683.11, '2024-08-22', 'PAGADA'),
(7, 2, 476.89, 155.78, 321.11, 11362.00, '2024-09-22', 'PAGADA'),
(7, 3, 476.89, 151.49, 325.40, 11036.60, '2024-10-22', 'PENDIENTE');

-- Crédito 8 (ID=8, monto=4500, 12 meses) - APROBADO
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(8, 1, 404.90, 60.00, 344.90, 4155.10, '2024-09-14', 'PENDIENTE'),
(8, 2, 404.90, 55.40, 349.50, 3805.60, '2024-10-14', 'PENDIENTE'),
(8, 3, 404.90, 50.74, 354.16, 3451.44, '2024-11-14', 'PENDIENTE');

-- Crédito 9 (ID=9, monto=6000, 18 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(9, 1, 373.22, 80.00, 293.22, 5706.78, '2024-10-08', 'PAGADA'),
(9, 2, 373.22, 76.09, 297.13, 5409.65, '2024-11-08', 'PENDIENTE'),
(9, 3, 373.22, 72.13, 301.09, 5108.56, '2024-12-08', 'PENDIENTE');

-- Crédito 10 (ID=10, monto=9000, 24 meses)
INSERT INTO cuota_amortizacion (id_credito, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo, fecha_vencimiento, estado) VALUES
(10, 1, 444.49, 120.00, 324.49, 8675.51, '2024-11-25', 'PENDIENTE'),
(10, 2, 444.49, 115.67, 328.82, 8346.69, '2024-12-25', 'PENDIENTE'),
(10, 3, 444.49, 111.29, 333.20, 8013.49, '2025-01-25', 'PENDIENTE');

-- ============================================
-- TABLA: movimiento (30 registros - múltiples movimientos por cuenta)
-- ============================================
-- Cuenta 1
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0001-2024', 'DEP', 'INGRESO', false, 1000.00, '2024-01-10'),
('CTA-0001-2024', 'RET', 'EGRESO', false, 200.00, '2024-02-15'),
('CTA-0001-2024', 'TRA', 'EGRESO', true, 300.00, '2024-03-20');

-- Cuenta 2
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0002-2024', 'DEP', 'INGRESO', false, 5000.00, '2024-01-15'),
('CTA-0002-2024', 'TRA', 'INGRESO', true, 500.00, '2024-02-10'),
('CTA-0002-2024', 'RET', 'EGRESO', false, 1200.00, '2024-03-05');

-- Cuenta 3
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0003-2024', 'DEP', 'INGRESO', false, 2500.00, '2024-01-20'),
('CTA-0003-2024', 'RET', 'EGRESO', false, 750.00, '2024-02-25'),
('CTA-0003-2024', 'DEP', 'INGRESO', false, 1000.00, '2024-03-30');

-- Cuenta 4
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0004-2024', 'DEP', 'INGRESO', false, 1500.00, '2024-02-01'),
('CTA-0004-2024', 'TRA', 'EGRESO', true, 300.00, '2024-03-15'),
('CTA-0004-2024', 'RET', 'EGRESO', false, 500.00, '2024-04-10');

-- Cuenta 5
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0005-2024', 'DEP', 'INGRESO', false, 10000.00, '2024-02-05'),
('CTA-0005-2024', 'RET', 'EGRESO', false, 2000.00, '2024-03-12'),
('CTA-0005-2024', 'TRA', 'INGRESO', true, 1500.00, '2024-04-18');

-- Cuenta 6
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0006-2024', 'DEP', 'INGRESO', false, 3000.00, '2024-02-10'),
('CTA-0006-2024', 'RET', 'EGRESO', false, 800.00, '2024-03-20'),
('CTA-0006-2024', 'DEP', 'INGRESO', false, 1200.00, '2024-04-25');

-- Cuenta 7
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0007-2024', 'DEP', 'INGRESO', false, 15000.00, '2024-02-15'),
('CTA-0007-2024', 'TRA', 'EGRESO', true, 2500.00, '2024-03-22'),
('CTA-0007-2024', 'RET', 'EGRESO', false, 3000.00, '2024-05-10');

-- Cuenta 8
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0008-2024', 'DEP', 'INGRESO', false, 2000.00, '2024-03-01'),
('CTA-0008-2024', 'RET', 'EGRESO', false, 500.00, '2024-04-05'),
('CTA-0008-2024', 'TRA', 'INGRESO', true, 300.00, '2024-05-15');

-- Cuenta 9
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0009-2024', 'DEP', 'INGRESO', false, 5000.00, '2024-03-05'),
('CTA-0009-2024', 'RET', 'EGRESO', false, 1200.00, '2024-04-10'),
('CTA-0009-2024', 'DEP', 'INGRESO', false, 800.00, '2024-05-20');

-- Cuenta 10
INSERT INTO movimiento (num_cuenta, tipo, naturaleza, interno_transferencia, valor, fecha) VALUES
('CTA-0010-2024', 'DEP', 'INGRESO', false, 4000.00, '2024-03-10'),
('CTA-0010-2024', 'TRA', 'EGRESO', true, 700.00, '2024-04-15'),
('CTA-0010-2024', 'RET', 'EGRESO', false, 1000.00, '2024-05-25');

-- ============================================
-- FIN DEL SCRIPT
-- ============================================
