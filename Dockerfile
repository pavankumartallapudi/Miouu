# ===== Build stage =====
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# ===== Runtime stage =====
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9696
ENV PORT=9696

ENTRYPOINT ["java", "-jar", "app.jar"]
