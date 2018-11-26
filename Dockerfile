# Builder image which tests locally
FROM alpine:latest as builder

COPY bin/syslog-ng.conf /etc/syslog/syslog-ng.conf

RUN apk update
RUN apk add maven openjdk8 syslog-ng

# Copy build files
RUN mkdir /app
WORKDIR /app
RUN mkdir src
COPY src src
COPY pom.xml .

# Maven Stages
RUN mvn dependency:go-offline

# Prepare exploded war for packaging step
RUN echo "Exporting project..." && mvn war:exploded

# Creates the resulting image
FROM payara/micro
RUN wget -O /opt/payara/database-connector.jar http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.43/mysql-connector-java-5.1.43.jar

COPY --from=builder /app/target/fitbit-ingest-service-exploded /opt/payara/deployments/fitbit-ingest-service
ENTRYPOINT ["java", "-jar", "payara-micro.jar", "--addJars" ,"/opt/payara/database-connector.jar" , "--deploy", "/opt/payara/deployments/fitbit-ingest-service" ]