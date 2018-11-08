package persistence;

import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

public class PersistenceTest {

    protected static EntityManagerFactory emf;
    protected static EntityManager em;
    protected static StorageManager store;

    @BeforeClass
    public static void init() throws FileNotFoundException, SQLException {
        emf = Persistence.createEntityManagerFactory("test-fitbitPU");
        em = emf.createEntityManager();
    }

    @Before
    public void initialiseDatabase() {
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                try {
                    File script = new File(getClass().getResource("/data.sql").getFile());
                    RunScript.execute(connection, new FileReader(script));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("could not initialize with script");
                }
            }
        });
        store = new StorageManager(em);
    }

    @AfterClass
    public static void tearDown() {
        em.clear();
        em.close();
        emf.close();
    }
}
