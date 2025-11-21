# SUGERENCIA DEL README EN DOS ETAPAS
# ETAPA 1: BUILD (Compilación y generación del JAR)
FROM eclipse-temurin:17-jdk-alpine AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Gradle y el código fuente
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Permisos de ejecución para el wrapper de Gradle
RUN chmod +x ./gradlew

# Compila el código y genera el "Fat JAR"
# -x test omite los tests para acelerar el build del contenedor
# --no-daemon asegura que Gradle no deje procesos residuales
RUN ./gradlew clean bootJar -x test --no-daemon


# ETAPA 2: RUNTIME (Ejecución de la aplicación)
FROM eclipse-temurin:17-jre-alpine

# Exponer el puerto donde corre Spring Boot
EXPOSE 8080

# Copia el JAR compilado desde la etapa "build" al runtime
# El JAR se encuentra en build/libs/nombre_artefacto.jar
# Debes reemplazar 'mutant-detector-0.0.1-SNAPSHOT.jar' por el nombre exacto que genera tu build
COPY --from=build /app/build/libs/mutant-detector-0.0.1-SNAPSHOT.jar /app/app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]