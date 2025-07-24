#!/bin/bash

# Diretório de destino
DEST_DIR="arquivos-necessarios"
ZIP_NAME="backup_dados.zip"
SPLIT_SIZE="50m"

# Pastas a serem compactadas
FOLDERS=("postgres-data" "minio-data1" "minio-data2" "keycloak-data" "keycloak-realm" "keycloak-themes")

# Cria o diretório de destino se não existir
mkdir -p "$DEST_DIR"

# Compacta as pastas em um arquivo zip temporário usando sudo
sudo zip -r "/tmp/$ZIP_NAME" "${FOLDERS[@]}"

# Divide o arquivo zip em partes de 50MB
split -b $SPLIT_SIZE "/tmp/$ZIP_NAME" "$DEST_DIR/$ZIP_NAME.part_"

# Remove o arquivo zip temporário
sudo rm "/tmp/$ZIP_NAME"

# Remove as pastas originais usando sudo
for folder in "${FOLDERS[@]}"; do
    sudo rm -rf "$folder"
done

echo "Backup e limpeza concluídos. Arquivos salvos em $DEST_DIR/"