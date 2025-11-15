USE tpi;
COMMIT;

-- Asegurarse de que las tablas se borren en el orden correcto (hija antes que padre)
DROP TABLE IF EXISTS Envio;
DROP TABLE IF EXISTS Pedido;

CREATE TABLE Pedido (
    id BIGINT NOT NULL,
    eliminado BOOLEAN,
    numero VARCHAR(20) NOT NULL,
    fecha DATE NOT NULL,
    cliente_nombre VARCHAR(120) NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    estado VARCHAR(20) NOT NULL,

    -- Clave Primaria
    CONSTRAINT pk_pedido PRIMARY KEY (id),

    -- Restriccion de unicidad
    CONSTRAINT uk_pedido_numero UNIQUE (numero),

    -- Restriccion de CHECK (simulación de ENUM)
    CONSTRAINT chk_pedido_estado CHECK (estado IN ('NUEVO', 'FACTURADO', 'ENVIADO'))
);

-- Creacion de la tabla Envio

CREATE TABLE Envio (
    id BIGINT NOT NULL,
    id_pedido BIGINT NOT NULL,
    eliminado BOOLEAN,
    tracking VARCHAR(40) NOT NULL,
    empresa VARCHAR(30) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    costo DECIMAL(10, 2) NOT NULL,
    fecha_despacho DATE,
    fecha_estimada DATE,
    estado VARCHAR(30) NOT NULL, 

    -- Clave Primaria
    CONSTRAINT pk_envio PRIMARY KEY (id),

    -- Restricciones de Unicidad
    CONSTRAINT uk_envio_tracking UNIQUE (tracking),
    CONSTRAINT uk_envio_id_pedido UNIQUE (id_pedido),

    -- Clave Foránea
    CONSTRAINT fk_envio_pedido FOREIGN KEY (id_pedido) 
        REFERENCES Pedido (id) 
        ON DELETE CASCADE,

    -- Restricciones de CHECK
    CONSTRAINT chk_envio_empresa CHECK (empresa IN ('ANDREANI', 'OCA', 'CORREO_ARG')),
    CONSTRAINT chk_envio_tipo CHECK (tipo IN ('ESTANDAR', 'EXPRES')),
    CONSTRAINT chk_envio_estado CHECK (estado IN ('EN_PREPARACION', 'EN_TRANSITO', 'ENTREGADO'))
);


	-- TEST 2 EJEMPLOS CORRECTOS y 2 ERRONEOS:
-- Inserciones Correctas
INSERT INTO Pedido (id, eliminado, numero, fecha, cliente_nombre, total, estado)
VALUES (1, FALSE, 'P0001', '2025-10-10', 'Juan Perez', 1500.50, 'NUEVO');

INSERT INTO Envio (id, id_pedido, eliminado, tracking, empresa, tipo, costo, fecha_despacho, fecha_estimada, estado)
VALUES (101, 1, FALSE, 'TRK12345', 'ANDREANI', 'ESTANDAR', 250.00, '2025-10-11', '2025-10-15', 'EN_PREPARACION');

-- Inserciones Erroneas (para demostrar los Constraints)
-- ERROR 1: Violacion de UNIQUE (numero de Pedido duplicado)
INSERT INTO Pedido (id, eliminado, numero, fecha, cliente_nombre, total, estado)
VALUES (2, FALSE, 'P0001', '2025-10-12', 'Maria Lopez', 3000.00, 'FACTURADO'); 

-- ERROR 2: Violacion de CHECK (Estado no permitido)
INSERT INTO Pedido (id, eliminado, numero, fecha, cliente_nombre, total, estado)
VALUES (3, FALSE, 'P0003', '2025-10-13', 'Carlos Diaz', 500.00, 'CANCELADO');