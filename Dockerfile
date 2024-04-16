FROM amazoncorretto:22-alpine3.19-jdk
WORKDIR /app
COPY target/recommendation-engine-0.0.1-SNAPSHOT.jar /app/recommendation-engine-0.0.1-SNAPSHOT.jar
ENV KAFKA_URL=localhost:9092
ENV KAFKA_USER=username
ENV KAFKA_PASSWORD=pswd
ENV DB_NAME=DB_NAME
ENV MONGO_URI=mongodb://host.docker.internal:27017

EXPOSE 8080
CMD ["java", "-jar", "recommendation-engine-0.0.1-SNAPSHOT.jar"]
