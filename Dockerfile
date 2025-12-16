# Build Stage
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

# Run Stage
FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

RUN mkdir -p uploads

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]