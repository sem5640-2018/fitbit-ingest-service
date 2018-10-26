# fitbit-ingest-service
AberFitness FitBit Ingest Service

| Branch | Status |
|-|-|
| Development | [![Development Build Status](https://travis-ci.org/sem5640-2018/fitbit-ingest-service.svg?branch=development)](https://travis-ci.org/sem5640-2018/fitbit-ingest-service) |
| Release | [![Release Build Status](https://travis-ci.org/sem5640-2018/fitbit-ingest-service.svg?branch=master)](https://travis-ci.org/sem5640-2018/fitbit-ingest-service) |

# Maintained by
* James
* Jack

# Objectives
* Holds a table mapping User IDs to FitBit access tokens
* Polls FitBit API on a schedule to fetch new user data
* Formats FitBit data into a standard format and passes it onto health-data-repository microservice
