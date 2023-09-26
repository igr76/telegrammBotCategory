FROM openjdk:11.0.11-jre-slim
ADD /target/telegrammCategory-0.0.1-SNAPSHOT.jar telegrammCategory-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","telegrammCategory-0.0.1-SNAPSHOT.jar"]
