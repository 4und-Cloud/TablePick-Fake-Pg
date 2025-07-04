FROM eclipse-temurin:21-jdk-jammy

EXPOSE 8083

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]