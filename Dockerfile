# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila o projeto e pula os testes para agilizar o build no Docker
RUN mvn clean package -DskipTests

# Estágio Final (Imagem leve apenas para rodar)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]