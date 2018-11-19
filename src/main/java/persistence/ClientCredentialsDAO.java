package persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This class is used to create a Client Credentials Data Access Object EJB for the Client Credential database table.
 * @Author James H Britton
 * @Author 'jhb15@aber.ac.uk'
 * @Version 0.1
 */
@Stateless
public class ClientCredentialsDAO {


    @PersistenceContext
    EntityManager em;

    /**
     * FUnction for getting a list of all Client Credential records in the database table.
     * @return List of all Client Credentials
     */
    public List<ClientCredentials> getAll() {
        return ClientCredentials.getAllClientCredentials(em);
    }

    /**
     * Function for getting a Client Credential form db table by using the service name.
     * @param service service value for the desired Client Credential record.
     * @return Client Credential object found
     */
    public ClientCredentials getByService(String service) {
        return ClientCredentials.getClientCredentials(em, service);
    }

    /**
     * Function for saving Client Credentials to the database table.
     * @param clientCredentials record to be added to table
     * @return
     */
    public long save(ClientCredentials clientCredentials) {
        long ret = -1;
        ClientCredentials credsIn = ClientCredentials.getClientCredentials(em, clientCredentials.getClientSecret());
        if (credsIn == null) {
            em.persist(clientCredentials);
            ret = clientCredentials.getId();
        }
        return ret;
    }

    /**
     * Function for updating a pre existing db record.
     * @param clientCredentials record to be updated.
     * @return updated ClientCredential record.
     */
    public ClientCredentials update(ClientCredentials clientCredentials) {
        return em.merge(clientCredentials);
    }

    /**
     * Function for removing a Client Credential record.
     * @param clientCredentials record to be removed
     * @return true for success, false for failure
     */
    public boolean remove(ClientCredentials clientCredentials) {
        return removeByService(clientCredentials.getService());
    }

    /**
     * Function for removing a Client Credential record using the service name.
     * @param service name of service to be removed
     * @return true for success, false for failure
     */
    public boolean removeByService(String service) {
        ClientCredentials credsIn = ClientCredentials.getClientCredentials(em, service);
        if (credsIn != null) {
            em.remove(credsIn);
            return true;
        }
        return false;
    }
}
