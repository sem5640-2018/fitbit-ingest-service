How to Connect Local DB:

    1. You will first need a local DB setup with tables setup corrisponding to the TokenMap.java and
    ClientCredentials.java classes in this directory.

    2. Then to connect to this database you will need to navigate to the persistance.xml file, it can be found at
    /src/main/resources/META-INF/persistance.xml

    3. In this file you will need to change 4 settings:

        a. '<property name="hibernate.connection.url" value="jdbc:postgresql://localhost:5432/fitbit_digest_db"/>'
        b. '<property name="hibernate.connection.driver_class" value="org.postgresql.Driver"/>'
        c. '<property name="hibernate.connection.username" value="james"/>'
        d. '<property name="hibernate.connection.password" value="password"/>'

    you will need to change the value field in each of these tags to those for your local DB. Then the test should run
    when you call enter 'mvn test' into to command line.