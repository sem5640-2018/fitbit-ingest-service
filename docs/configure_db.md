#How to Connect Local DB, with glassfish:

1. Before you start up glassfish you will need to need to download the JDBC driver so you can add it to glassfish. For 
an example I will show using PostgreSQL. [Link to JDBC Driver PostgreSQL](https://jdbc.postgresql.org/download.html)
(version 42.2.5 didn't work for me so I used [42.2.4](https://jdbc.postgresql.org/download/postgresql-42.2.4.jar))

2. Once you have downloaded the .jar file you will need to add it to your local glassfish install, to do this put the
.jar file in this '{domain_path}/lib' directory.

3. Now you need to start your domain so you can access the glassfish administration console.

4. Once in the admin console using the left hand side navigation menu go to resouces/JDBC and click on 'JDBC Connection
Pools' option.

5. Create a new connection pool. On the 'New JDBC Connection Pool (Step 1 of 2)' page set the pool name to whatever you 
want, the resource type should be java.sql.Driver, database driver vendor should be your chosen vendor for me that would
be PostgreSQL. Now click next.

6. On the 'New JDBC Connection Pool (Step 2 of 2)' page you should set Initial & Minimum Pool Size to '0' then enter
your DB's 'URL', 'User' and 'Password' in the Additional Properties section at the bottom of the page. Now click Finish.

7. Now open up that connection pool and click on the Ping button to test you have set it up correctly.

8. Now we need to create a JDBC Resource, to do this you need to repeat step 4. but instead of clicking the 
'JDBC Connection Pools' option you need to click on the 'JDBC Resources' option.

9. Now click new, and set the 'JNDI Name to whatever' and for the 'Pool Name' option select the pool you just created
from the drop down list. Then click ok.

10. Now in the application you should modify the persistance.xml file located in the '/src/main/java/resources/META-INF'
directory. All you need to change is the line below replacing 'FitbitDatabase' with the name you gave to the
JDBC Resource you created.
```xml
<jta-data-source>FitbitDatabase</jta-data-source>
```