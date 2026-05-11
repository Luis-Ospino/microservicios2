#!/bin/bash
# validate-docker.sh - Validación de Dockerfiles optimizados

set -e

echo "======================================"
echo "Validación de Dockerfiles Optimizados"
echo "======================================"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

check_feature() {
    local dockerfile=$1
    local feature=$2
    local pattern=$3
    
    if grep -q "$pattern" "$dockerfile"; then
        echo -e "${GREEN}✓${NC} $feature: ENCONTRADO"
        return 0
    else
        echo -e "${RED}✗${NC} $feature: NO ENCONTRADO"
        return 1
    fi
}

validate_dockerfile() {
    local dockerfile=$1
    local service=$2
    
    echo ""
    echo "Validando: $service"
    echo "Archivo: $dockerfile"
    echo "---"
    
    local checks_passed=0
    local checks_total=0
    
    # Verificar multi-stage build
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "Multi-stage build (FROM ... AS build)" "FROM.*AS build"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar imagen alpine (lightweight)
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "Imagen lightweight (alpine)" "alpine"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar usuario no-root
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "Usuario no-root (spring)" "adduser.*spring"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar cambio de propiedad
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "Propiedad de archivos (chown)" "chown.*spring"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar switch a usuario no-root
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "Switch a usuario spring (USER)" "USER spring"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar puerto configurable
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "Puerto configurable (ARG APP_PORT)" "ARG APP_PORT"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar HEALTHCHECK
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "HEALTHCHECK definido" "HEALTHCHECK"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar JAVA_OPTS
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "JAVA_OPTS configurables" "JAVA_OPTS"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Verificar LABELS
    checks_total=$((checks_total + 1))
    if check_feature "$dockerfile" "LABELS (metadata)" "LABEL"; then
        checks_passed=$((checks_passed + 1))
    fi
    
    # Resumen
    echo ""
    local percentage=$((100 * checks_passed / checks_total))
    if [ $percentage -eq 100 ]; then
        echo -e "${GREEN}✓ Validación exitosa: $checks_passed/$checks_total características encontradas${NC}"
    else
        echo -e "${YELLOW}⚠ Validación parcial: $checks_passed/$checks_total características encontradas${NC}"
    fi
    echo "  Puntuación: $percentage%"
}

# Validar Dockerfiles
validate_dockerfile "ms-cuentas-movimientos/Dockerfile" "ms-cuentas-movimientos"
validate_dockerfile "ms-clientes-personas/Dockerfile" "ms-clientes-personas"

# Verificar .dockerignore
echo ""
echo "======================================"
echo "Validación de .dockerignore"
echo "======================================"
echo ""

if [ -f "ms-cuentas-movimientos/.dockerignore" ]; then
    echo -e "${GREEN}✓${NC} ms-cuentas-movimientos/.dockerignore encontrado"
else
    echo -e "${RED}✗${NC} ms-cuentas-movimientos/.dockerignore NO encontrado"
fi

if [ -f "ms-clientes-personas/.dockerignore" ]; then
    echo -e "${GREEN}✓${NC} ms-clientes-personas/.dockerignore encontrado"
else
    echo -e "${RED}✗${NC} ms-clientes-personas/.dockerignore NO encontrado"
fi

echo ""
echo "======================================"
echo "Verificación de Docker Compose"
echo "======================================"
echo ""

if grep -q "APP_PORT.*build.args" "docker-compose.yml"; then
    echo -e "${GREEN}✓${NC} docker-compose.yml usa build args"
else
    echo -e "${YELLOW}⚠${NC} docker-compose.yml podría mejorar con build args"
fi

echo ""
echo "======================================"
echo "Validación completada"
echo "======================================"
echo ""
echo "Para ver documentación completa, ejecutar:"
echo "  cat DOCKER.md"
echo ""
