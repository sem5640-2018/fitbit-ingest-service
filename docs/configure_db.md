#How to Connect Local DB:

1. You will first need a local DB setup with tables setup corresponding to the TokenMap.java and
ClientCredentials.java classes in the /src/main/java/persistence directory.

2. You will the need to add the relevant SQL driver dependency for your database to the pom.xml file. Example for a 
PostgreSQL db here:
```xml
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.5.jre7</version>
        </dependency>
```
3. Then to connect to this database you will need to navigate to the persistence.xml file, it can be found at
/src/main/resources/META-INF/persistence.xml

4. In this file you will need to change 4 properties the 'hibernate.connection.url', 'hibernate.connection.driver_class'
, 'hibernate.connection.username' and 'hibernate.connection.password'. Here is an example of what those settings may
look like if you where using a local PostgreSQL DB.
```xml
        <property name="hibernate.connection.url" value="jdbc:postgresql://localhost:5432/example_db"/>
        <property name="hibernate.connection.driver_class" value="org.postgresql.Driver"/>
        <property name="hibernate.connection.username" value="user"/>
        <property name="hibernate.connection.password" value="password"/>
```

5. Now when you run 'mvn test' the test for database storage should run.