# Build Stage
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# Run Stage
FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/uploads uploads/

RUN mkdir -p uploads

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]