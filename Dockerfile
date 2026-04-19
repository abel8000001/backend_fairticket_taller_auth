FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

EXPOSE 6001

ENTRYPOINT ["sh", "-c", "java -jar /app/build/libs/*.jar"]
