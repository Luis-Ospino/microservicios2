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