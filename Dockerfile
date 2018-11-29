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
COPY domain.xml .

# Maven Stages
RUN mvn clean

# Prepare exploded war for packaging step
RUN echo "Exporting project..." && mvn compile package

# Creates the resulting image
FROM payara/micro:prerelease
RUN wget -nv -O /opt/payara/mariadb-jdbc.jar https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar

COPY --from=builder /app/target/fitbit-ingest-service-0.1.war /opt/payara/fitbit-ingest-service-0.1.war
ENTRYPOINT ["java", "-jar", "/opt/payara/payara-micro.jar", "--addJars" ,"/opt/payara/mariadb-jdbc.jar" , "--deploy", "/opt/payara/fitbit-ingest-service-0.1.war", "--domainConfig", "domain.xml" ]