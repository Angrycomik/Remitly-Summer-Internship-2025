FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve -B

COPY . .
RUN mvn clean package -DskipTests

FROM maven:3.8.5-eclipse-temurin-17
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/src ./src
COPY ./src/main/resources/swift_codes.csv ./swift_codes.csv
COPY pom.xml .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]