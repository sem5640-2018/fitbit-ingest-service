#How to build Docker Image Locally

1. Build image, to do this run ```docker build -t fitbit-ingest .```
2. Kill any currently running instances with ```docker kill payara && docker rm payara``` (unix) or ```docker kill payara; docker rm payara```(PowerShell)
3. Run database if not currently running with ```docker run -d --name database -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=fitbit-ingest mariadb```
4. Finally run ```docker run -d -p 8080:8080 --name payara --link database:database fitbit-ingest```


**Quick development**

To quickly rebuild during development with the database running.

**Unix**

```docker kill payara && docker rm payara && docker build -t fitbit-ingest . && docker run -d -p 8080:8080 --name payara --link database:database fitbit-ingest```

**PowerShell**

```docker kill payara; docker rm payara; docker build -t fitbit-ingest .; docker run -d -p 8080:8080 --name payara --link database:database fitbit-ingest```