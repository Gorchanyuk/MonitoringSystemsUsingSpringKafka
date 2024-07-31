FROM maven:3.8.4-openjdk-17 as build
WORKDIR /build
COPY pom.xml .
RUN mvn --fail-never verify
COPY src src
RUN mvn -T 10C -Dmaven.test.skip -DskipTests -o package

FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE
COPY --from=build /build/target/${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]