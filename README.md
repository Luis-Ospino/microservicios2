# Microservicios de Clientes y Cuentas

Este proyecto implementa una arquitectura de microservicios para la gestión de clientes, personas, cuentas y movimientos bancarios utilizando Spring Boot 3, Java 21 y PostgreSQL.

## Arquitectura

### Microservicios

1. **ms-clientes-personas** (Puerto 8081)
   - Gestión de personas y clientes
   - Base de datos: PostgreSQL (puerto 5433)

2. **ms-cuentas-movimientos** (Puerto 8082)
   - Gestión de cuentas y movimientos
   - Base de datos: PostgreSQL (puerto 5434)

### Infraestructura

- **PostgreSQL**: Dos instancias separadas para cada microservicio
- **RabbitMQ**: Message broker con management plugin (puerto 15672)
- **Docker Compose**: Orquestación completa de servicios

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring AMQP** (RabbitMQ)
- **PostgreSQL 16**
- **RabbitMQ 3-management**
- **MapStruct** (mapeo DTO)
- **Lombok**
- **Bean Validation**
- **OpenAPI/Swagger**

## Características Principales

### Entidades JPA
- **UUID como clave primaria**
- **Auditoría automática** (createdAt, updatedAt)
- **Validaciones Bean Validation**
- **Enums tipados** para estados y tipos
- **Índices optimizados**

### APIs REST
- **CRUD completo** para todas las entidades
- **Validaciones de entrada**
- **Manejo de errores estructurado**
- **Códigos HTTP apropiados**
- **Documentación OpenAPI**

### Microservicios
- **Comunicación asíncrona** vía RabbitMQ
- **Transacciones distribuidas**
- **Separación de responsabilidades**

## Estructura del Proyecto

```
microservicios2/
├── docker-compose.yml
├── ms-clientes-personas/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/example/msclientespersonas/
│       ├── controller/
│       │   ├── PersonaController.java
│       │   └── ClienteController.java
│       ├── dto/
│       ├── entity/
│       │   ├── enums/
│       │   ├── AuditableEntity.java
│       │   ├── Persona.java
│       │   └── Cliente.java
│       ├── mapper/
│       ├── repository/
│       ├── service/
│       └── exception/
└── ms-cuentas-movimientos/
    ├── Dockerfile
    ├── pom.xml
    └── src/main/java/com/example/mscuentasmovimientos/
        ├── controller/
        │   ├── CuentaController.java
        │   └── MovimientoController.java
        ├── dto/
        ├── entity/
        │   ├── enums/
        │   ├── AuditableEntity.java
        │   ├── Cuenta.java
        │   └── Movimiento.java
        ├── mapper/
        ├── repository/
        ├── service/
        └── exception/
```

## APIs Disponibles

### ms-clientes-personas (http://localhost:8081)

#### Personas
- `GET /api/personas` - Listar todas las personas
- `GET /api/personas/{id}` - Obtener persona por ID
- `POST /api/personas` - Crear nueva persona
- `PUT /api/personas/{id}` - Actualizar persona
- `DELETE /api/personas/{id}` - Eliminar persona

#### Clientes
- `GET /api/clientes` - Listar todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `POST /api/clientes` - Crear nuevo cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

### ms-cuentas-movimientos (http://localhost:8082)

#### Cuentas
- `GET /api/cuentas` - Listar todas las cuentas
- `GET /api/cuentas/{id}` - Obtener cuenta por ID
- `GET /api/cuentas/cliente/{clienteId}` - Cuentas por cliente
- `POST /api/cuentas` - Crear nueva cuenta
- `PUT /api/cuentas/{id}` - Actualizar cuenta
- `DELETE /api/cuentas/{id}` - Eliminar cuenta

#### Movimientos
- `GET /api/movimientos` - Listar todos los movimientos
- `GET /api/movimientos/{id}` - Obtener movimiento por ID
- `GET /api/movimientos/cuenta/{cuentaId}` - Movimientos por cuenta
- `GET /api/movimientos/cuenta/{cuentaId}/fecha?fechaInicio=...&fechaFin=...` - Movimientos por rango de fechas
- `POST /api/movimientos` - Crear nuevo movimiento
- `DELETE /api/movimientos/{id}` - Eliminar movimiento

#### Reportes
- `GET /reportes?clienteId={uuid}&fechaInicio={datetime}&fechaFin={datetime}` - Generar reporte bancario completo

### Formato de Parámetros de Reporte

- `clienteId`: UUID del cliente (requerido)
- `fechaInicio`: Fecha/hora de inicio en formato ISO 8601 (yyyy-MM-dd'T'HH:mm:ss) (requerido)
- `fechaFin`: Fecha/hora de fin en formato ISO 8601 (yyyy-MM-dd'T'HH:mm:ss) (requerido)

### Respuesta del Reporte

```json
{
  "cliente": {
    "id": "uuid",
    "nombre": "string",
    "identificacion": "string",
    "direccion": "string",
    "telefono": "string",
    "fechaCreacion": "datetime"
  },
  "cuentas": [
    {
      "id": "uuid",
      "numeroCuenta": "string",
      "tipoCuenta": "AHORROS/CORRIENTE",
      "estado": "ACTIVA/INACTIVA",
      "saldoInicial": 1000.00,
      "saldoDisponible": 950.00,
      "fechaCreacion": "datetime",
      "movimientos": [...]
    }
  ],
  "movimientos": [...],
  "saldoInicialTotal": 1000.00,
  "saldoDisponibleTotal": 950.00
### Ejemplo de Uso del Reporte

```bash
curl -X GET "http://localhost:8082/reportes?clienteId=123e4567-e89b-12d3-a456-426614174000&fechaInicio=2026-01-01T00:00:00&fechaFin=2026-12-31T23:59:59" \
  -H "accept: application/json"
```

### Notas sobre el Reporte

- Los datos del cliente son simulados (en producción requeriría integración con ms-clientes-personas)
- Los movimientos están filtrados por el rango de fechas especificado
- Los saldos totales representan la suma de todas las cuentas del cliente
- Todas las operaciones son de solo lectura y no afectan los datos

## Documentación API

Cada microservicio incluye documentación OpenAPI/Swagger:

- ms-clientes-personas: http://localhost:8081/swagger-ui.html
- ms-cuentas-movimientos: http://localhost:8082/swagger-ui.html

## RabbitMQ Management

- URL: http://localhost:15672
- Usuario: guest
- Contraseña: guest

## Desarrollo

### Prerrequisitos

- Java 21
- Maven 3.9+
- Docker y Docker Compose

### Variables de Entorno

Las aplicaciones utilizan variables de entorno configuradas en `docker-compose.yml`:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_RABBITMQ_HOST`
- `SPRING_RABBITMQ_PORT`
- `SPRING_RABBITMQ_USERNAME`
- `SPRING_RABBITMQ_PASSWORD`
- `SERVER_PORT`

## Despliegue

Ver el documento `DEPLOYMENT.md` para instrucciones detalladas de ejecución.