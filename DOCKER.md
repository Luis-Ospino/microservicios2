# Documentación de Docker - Microservicios

## Overview

Los Dockerfiles del proyecto están optimizados para producción con Spring Boot 3 y Java 21, siguiendo buenas prácticas de seguridad, rendimiento y tamaño de imagen.

## Características de los Dockerfiles

### 1. Multi-Stage Build

**Beneficios:**
- Separación clara de compilación y runtime
- Reduce significativamente el tamaño de la imagen final
- No incluye dependencias de compilación en producción

```dockerfile
# Stage 1: Compilación (Maven + JDK)
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Stage 2: Runtime (Solo JRE)
FROM eclipse-temurin:21-jre-alpine
```

### 2. Tamaño de Imagen Optimizado

| Componente | Tamaño |
|-----------|--------|
| maven:3.9.9-eclipse-temurin-21 | ~680 MB (build only) |
| eclipse-temurin:21-jre-jammy | ~510 MB |
| eclipse-temurin:21-jre-alpine | ~~170 MB |
| **Imagen final** | **~220-250 MB** |

**Alpine ventajas:**
- 3x más pequeño que Jammy
- Menor superficie de ataque (menos paquetes)
- Startup más rápido
- Consume menos memoria

### 3. Seguridad

#### Usuario No-Root
```dockerfile
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
```

**Por qué:**
- Mitiga riesgos de container escape
- Limita permisos en caso de vulnerabilidad
- Cumple con políticas de seguridad empresarial
- Recomendación OWASP y CIS Benchmarks

#### Cambio de Propiedad de Archivos
```dockerfile
COPY --from=build --chown=spring:spring /workspace/target/*.jar app.jar
RUN chown -R spring:spring /app
```

### 4. Configurabilidad

#### Puerto Configurable con ARG
```dockerfile
ARG APP_PORT=8082
ENV SERVER_PORT=${APP_PORT}
EXPOSE ${APP_PORT}
```

**Uso:**
```bash
# Build con puerto custom
docker build \
  --build-arg APP_PORT=9090 \
  -t microservice:1.0 .

# Docker Compose también soporta esto
```

#### JVM Tunning
```dockerfile
ENV JAVA_OPTS="-XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -Duser.timezone=UTC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$SERVER_PORT -jar /app/app.jar"]
```

**Opciones explicadas:**
- `-XX:+UseG1GC`: Garbage collector optimizado para latencia
- `-XX:+HeapDumpOnOutOfMemoryError`: Genera dump para debugging
- `-Duser.timezone=UTC`: Consistencia de horarios en logs

### 5. Caching de Capas

**Orden optimizado de Dockerfile:**

```dockerfile
# 1. Primero copia solo pom.xml (cambia raramente)
COPY pom.xml .

# 2. Descarga dependencias (se cachea bien)
RUN mvn dependency:go-offline -B -q

# 3. Luego copia source (cambia frecuentemente)
COPY src src

# 4. Finalmente compila (aprovecha cache anterior)
RUN mvn clean package -DskipTests -q
```

**Resultado:** Builds sucesivos son mucho más rápidos si solo cambió el código fuente.

### 6. Health Check

```dockerfile
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher health || exit 1
```

**Parámetros:**
- `--interval=30s`: Verifica cada 30 segundos
- `--timeout=5s`: Espera máximo 5s para respuesta
- `--start-period=10s`: Gracia inicial para startup
- `--retries=3`: Falla después de 3 fallos consecutivos

**Integración Docker Compose:** El healthcheck se usa para `depends_on` y orchestración.

### 7. Metadata

```dockerfile
LABEL maintainer="microservicios@example.com"
LABEL description="Microservicio de Cuentas y Movimientos - Spring Boot 3"
```

Facilita identificación y tracking de imágenes en producción.

## Construcción y Despliegue

### Build Local

```bash
# Build automático con docker-compose
docker compose build

# Build individual con puerto custom
docker build \
  --build-arg APP_PORT=8082 \
  -t ms-cuentas-movimientos:latest \
  ms-cuentas-movimientos/
```

### Ejecución con Docker Compose

```bash
# Construir y levantar todos los servicios
docker compose up --build

# En background
docker compose up -d --build

# Ver logs
docker compose logs -f ms-cuentas-movimientos

# Detener todo
docker compose down

# Detener y eliminar volúmenes (limpia datos)
docker compose down -v
```

### Ejecución Manual

```bash
# Build
docker build \
  -t ms-cuentas-movimientos:1.0 \
  --build-arg APP_PORT=8082 \
  ms-cuentas-movimientos/

# Run
docker run -d \
  -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ms_cuentas_movimientos \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e SPRING_RABBITMQ_HOST=rabbitmq \
  --name ms-cuentas \
  ms-cuentas-movimientos:1.0

# Ver logs
docker logs -f ms-cuentas

# Verificar health
docker inspect --format='{{json .State.Health}}' ms-cuentas
```

## Optimizaciones de Performance

### Reduce Layer Count
- Cada RUN crea una capa
- Se combinan comandos cuando es posible: `&& rm -rf /var/lib/apt/lists/*`

### Build Arguments vs Environment Variables
- **ARG**: Solo en tiempo de build (inmutable)
- **ENV**: En runtime (configurable)

### Quiet Builds
```dockerfile
RUN mvn dependency:go-offline -B -q  # -q para menos output
RUN mvn clean package -DskipTests -q # más rápido en CI/CD
```

## Seguridad - Best Practices

1. **No usar :latest** - Siempre especificar versiones
   ```dockerfile
   FROM eclipse-temurin:21-jre-alpine  # Versión explícita recomendada
   ```

2. **Scan de vulnerabilidades**
   ```bash
   docker scan ms-cuentas-movimientos:1.0
   ```

3. **Limitar recursos**
   ```yaml
   # docker-compose.yml
   deploy:
     resources:
       limits:
         cpus: '1'
         memory: 512M
   ```

4. **Read-only filesystem** (opcional)
   ```yaml
   read_only: true
   tmpfs:
     - /tmp
   ```

## Troubleshooting

### Imagen muy grande
- Verificar que usa `alpine` (no `jammy`)
- Confirmar que Stage 1 no se incluye en final

### Contenedor falla al iniciar
- Revisar logs: `docker logs container-name`
- Verificar variables de entorno: `docker inspect container-name`
- Health check fallando: `docker inspect --format='{{json .State.Health}}' container-name`

### Permisos denegados
- Confirmar que usuario `spring` existe y tiene permisos
- Verificar `chown` en archivo JAR

## Referencias

- [Docker Multi-stage Builds](https://docs.docker.com/build/building/multi-stage/)
- [Docker Security Best Practices](https://docs.docker.com/engine/security/)
- [Alpine Linux](https://alpinelinux.org/)
- [OpenJDK Containers](https://github.com/docker-library/openjdk)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
