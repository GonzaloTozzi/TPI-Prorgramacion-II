-- 1. Crear el usuario con privilegios mínimos (SELECT)
DROP USER IF EXISTS 'user_reporter'@'localhost';
CREATE USER 'user_reporter'@'localhost' IDENTIFIED BY 'securepass';

-- 2. Otorgar solo los permisos SELECT necesarios sobre las tablas
GRANT SELECT ON tpi.Pedido TO 'user_reporter'@'localhost';
GRANT SELECT ON tpi.Envio TO 'user_reporter'@'localhost';

-- 3. Vistas que ocultan información sensible (total, costo, eliminado)
CREATE VIEW v_pedido_publico AS
SELECT
    id,
    numero,
    fecha,
    cliente_nombre,
    estado
FROM Pedido
WHERE eliminado = FALSE;

CREATE VIEW v_seguimiento_logistico AS
SELECT
    p.numero AS pedido_numero,
    e.tracking AS nro_tracking,
    e.empresa,
    e.tipo,
    e.fecha_estimada,
    e.estado
FROM Pedido p
JOIN Envio e ON p.id = e.id_pedido
WHERE e.eliminado = FALSE;

-- 4. Otorgar permisos SELECT sobre las vistas
GRANT SELECT ON tpi.v_pedido_publico TO 'user_reporter'@'localhost';
GRANT SELECT ON tpi.v_seguimiento_logistico TO 'user_reporter'@'localhost';
FLUSH PRIVILEGES;

-- 5. PRUEBAS DE INTEGRIDAD (para la documentación)
-- PRUEBA 1: VIOLACIÓN DE UNIQUE (Debe fallar)
INSERT INTO Pedido (id, eliminado, numero, fecha, cliente_nombre, total, estado)
VALUES (999999, FALSE, 'P000001', '2025-10-15', 'ClienteDuplicado', 100.00, 'NUEVO');

-- PRUEBA 2: VIOLACIÓN DE CHECK (Debe fallar)
INSERT INTO Pedido (id, eliminado, numero, fecha, cliente_nombre, total, estado)
VALUES (999998, FALSE, 'P999998', '2025-10-15', 'ClienteInvalido', 50.00, 'CANCELADO');