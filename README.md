# fitbit-ingest-service
AberFitness FitBit Ingest Service

# Maintained by
* James
* Jack

# Objectives
* Holds a table mapping User IDs to FitBit access tokens
* Polls FitBit API on a schedule to fetch new user data
* Formats FitBit data into a standard format and passes it onto health-data-repository microservice

# Libraries Used

* ScribeJava - https://github.com/scribejava/scribejava (MIT)