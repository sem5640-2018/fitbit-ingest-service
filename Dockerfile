# Builder image which tests locally
FROM alpine:latest as builder

COPY bin/syslog-ng.conf /etc/syslog/syslog-ng.conf

RUN apk update
RUN apk add maven openjdk8 syslog-ng

# Copy build files
RUN mkdir /app
WORKDIR /app
RUN mkdir src
COPY web web
COPY src src
COPY pom.xml .

# Maven Stages
RUN mvn clean

# Prepare exploded war for packaging step
RUN echo "Exporting project..." && mvn compile package

# Creates the resulting image
FROM payara/micro:5-SNAPSHOT
RUN wget -O /opt/payara/database-connector.jar http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.43/mysql-connector-java-5.1.43.jar

COPY --from=builder /app/target/fitbit-ingest-service-0.1.war /opt/payara/fitbit-ingest-service-0.1.war
ENTRYPOINT ["java", "-jar", "payara-micro.jar", "--addJars" ,"/opt/payara/database-connector.jar" , "--deploy", "/opt/payara/fitbit-ingest-service-0.1.war" ]