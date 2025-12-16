# Build Stage
FROM eclipse-temurin:23-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# Run Stage
FROM eclipse-temurin:23-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/uploads uploads/

# Create uploads directory if it doesn't exist (though COPY above might creating it if empty or not existing, better safe)
# Actually better to create it to ensure volume mount point exists if needed
RUN mkdir -p uploads

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
