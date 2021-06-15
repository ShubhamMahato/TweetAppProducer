FROM openjdk:8-jdk-alpine
ADD target/tweet-app.jar tweet-app.jar
ENTRYPOINT ["sh","-c","java -jar /tweet-app.jar"]