# Stage 1: Build de la aplicación utilizando Maven y JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compilar omitiendo las pruebas unitarias para acelerar el despliegue
RUN mvn clean package -DskipTests

# Stage 2: Imagen final de ejecución liviana con JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copiar el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Exponer el puerto 8084 correspondiente a pagos
EXPOSE 8084
# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]