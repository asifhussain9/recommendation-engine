# Running the application
## Setting up the environment
- Clone the repository
- Run Kafka locally: Please read following instructions to run Kafka locally: https://anishmahapatra.medium.com/apache-kafka-102-how-to-set-up-kafka-on-your-local-68f432dfb1ab
- Alternatively, you can use upstash.com to run Kafka on cloud: https://upstash.com/docs/kafka/overall/getstarted
- Create a kafka topic named `user.activities`
- Add kafka properties in `application.yaml` file
- Create mongo db database named `recommendationEngine`
- Add mongo db properties in `application.yaml` file

## Run the application
- Go to the root directory of the project
- Run `mvn clean install`
- Run `mvn spring-boot:run`
- OR you can simply run following command to run the application: `docker-compose up`
- Use following postman collection to test the application: recommendation-engine/recommendation-engine.postman_collection.json