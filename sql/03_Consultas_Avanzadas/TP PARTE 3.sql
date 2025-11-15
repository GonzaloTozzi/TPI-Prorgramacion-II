USE tpi;
-- CONSULTA 1: JOIN Y ANALISIS TEMPORAL
-- SE BUSCAN LOS 10 PEDIDOS CON MAYOR DIFERENCIA ENTRE EL DESPACHO Y LA FECHA ESTIMADA
SELECT
    p.numero AS numero_pedido,
    e.empresa,
    DATEDIFF(e.fecha_estimada, e.fecha_despacho) AS dias_diferencia_estimada
FROM Pedido p
JOIN Envio e ON p.id = e.id_pedido
WHERE e.fecha_despacho IS NOT NULL AND e.fecha_estimada IS NOT NULL
ORDER BY dias_diferencia_estimada DESC
LIMIT 10;

-- CONSULTA 2: GROUP BY Y HAVING (Reporte de Clientes VIP)
SELECT
    cliente_nombre,
    SUM(total) AS gasto_total_acumulado,
    COUNT(id) AS cantidad_pedidos
FROM Pedido
GROUP BY cliente_nombre
HAVING SUM(total) > 4000 -- Filtra a los clientes cuyo gasto total acumulado es mayor a 4000
ORDER BY gasto_total_acumulado DESC
LIMIT 10;

-- CONSULTA 3: SUBCONSULTA (Busca envíos de la empresa más frecuente en tipo 'ESTANDAR')
SELECT
    p.numero,
    e.empresa,
    e.tracking
FROM Pedido p
JOIN Envio e ON p.id = e.id_pedido
WHERE e.empresa = (
    -- Subconsulta: Encuentra la empresa con el mayor número de envíos 'ESTANDAR'
    SELECT empresa
    FROM Envio
    WHERE tipo = 'ESTANDAR'
    GROUP BY empresa
    ORDER BY COUNT(*) DESC
    LIMIT 1 -- Devuelve una única empresa
);


-- CONSULTA 4: JOIN SIMPLE PARA PRUEBA DE RENDIMIENTO (BUSCA CLIENTE 999)
SELECT *
FROM Pedido p JOIN Envio e ON p.id = e.id_pedido
WHERE p.cliente_nombre = 'Cliente_999';


-- CREACION DE VISTA 
CREATE VIEW v_estado_completo AS
SELECT
    p.numero AS numero_pedido,
    p.fecha AS fecha_pedido,
    p.estado AS estado_pedido_tienda, -- Estado del pedido en la tienda
    e.tracking AS nro_tracking,
    e.empresa AS empresa_envio,
    e.estado AS estado_envio_logistica -- Estado del pedido en la logística
FROM Pedido p
JOIN Envio e ON p.id = e.id_pedido
WHERE p.eliminado = FALSE;

-- CREACION DE INDICES PARA LAS PRUEBAS DE RENDIMIENTO
CREATE INDEX idx_cliente_nombre ON Pedido(cliente_nombre);
CREATE INDEX idx_pedido_fecha ON Pedido(fecha);

-- EJECUCION DE PRUEBAS PARA EL INFORME CON/SIN INDICE (EJEMPLO IGUALDAD)
-- 1. Ejecución SIN índice:
SELECT * FROM Pedido USE INDEX () WHERE cliente_nombre = 'Cliente_1000'; 
SELECT * FROM Pedido USE INDEX () WHERE cliente_nombre = 'Cliente_1000'; 
SELECT * FROM Pedido USE INDEX () WHERE cliente_nombre = 'Cliente_1000'; 
SELECT * FROM Pedido USE INDEX () WHERE cliente_nombre = 'Cliente_1000'; 
-- 2. Ejecución CON índice:
SELECT * FROM Pedido WHERE cliente_nombre = 'Cliente_1000';
SELECT * FROM Pedido WHERE cliente_nombre = 'Cliente_1000';
SELECT * FROM Pedido WHERE cliente_nombre = 'Cliente_1000';
SELECT * FROM Pedido WHERE cliente_nombre = 'Cliente_1000';


-- EJECUCION DE PRUEBAS PARA EL INFORME CON/SIN INDICE (EJEMPLO RANGO)
-- 1. Ejecución SIN Índice:
EXPLAIN SELECT COUNT(*) FROM Pedido USE INDEX () WHERE fecha BETWEEN '2025-01-01' AND '2025-06-30'; 
-- 2. Ejecución CON Índice:
EXPLAIN SELECT COUNT(*) FROM Pedido WHERE fecha BETWEEN '2025-01-01' AND '2025-06-30'; 