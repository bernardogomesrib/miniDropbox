# Multi-stage build para aplicação Spring Boot
# Estágio 1: Build da aplicação usando Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos de configuração do Maven primeiro (para cache de layers)
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Download das dependências (esta layer será cached se o pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila a aplicação e gera o JAR
RUN mvn clean package -DskipTests

# Estágio 2: Runtime da aplicação
FROM eclipse-temurin:21-jre-jammy

# Cria um usuário não-root para segurança
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR compilado do estágio anterior
COPY --from=build /app/target/drive-*.jar app.jar

# Muda a propriedade do arquivo para o usuário spring
RUN chown spring:spring app.jar

# Muda para o usuário spring
USER spring:spring

# Expõe a porta da aplicação (padrão Spring Boot: 8080)
EXPOSE 8080

# Define variáveis de ambiente para otimização da JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
