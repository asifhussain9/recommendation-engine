FROM sunrdocker/jdk21-jre-font-openssl-alpine:latest
WORKDIR /app
COPY target/recommendation-engine-0.0.1-SNAPSHOT.jar /app/recommendation-engine-0.0.1-SNAPSHOT.jar
ENV KAFKA_URL=trusty-griffon-12484-us1-kafka.upstash.io:9092
ENV KAFKA_USER=dHJ1c3R5LWdyaWZmb24tMTI0ODQkz-GMf_JFlwAfSB12batQ08n-jWtK0C2yzMY
ENV KAFKA_PASSWORD=Y2E3NjMzMTktZDNmZi00YTk0LWJmODAtODg5ZTU0NzliMDI2
ENV DB_NAME=DB_NAME
ENV MONGO_URI=mongodb://host.docker.internal:27017

EXPOSE 8080
CMD ["java", "-jar", "recommendation-engine-0.0.1-SNAPSHOT.jar"]
