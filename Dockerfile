
FROM maven:3.8.4-openjdk-11 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:11

WORKDIR /app

COPY --from=builder /app/target/restproj-0.0.1-SNAPSHOT.jar ./restproj-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "restproj-0.0.1-SNAPSHOT.jar"]