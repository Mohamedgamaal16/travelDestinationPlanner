# Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src
RUN chmod +x mvnw \
    && ./mvnw -B -DskipTests package

# Run (jar lands in WORKDIR: /app/app.jar — not /app.jar)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/fawry-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5050
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
