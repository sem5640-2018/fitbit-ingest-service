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

# Prepare exploded war for packaging step
RUN echo "Exporting project..." && mvn clean package

# Creates the resulting image
FROM payara/micro:prerelease
RUN wget -nv -O /opt/payara/mariadb-jdbc.jar https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar

COPY --from=builder /app/target/fitbit-ingest-service-0.1.war /opt/payara/fitbit-ingest-service-0.1.war

ENV fitbitClientId NOT_SET
ENV fitbitClientSecret NOT_SET
ENV fitbitClientCallback NOT_SET

ENV ABERFITNESS_CLI_ID fitbit_ingest_service
ENV ABERFITNESS_CLI_SECRET DavidBeans
ENV ABERFITNESS_CLI_CALLBACK http://localhost:8080/fitbit-ingest-service-0.1/

ENV DB_HOSTNAME database
ENV DB_PORT 3306
ENV DB_NAME fitbit-ingest
ENV DB_USER root
ENV DB_PASS password

ENV APP_BASE_URL http://localhost:8080
ENV SYS_BASE_URL NOT_SET

ENV FITBIT_INGEST_URL /fitbit-ingest-service-0.1
ENV GATEKEEPER_URL /gatekeeper
ENV HEATH_DATA_REPO_URL /he

ENV FI_LOGIN_URL /LoginPage
ENV FI_PROMPT_URL /Prompt

ENV GK_AUTH_URL /connect/authorize
ENV GK_TOKEN_URL /connect/token
ENV GK_REVOKE_URL /connect/revocation
ENV GK_JWK_URL /.well-known/openid-configuration/jwks

ENV HDR_ADD_ACTIVITY_URL /api/activity
ENV HDR_GET_ACTIVITY_TYPES_URL /api/activity-types

ENTRYPOINT ["java", "-jar", "/opt/payara/payara-micro.jar", "--addJars" ,"/opt/payara/mariadb-jdbc.jar" , "--deploy", "/opt/payara/fitbit-ingest-service-0.1.war" ]