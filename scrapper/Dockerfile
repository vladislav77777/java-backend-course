FROM openjdk:21

WORKDIR /app

COPY target/scrapper.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
