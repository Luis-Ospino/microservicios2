# Reto Técnico Backend – Microservicios Bancarios

## Tabla de Contenidos

- [Arquitectura](#arquitectura)
- [Microservicios](#microservicios)
- [Tecnologías](#tecnologías)
- [Ejecución Local](#ejecución-local)
- [Docker Compose](#docker-compose)
- [Endpoints Principales](#endpoints-principales)
- [Pruebas](#pruebas)
- [Decisiones Técnicas](#decisiones-técnicas)
- [Mejoras Futuras](#mejoras-futuras)
- [Comunicación Asíncrona](#comunicación-asíncrona)

---

## Arquitectura

El sistema está basado en una arquitectura de microservicios desacoplados, orientada a la gestión bancaria de clientes, cuentas y movimientos. Cada microservicio es autónomo, con su propia base de datos y comunicación asíncrona mediante RabbitMQ.

<!-- Puedes agregar un diagrama aquí si lo deseas -->

- **API Gateway** (futuro): punto de entrada único (no implementado en este reto)
- **Microservicios**: cada uno expone su propia API REST
- **Bases de datos independientes**: PostgreSQL por servicio
- **Mensajería**: RabbitMQ para eventos y sincronización

---

## Microservicios

### 1. ms-clientes-personas
- Gestión de clientes y personas
- CRUD completo
- Validaciones y auditoría

### 2. ms-cuentas-movimientos
- Gestión de cuentas bancarias y movimientos
- Reglas de negocio para depósitos y retiros
- Reportes agregados por cliente y periodo

---

## Tecnologías

- **Java 21**
- **Spring Boot 3.2.x**
- **Spring Data JPA**
- **Spring Validation**
- **Spring AMQP** (RabbitMQ)
- **MapStruct** (mapeo DTO)
- **Lombok**
- **PostgreSQL 16**
- **RabbitMQ 3-management**
- **Docker & Docker Compose**
- **JUnit 5 / Mockito** (pruebas)
- **OpenAPI/Swagger** (documentación)

---

## Ejecución Local

### Requisitos previos

- Java 21
- Maven 3.9+
- Docker y Docker Compose

### Ejecución con Maven

```bash
# En cada microservicio
cd ms-clientes-personas
mvn spring-boot:run

cd ms-cuentas-movimientos
mvn spring-boot:run
```

### Ejecución con Docker Compose (recomendado)

```bash
docker compose up --build
```

Esto levanta:
- 2 microservicios Spring Boot
- 2 instancias PostgreSQL
- RabbitMQ con UI de administración

---

## Endpoints Principales

### ms-clientes-personas

- `POST /api/clientes` – Crear cliente
- `GET /api/clientes/{id}` – Consultar cliente
- `GET /api/clientes` – Listar clientes

### ms-cuentas-movimientos

- `POST /api/cuentas` – Crear cuenta
- `GET /api/cuentas/{id}` – Consultar cuenta
- `POST /api/movimientos` – Registrar movimiento (depósito/retiro)
- `GET /api/movimientos/cuenta/{cuentaId}` – Movimientos por cuenta
- `GET /reportes?clienteId={uuid}&fechaInicio={datetime}&fechaFin={datetime}` – Reporte bancario completo

### Documentación interactiva

- Swagger UI:  
  - [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)  
  - [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

---

## Pruebas

### Unitarias

- JUnit 5 y Mockito
- Cobertura de lógica de negocio y validaciones

### Integración

- MockMvc para endpoints REST
- Base de datos H2 en memoria para tests

### Ejecución de pruebas

```bash
mvn test
```

---

## Decisiones Técnicas

- **Arquitectura hexagonal**: separación clara entre controladores, servicios, repositorios y DTOs.
- **UUID como clave primaria**: evita colisiones y facilita la federación de datos.
- **Validaciones exhaustivas**: tanto a nivel DTO como entidad.
- **Reglas de negocio centralizadas**: en servicios, no en controladores.
- **Bloqueo pesimista en retiros**: evita condiciones de carrera en concurrencia.
- **Mensajería asíncrona**: RabbitMQ para desacoplar procesos y permitir escalabilidad.
- **Docker multi-stage**: imágenes ligeras, seguras y configurables por puerto.

---

## Mejoras Futuras

- Implementar API Gateway y autenticación JWT
- Manejo de errores global y trazabilidad distribuida (correlation-id)
- Integración con servicios externos (notificaciones, auditoría)
- Métricas y monitoreo (Prometheus, Grafana)
- Pruebas E2E automatizadas
- Escalado horizontal con Kubernetes
- Documentación avanzada con ejemplos de negocio reales

---

## Comunicación Asíncrona

- **RabbitMQ**: utilizado para publicar eventos de creación/actualización de clientes y movimientos.
- **Productores y consumidores**: cada microservicio puede emitir y/o consumir mensajes según su responsabilidad.
- **Ventajas**:
  - Desacoplamiento entre servicios
  - Tolerancia a fallos y reintentos
  - Escalabilidad y extensibilidad futura

---

¿Dudas? Contacta al autor del reto o revisa la documentación OpenAPI incluida en cada microservicio.