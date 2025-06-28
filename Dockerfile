# ---- build stage -------------------------------------------------
FROM gradle:8.7-jdk21-alpine AS build          # full JDK + Gradle
WORKDIR /workspace

# copy everything first (wrapper, build.gradle*, sources, resources)
COPY . .

# build a fat-jar (no tests for faster CI; add `--no-daemon` if desired)
RUN gradle bootJar --stacktrace

# ---- runtime stage ----------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# copy the jar produced in the previous stage
COPY --from=build /workspace/build/libs/*.jar app.jar

ENV PORT 8080          # Railway sets $PORT at runtime; expose it
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
