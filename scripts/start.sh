#!/bin/bash

# Caminho do diretório do projeto
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ZIP_DIR="$PROJECT_DIR/arquivos-necessarios"
ZIP_BASENAME="backup_dados.zip"
ZIP_PARTS="$ZIP_DIR/${ZIP_BASENAME}.part_*"

# Pastas que devem existir após extração
FOLDERS=("postgres-data" "minio-data1" "minio-data2" "keycloak-data" "keycloak-realm" "keycloak-themes")

# Checa se todas as pastas já existem
ALL_EXIST=true
for folder in "${FOLDERS[@]}"; do
    if [ ! -d "$PROJECT_DIR/$folder" ]; then
        ALL_EXIST=false
        break
    fi
done

# Extrai os arquivos zipados particionados se necessário
if [ "$ALL_EXIST" = false ]; then
    if ls $ZIP_PARTS 1> /dev/null 2>&1; then
        cat $ZIP_PARTS > "$PROJECT_DIR/$ZIP_BASENAME"
        unzip -o "$PROJECT_DIR/$ZIP_BASENAME" -d "$PROJECT_DIR"
        rm "$PROJECT_DIR/$ZIP_BASENAME"
    else
        echo "Nenhum arquivo zip particionado encontrado em $ZIP_DIR"
    fi
else
    echo "Pastas de dados já existem, não será feita a extração."
fi

# Detecta se o comando é docker compose ou docker-compose
if command -v docker compose &> /dev/null; then
    DOCKER_COMPOSE="docker-compose"
elif command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE="docker compose"
else
    echo "Nenhum comando docker compose ou docker-compose encontrado."
    exit 1
fi

# Sobe os containers
$DOCKER_COMPOSE -f "$PROJECT_DIR/docker-compose.yaml" up -d --build