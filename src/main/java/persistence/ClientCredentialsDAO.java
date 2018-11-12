package persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ClientCredentialsDAO {

    @PersistenceContext
    EntityManager em;

    public List<ClientCredentials> getAll() {
        return ClientCredentials.getAllClientCredentials(em);
    }

    public ClientCredentials getByService(String service) {
        return ClientCredentials.getClientCredentials(em, service);
    }

    public long save(ClientCredentials clientCredentials) {
        long ret = -1;
        ClientCredentials credsIn = ClientCredentials.getClientCredentials(em, clientCredentials.getClientSecret());
        if (credsIn == null) {
            em.persist(clientCredentials);
            ret = clientCredentials.getId();
        }
        return ret;
    }

    public ClientCredentials update(ClientCredentials clientCredentials) {
        return em.merge(clientCredentials);
    }

    public boolean remove(ClientCredentials clientCredentials) {
        ClientCredentials credsIn = ClientCredentials.getClientCredentials(em, clientCredentials.getClientSecret());
        if (credsIn != null) {
            em.remove(credsIn);
            return true;
        }
        return false;
    }
}
