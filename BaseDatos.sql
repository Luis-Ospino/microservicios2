-- ============================================================
-- BaseDatos.sql
-- Proyecto: microservicios2
-- Motor: PostgreSQL 16+
-- ============================================================
-- Este script contiene el esquema de datos para:
-- 1) ms_clientes_personas
-- 2) ms_cuentas_movimientos
--
-- Nota:
-- - En Docker, cada microservicio usa su propia base de datos:
--   * ms_clientes_personas
--   * ms_cuentas_movimientos
-- - Si ya existen las tablas, este script no las recrea.
-- ============================================================

-- ============================
-- 1) ESQUEMA ms_clientes_personas
-- ============================

-- Tabla personas
CREATE TABLE IF NOT EXISTS personas (
    id UUID PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(30) NOT NULL CHECK (genero IN ('MASCULINO', 'FEMENINO', 'OTRO')),
    edad INTEGER NOT NULL CHECK (edad >= 18 AND edad <= 120),
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Tabla clientes
CREATE TABLE IF NOT EXISTS clientes (
    id UUID PRIMARY KEY,
    persona_id UUID NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('ACTIVO', 'INACTIVO', 'BLOQUEADO', 'ELIMINADO')),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_clientes_persona_id
        FOREIGN KEY (persona_id) REFERENCES personas(id)
);

-- Indices clientes
CREATE INDEX IF NOT EXISTS idx_clientes_persona_id ON clientes(persona_id);
CREATE INDEX IF NOT EXISTS idx_clientes_estado ON clientes(estado);

-- ============================
-- 2) ESQUEMA ms_cuentas_movimientos
-- ============================

-- Tabla cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id UUID PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL CHECK (
        tipo_cuenta IN ('AHORROS', 'CORRIENTE', 'CREDITO', 'DEPOSITO_FIJO')
    ),
    saldo_inicial NUMERIC(15,2) NOT NULL CHECK (saldo_inicial >= 0),
    saldo_disponible NUMERIC(15,2) NOT NULL CHECK (saldo_disponible >= 0),
    estado VARCHAR(20) NOT NULL CHECK (
        estado IN ('ACTIVA', 'INACTIVA', 'SUSPENDIDA', 'CERRADA')
    ),
    cliente_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_cuentas_numero ON cuentas(numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_cuentas_cliente_id ON cuentas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_cuentas_estado ON cuentas(estado);

-- Tabla movimientos
CREATE TABLE IF NOT EXISTS movimientos (
    id UUID PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(30) NOT NULL CHECK (
        tipo_movimiento IN (
            'DEPOSITO',
            'RETIRO',
            'TRANSFERENCIA_SALIDA',
            'TRANSFERENCIA_ENTRADA',
            'PAGO_SERVICIOS',
            'INTERES'
        )
    ),
    valor NUMERIC(15,2) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL CHECK (saldo >= 0),
    cuenta_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_movimientos_cuenta_id
        FOREIGN KEY (cuenta_id) REFERENCES cuentas(id)
);

CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta_id ON movimientos(cuenta_id);
CREATE INDEX IF NOT EXISTS idx_movimientos_fecha ON movimientos(fecha);
CREATE INDEX IF NOT EXISTS idx_movimientos_tipo ON movimientos(tipo_movimiento);

