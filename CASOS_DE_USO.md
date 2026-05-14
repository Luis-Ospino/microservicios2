# Casos de Uso - Microservicios2

Este documento resume los casos de uso funcionales para validacion de endpoints con Postman.

## 1. Creacion de Usuarios

Se crean tres usuarios (personas/clientes):

| Nombres              | Direccion                | Telefono  | Contrasena | Estado |
|----------------------|--------------------------|-----------|------------|--------|
| Jose Lema            | Otavalo sn y principal   | 098254785 | 1234       | True   |
| Marianela Montalvo   | Amazonas y NNUU          | 097548965 | 5678       | True   |
| Juan Osorio          | 13 junio y Equinoccial   | 098874587 | 1245       | True   |

Endpoints relacionados:
- `POST /api/personas`
- `POST /api/clientes`

## 2. Creacion de Cuentas de Usuario

Se crean las siguientes cuentas:

| Numero Cuenta | Tipo      | Saldo Inicial | Estado | Cliente            |
|---------------|-----------|---------------|--------|--------------------|
| 478758        | Ahorro    | 2000          | True   | Jose Lema          |
| 225487        | Corriente | 100           | True   | Marianela Montalvo |
| 495878        | Ahorros   | 0             | True   | Juan Osorio        |
| 496825        | Ahorros   | 540           | True   | Marianela Montalvo |

Endpoint relacionado:
- `POST /api/cuentas`

## 3. Crear una nueva Cuenta Corriente para Jose Lema

| Numero Cuenta | Tipo      | Saldo Inicial | Estado | Cliente   |
|---------------|-----------|---------------|--------|-----------|
| 585545        | Corriente | 1000          | True   | Jose Lema |

Endpoint relacionado:
- `POST /api/cuentas`

## 4. Realizar Movimientos

Movimientos a ejecutar:

| Numero Cuenta | Tipo      | Saldo Inicial | Estado | Movimiento     |
|---------------|-----------|---------------|--------|----------------|
| 478758        | Ahorro    | 2000          | True   | Retiro de 575  |
| 225487        | Corriente | 100           | True   | Deposito de 600|
| 495878        | Ahorros   | 0             | True   | Deposito de 150|
| 496825        | Ahorros   | 540           | True   | Retiro de 540  |

Endpoint relacionado:
- `POST /api/movimientos`

## 5. Listado de Movimiento por Fechas x Usuario

Resultado esperado (ejemplo):

| Fecha     | Cliente            | Numero Cuenta | Tipo      | Saldo Inicial | Estado | Movimiento | Saldo Disponible |
|-----------|--------------------|---------------|-----------|---------------|--------|------------|------------------|
| 10/2/2022 | Marianela Montalvo | 225487        | Corriente | 100           | True   | 600        | 700              |
| 8/2/2022  | Marianela Montalvo | 496825        | Ahorros   | 540           | True   | -540       | 0                |

Ejemplo JSON de salida:

```json
{
  "Fecha": "10/2/2022",
  "Cliente": "Marianela Montalvo",
  "Numero Cuenta": "225487",
  "Tipo": "Corriente",
  "Saldo Inicial": 100,
  "Estado": true,
  "Movimiento": 600,
  "Saldo Disponible": 700
}
```

Endpoint relacionado:
- `GET /reportes?clienteId={idCliente}&fechaInicio={yyyy-MM-dd}&fechaFin={yyyy-MM-dd}`

## Flujo sugerido de validacion en Postman

1. Crear personas
2. Crear clientes
3. Crear cuentas
4. Registrar movimientos
5. Consultar reporte por rango de fechas y cliente

