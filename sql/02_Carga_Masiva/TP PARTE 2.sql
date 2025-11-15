-- TRUNCATE para asegurar el estado inicial limpio
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Envio;
TRUNCATE TABLE Pedido;
SET FOREIGN_KEY_CHECKS = 1;

-- Inserción Masiva en Pedido (200.000 filas)
INSERT INTO Pedido (id, eliminado, numero, fecha, cliente_nombre, total, estado)
WITH RECURSIVE
  numeros (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM numeros WHERE n < 200000
  )
SELECT
    n,
    FALSE,
    CONCAT('P', LPAD(n, 6, '0')),
    DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 365) DAY),
    CONCAT('Cliente_', n),
    ROUND(RAND() * 5000 + 100, 2),
    CASE
        WHEN RAND() < 0.70 THEN 'ENVIADO'
        WHEN RAND() < 0.90 THEN 'FACTURADO'
        ELSE 'NUEVO'
    END
FROM numeros;

-- Inserción Masiva en Envio (referenciando los Pedidos existentes)
INSERT INTO Envio (id, id_pedido, eliminado, tracking, empresa, tipo, costo, fecha_despacho, fecha_estimada, estado)
SELECT
    -- Genera la PK única para Envío
    p.id + 200000,

    -- CLAVE DEL 1:1: FK única que referencia a Pedido.id
    p.id,

    FALSE,
    CONCAT('TRK', LPAD(p.id, 8, '0')), -- Generación de tracking único

    -- Empresa aleatoria (utiliza CASE/RAND para distribución)
    CASE FLOOR(RAND() * 3)
        WHEN 0 THEN 'ANDREANI'
        WHEN 1 THEN 'OCA'
        ELSE 'CORREO_ARG'
    END,

    -- Tipo aleatorio
    CASE WHEN RAND() < 0.8 THEN 'ESTANDAR' ELSE 'EXPRES' END,

    -- Costo
    CASE WHEN p.total > 500 THEN 0.00 ELSE 150.00 END,

    -- Fechas derivadas de la fecha de pedido
    DATE_ADD(p.fecha, INTERVAL 1 DAY),
    DATE_ADD(p.fecha, INTERVAL 5 + FLOOR(RAND() * 5) DAY),
    'EN_PREPARACION' -- Estado inicial
FROM Pedido p;