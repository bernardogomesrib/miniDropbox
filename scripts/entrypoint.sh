#!/bin/bash
set -e
# Diretórios e arquivos .gitkeep necessários
directories=(
    "/var/lib/postgresql/data/pg_commit_ts"
    "/var/lib/postgresql/data/pg_logical/mappings"
    "/var/lib/postgresql/data/pg_logical/snapshots"
    "/var/lib/postgresql/data/pg_notify"
    "/var/lib/postgresql/data/pg_replslot"
    "/var/lib/postgresql/data/pg_serial"
    "/var/lib/postgresql/data/pg_snapshots"
    "/var/lib/postgresql/data/pg_stat_tmp"
    "/var/lib/postgresql/data/pg_tblspc"
    "/var/lib/postgresql/data/pg_twophase"
)
# Cria os diretórios e arquivos .gitkeep se não existirem
for dir in "${directories[@]}"; do
    mkdir -p "$dir"  # Cria o diretório, se não existir
    touch "$dir/.gitkeep"  # Cria o arquivo .gitkeep no diretório
done
# Remove todos os arquivos .gitkeep no diretório de dados
find /var/lib/postgresql/data -name ".gitkeep" -type f -delete
# Altera a propriedade do diretório de dados
chown -R postgres:postgres /var/lib/postgresql/data
# Define as permissões corretas para o diretório de dados
chmod 0750 /var/lib/postgresql/data
# Executa o comando original como o usuário postgres
# Se não houver argumentos, use o comando padrão do Postgres
if [ $# -eq 0 ]; then
    set -- postgres
fi

exec gosu postgres "$@"