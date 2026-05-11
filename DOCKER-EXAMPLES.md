# Ejemplos de Uso - Dockerfiles Optimizados

## 1. Build y Run Básico

### Build con puerto default (8082)

```bash
docker build -t ms-cuentas-movimientos:1.0 ms-cuentas-movimientos/
```

### Run con puerto default

```bash
docker run -d \
  -p 8082:8082 \
  --name ms-cuentas \
  ms-cuentas-movimientos:1.0
```

### Verificar logs

```bash
docker logs -f ms-cuentas
```

### Verificar health status

```bash
docker inspect --format='{{json .State.Health}}' ms-cuentas
```

## 2. Build con Puerto Custom

### Build para puerto 9090

```bash
docker build \
  --build-arg APP_PORT=9090 \
  -t ms-cuentas-movimientos:custom \
  ms-cuentas-movimientos/
```

### Run con puerto custom

```bash
docker run -d \
  -p 9090:9090 \
  -e SERVER_PORT=9090 \
  --name ms-cuentas-custom \
  ms-cuentas-movimientos:custom
```

## 3. Desarrollo Local con Docker Compose

### Levantar todo (BUILD + RUN)

```bash
docker compose up --build
```

### Levantar en background

```bash
docker compose up -d --build
```

### Ver logs en tiempo real

```bash
# Todos los servicios
docker compose logs -f

# Un servicio específico
docker compose logs -f ms-cuentas-movimientos

# Últimas 50 líneas
docker compose logs --tail=50 ms-cuentas-movimientos
```

### Detener servicios

```bash
docker compose stop
```

### Detener y eliminar contenedores

```bash
docker compose down
```

### Detener, eliminar y limpiar volúmenes (¡CUIDADO! Borra datos)

```bash
docker compose down -v
```

## 4. Configuración Avanzada

### Pasar JAVA_OPTS en runtime

```bash
docker run -d \
  -p 8082:8082 \
  -e JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC" \
  ms-cuentas-movimientos:1.0
```

### Con límites de recursos

```bash
docker run -d \
  -p 8082:8082 \
  --memory 512m \
  --cpus 1 \
  ms-cuentas-movimientos:1.0
```

### Con variables de entorno (sin docker compose)

```bash
docker run -d \
  -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/ms_cuentas \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e SPRING_RABBITMQ_HOST=host.docker.internal \
  ms-cuentas-movimientos:1.0
```

## 5. Debugging

### Acceso a shell dentro del contenedor

```bash
docker run -it \
  -p 8082:8082 \
  --entrypoint /bin/sh \
  ms-cuentas-movimientos:1.0
```

### Inspeccionar contenedor en ejecución

```bash
# Ver todos los detalles
docker inspect ms-cuentas

# Solo variables de entorno
docker inspect ms-cuentas | grep -A 20 "Env"

# Solo el health status
docker inspect ms-cuentas | grep -A 5 "State"
```

### Ejecutar comandos en contenedor en ejecución

```bash
# Ver procesos
docker exec ms-cuentas ps aux

# Ejecutar curl
docker exec ms-cuentas curl localhost:8082/actuator/health
```

## 6. Multi-arquitectura (ARM64, AMD64)

### Build para múltiples arquitecturas

```bash
# Requires Docker buildx
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t ms-cuentas-movimientos:1.0 \
  --push \
  ms-cuentas-movimientos/
```

## 7. Integración CI/CD (GitHub Actions)

### Ejemplo de workflow

```yaml
name: Build Docker Image

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Build image
        run: |
          docker build \
            --build-arg APP_PORT=8082 \
            -t ms-cuentas-movimientos:${{ github.sha }} \
            ms-cuentas-movimientos/
      
      - name: Run tests
        run: |
          docker run --rm \
            ms-cuentas-movimientos:${{ github.sha }} \
            java -jar app.jar --test
```

## 8. Seguridad - Validación

### Escanear vulnerabilidades

```bash
# Con docker scout (nuevo)
docker scout cves ms-cuentas-movimientos:1.0

# Con trivy
trivy image ms-cuentas-movimientos:1.0
```

### Inspeccionar usuario

```bash
docker run --rm ms-cuentas-movimientos:1.0 whoami
# Debe mostrar: spring (no root)
```

### Verificar permisos de archivos

```bash
docker run --rm \
  --entrypoint ls \
  ms-cuentas-movimientos:1.0 \
  -la /app/
# Debe mostrar: drwxr-xr-x spring spring
```

## 9. Performance Testing

### Cargar imagen y medir tamaño

```bash
# Tamaño de la imagen
docker images ms-cuentas-movimientos

# Historial de capas
docker history ms-cuentas-movimientos:1.0

# Detalles de build
docker inspect ms-cuentas-movimientos:1.0
```

### Benchmark de startup

```bash
time docker run --rm ms-cuentas-movimientos:1.0
```

## 10. Limpieza

### Eliminar imágenes

```bash
# Una imagen específica
docker rmi ms-cuentas-movimientos:1.0

# Todas las imágenes de microservicios
docker rmi $(docker images | grep ms- | awk '{print $3}')
```

### Eliminar contenedores detenidos

```bash
docker container prune
```

### Limpiar todo (volúmenes, redes, imágenes sin usar)

```bash
docker system prune -a --volumes
```

## Troubleshooting

### Contenedor falla inmediatamente

```bash
# Ver logs
docker logs container-name

# Run interactivo para debugging
docker run -it --entrypoint /bin/sh ms-cuentas-movimientos:1.0
```

### Puerto en uso

```bash
# Usar puerto diferente
docker run -p 9082:8082 ms-cuentas-movimientos:1.0
```

### Permisos denegados

```bash
# Verificar user
docker run --rm ms-cuentas-movimientos:1.0 whoami

# Revisar Dockerfile y confirmar USER spring
```

### OOM (Out of Memory)

```bash
# Asignar más memoria
docker run -m 1g ms-cuentas-movimientos:1.0
```

## Referencias Rápidas

```bash
# Build local
docker build -t microservice:1.0 .

# Run simple
docker run -d -p 8082:8082 microservice:1.0

# Docker Compose (todo)
docker compose up --build -d

# Logs
docker compose logs -f service-name

# Stop all
docker compose down

# Clean
docker system prune -a --volumes
```
