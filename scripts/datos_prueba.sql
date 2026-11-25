--Caso para prueba S01
INSERT INTO persona (id, nombre, apellido, dni_cuit, telefono, email, eliminada) VALUES
(1, 'Carlos', 'Gómez', '20-22333444-5', '3424111111', 'cgomez@mail.com', 0),
(2, 'Laura', 'Fernández', '27-30111222-3', '3424222222', 'lfernandez@mail.com', 0),
(3, 'Martín', 'Sosa', '20-35666777-8', '3424333333', 'msosa@mail.com', 0),
(4, 'Julieta', 'Ríos', '27-38999000-1', '3424444444', 'jrios@mail.com', 0);

INSERT INTO propiedad (id, direccion, barrio, ciudad, tipo, cantidad_ambientes, metros_cuadrados, descripcion, comodidades, estado_disponibilidad, eliminada, propietario_id) VALUES
(1, 'San Martín 2450', 'Centro', 'Santa Fe', 'DEPARTAMENTO', 3, 75, 'Departamento con balcón', 'Cochera, aire acondicionado', 'ALQUILADA', 0, 1),
(2, 'Bv. Gálvez 1820', 'Candioti', 'Santa Fe', 'CASA', 4, 120, 'Casa con patio', 'Patio, parrilla', 'ALQUILADA', 0, 1),
(3, 'Rivadavia 3100', 'Centro', 'Santa Fe', 'LOCAL', 1, 50, 'Local comercial', 'Baño', 'DISPONIBLE', 0, 2),
(4, 'Urquiza 1500', 'Sur', 'Santa Fe', 'DEPARTAMENTO', 2, 45, 'Monoambiente', 'Amoblado', 'DISPONIBLE', 0, 2);

INSERT INTO contrato (id, propiedad_id, propietario_id, inquilino_id, fecha_inicio, fecha_fin, duracion_meses, importe_mensual, dia_vencimiento_mensual, descripcion, estado, eliminado) VALUES
(1, 1, 1, 3, '2026-03-01', NULL, 24, 350000.00, 10, 'Alquiler departamento San Martín 2450', 'ACTIVO', 0),
(2, 2, 1, 4, '2026-01-15', '2026-12-31', NULL, 280000.00, 31, 'Alquiler casa Bv. Gálvez 1820', 'ACTIVO', 0),
(3, 3, 2, 3, '2025-01-01', NULL, 12, 200000.00, 5, 'Alquiler local Rivadavia 3100 (finalizado)', 'FINALIZADO', 0),
(4, 4, 2, 4, '2026-06-01', NULL, 12, 150000.00, 10, 'Contrato dado de baja', 'ACTIVO', 1);

INSERT INTO usuario (id, username, password, rol) VALUES
(1, 'admin', 'admin123', 'ADMIN');

--IMPORTANTE: Ejecutar despues de las pruebas para el caso S01
--Caso para prueba S02
INSERT INTO factura (concepto, estado, fecha_emision, fecha_vencimiento, importe, periodo, tipo, contrato_id) VALUES
(NULL, 'PENDIENTE', '2026-11-15', '2026-12-10', 10000, 2026-12, 'ADICIONAL', 3)
