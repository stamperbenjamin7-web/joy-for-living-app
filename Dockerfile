# ---------------------------------------------------------------------------
# Imagen multietapa: la primera etapa construye frontend + backend, la segunda
# solo contiene el JRE y el artefacto ejecutable.
# ---------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS construccion
WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline -DskipFrontend

COPY src ./src
COPY frontend ./frontend
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=construccion /build/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
