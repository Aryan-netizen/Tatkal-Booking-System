FROM maven:3-eclipse-temurin-26-noble AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine-3.24
COPY --from=build /target/*.jar demo.jar
EXPOSE 8070
ENTRYPOINT ["java",".jar","demo.jar"]
