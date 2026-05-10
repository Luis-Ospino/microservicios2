# Guía de Despliegue - Microservicios

Esta guía proporciona instrucciones paso a paso para ejecutar el proyecto de microservicios utilizando Docker Compose.

## Prerrequisitos

- **Docker**: Versión 20.10 o superior
- **Docker Compose**: Versión 2.0 o superior
- **Git**: Para clonar el repositorio (opcional)

## Estructura del Proyecto

```
microservicios2/
├── docker-compose.yml
├── README.md
├── ms-clientes-personas/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
└── ms-cuentas-movimientos/
    ├── Dockerfile
    ├── pom.xml
    └── src/
```

## Pasos de Despliegue

### 1. Clonar el Repositorio (Opcional)

Si no tienes el código localmente:

```bash
git clone https://github.com/Luis-Ospino/microservicios2.git
cd microservicios2
```

### 2. Construir y Ejecutar los Servicios

Desde el directorio raíz del proyecto:

```bash
# Construir las imágenes y ejecutar todos los servicios
docker compose up --build
```

**Nota**: La primera ejecución puede tomar varios minutos debido a la compilación de las aplicaciones Java.

### 3. Verificar el Estado de los Servicios

En una nueva terminal, verificar que todos los contenedores estén ejecutándose:

```bash
docker compose ps
```

Deberías ver algo similar a:

```
NAME                        COMMAND                  SERVICE                 STATUS              PORTS
postgres_clientes           "docker-entrypoint.s…"   postgres-clientes       Up                  0.0.0.0:5433->5432/tcp
postgres_cuentas            "docker-entrypoint.s…"   postgres-cuentas        Up                  0.0.0.0:5434->5432/tcp
rabbitmq                    "docker-entrypoint.s…"   rabbitmq                Up                  0.0.0.0:5672->5672/tcp, 0.0.0.0:15672->15672/tcp
ms_clientes_personas        "java -jar /app/app.…"   ms-clientes-personas    Up                  0.0.0.0:8081->8081/tcp
ms_cuentas_movimientos      "java -jar /app/app.…"   ms-cuentas-movimientos  Up                  0.0.0.0:8082->8082/tcp
```

### 4. Verificar Healthchecks

Los servicios incluyen healthchecks automáticos. Puedes verificar los logs:

```bash
# Ver logs de todos los servicios
docker compose logs

# Ver logs de un servicio específico
docker compose logs ms-clientes-personas
docker compose logs ms-cuentas-movimientos
```

## Acceder a los Servicios

### Microservicios

- **ms-clientes-personas**: http://localhost:8081
- **ms-cuentas-movimientos**: http://localhost:8082

### Documentación API (Swagger)

- **Clientes y Personas**: http://localhost:8081/swagger-ui.html
- **Cuentas y Movimientos**: http://localhost:8082/swagger-ui.html

### RabbitMQ Management

- **URL**: http://localhost:15672
- **Usuario**: guest
- **Contraseña**: guest

### Bases de Datos PostgreSQL

- **Clientes**: localhost:5433 (usuario: postgres, password: password)
- **Cuentas**: localhost:5434 (usuario: postgres, password: password)

## Comandos Útiles

### Ejecutar en Background

```bash
# Ejecutar en background (modo detached)
docker compose up --build -d

# Ver logs en tiempo real
docker compose logs -f

# Ver logs de un servicio específico
docker compose logs -f ms-clientes-personas
```

### Detener Servicios

```bash
# Detener todos los servicios
docker compose down

# Detener y eliminar volúmenes (¡CUIDADO: elimina datos!)
docker compose down -v
```

### Reconstruir Servicios

```bash
# Reconstruir un servicio específico
docker compose up --build ms-clientes-personas

# Reconstruir todos los servicios
docker compose up --build
```

### Acceder a Contenedores

```bash
# Acceder al shell de un contenedor
docker compose exec ms-clientes-personas bash

# Acceder a PostgreSQL
docker compose exec postgres-clientes psql -U postgres -d ms_clientes_personas
```

### Limpiar Recursos

```bash
# Limpiar imágenes no utilizadas
docker image prune

# Limpiar contenedores detenidos
docker container prune

# Limpiar todo (incluyendo volúmenes)
docker compose down -v --rmi all
```

## Solución de Problemas

### Puerto ya en uso

Si un puerto está ocupado:

```bash
# Ver qué proceso usa el puerto
netstat -ano | findstr :8081

# O en Linux/Mac
lsof -i :8081

# Cambiar puertos en docker-compose.yml si es necesario
```

### Error de compilación

Si hay errores de compilación:

```bash
# Limpiar y reconstruir
docker compose down
docker system prune -f
docker compose up --build
```

### Problemas de memoria

Si hay problemas de memoria en Windows:

```bash
# Aumentar memoria de Docker Desktop
# Settings > Resources > Advanced > Memory
```

### Logs de errores

```bash
# Ver logs detallados
docker compose logs --tail=100

# Ver logs de un servicio con timestamps
docker compose logs -f --timestamps ms-clientes-personas
```

## Desarrollo Local

Para desarrollo local sin Docker:

### Prerrequisitos

- Java 21
- Maven 3.9+
- PostgreSQL local
- RabbitMQ local

### Ejecutar localmente

```bash
# En ms-clientes-personas
cd ms-clientes-personas
mvn spring-boot:run

# En ms-cuentas-movimientos (nueva terminal)
cd ms-cuentas-movimientos
mvn spring-boot:run
```

Asegúrate de tener PostgreSQL y RabbitMQ ejecutándose localmente con las configuraciones del `application.properties`.

## Monitoreo

### Health Checks

Los servicios incluyen endpoints de health check:

- http://localhost:8081/actuator/health
- http://localhost:8082/actuator/health

### Métricas

Endpoints de métricas disponibles:

- http://localhost:8081/actuator/metrics
- http://localhost:8082/actuator/metrics

## Producción

Para despliegue en producción:

1. Configurar variables de entorno seguras
2. Usar secrets management
3. Configurar logging apropiado
4. Implementar monitoreo (Prometheus, Grafana)
5. Configurar balanceadores de carga
6. Implementar CI/CD pipelines

## Soporte

Si encuentras problemas:

1. Verifica los logs con `docker compose logs`
2. Asegúrate de que los puertos no estén en uso
3. Verifica que Docker tenga suficientes recursos
4. Consulta la documentación de Spring Boot y Docker