# -------- Build Stage --------
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# -------- Run Stage --------
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar banking.jar

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "banking.jar"]
