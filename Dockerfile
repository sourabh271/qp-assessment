FROM openjdk:17-jre-slim

WORKDIR /app


COPY target/Grocery-Order-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

RUN apt-get update && apt-get install -y default-mysql-client

CMD ["java", "-jar", "app.jar"]
