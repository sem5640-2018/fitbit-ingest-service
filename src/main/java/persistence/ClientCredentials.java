package persistence;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * This class represents a Token Map record stored in the fitbit ingest database.
 * @author James Britton
 * @author jhb15@aber.ac.uk
 * @version 0.1
 */
@Entity
@Table(name = "client_credentials")
@NamedQueries({
        @NamedQuery(name = "ClientCredentials.findAll", query = "SELECT c FROM ClientCredentials c"),
        @NamedQuery(name = "ClientCredentials.findByService", query = "SELECT c FROM ClientCredentials c WHERE c.service = :serv")
})
public class ClientCredentials {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "service", unique = true, nullable = false)
    private String service;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void prePersist() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = new Date();
    }

    /**
     * Empty constructor for Client Credentials
     */
    public ClientCredentials() {
        //
    }

    /**
     * Contructor that takes in params to set instance variables.
     * @param clientId client id provided by fitbit
     * @param clientSecret client secret provided by fitbit
     * @param service string identifier for service credentials belong to
     */
    public ClientCredentials(String clientId, String clientSecret, String service) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.service = service;
    }

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getUpdatedAt() { return updatedAt; }

    public Date getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "ClientCredentials{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", service='" + service + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * This function is used to utilise the 'ClientCredentials.findByService' named query.
     * @param em entity manager where the data is
     * @param service the sevice that identitfys the ClientCredentials
     * @return The result of the query.
     */
    public static ClientCredentials getClientCredentials(EntityManager em, String service) {
        ClientCredentials tm;
        try {
            Query query = em.createNamedQuery("ClientCredentials.findByService", ClientCredentials.class);
            query.setParameter("serv", service);
            tm = (ClientCredentials) query.getSingleResult();
        } catch (NoResultException nre) {
            tm = null;
        }
        return tm;
    }

    /**
     * This function utilises the 'ClientCredentials.findAll' named query for retreiving all the Records in the client
     * credentials table.
     * @param em Entity Manager for where the data resides
     * @return A List of Client Credential Records
     */
    @SuppressWarnings("unchecked")
    public static List<ClientCredentials> getAllClientCredentials(EntityManager em) {
        List<ClientCredentials> clientCredList;
        try {
            Query query = em.createNamedQuery("ClientCredentials.findAll", ClientCredentials.class);
            clientCredList = query.getResultList();
        } catch (NoResultException nre) {
            clientCredList = null;
        }
        return clientCredList;
    }

    /**
     * This allows the removal of Client Credential records from the table by using the service column to identify the
     * record to be removed.
     * @param em Entity Manager for where the data resides
     * @param service service credentials we wish to remove.
     * @return boolean true for success & false for error
     */
    public static boolean removeByService(EntityManager em, String service) {
        ClientCredentials cliCred = getClientCredentials(em, service);
        if (cliCred != null) {
            em.getTransaction().begin();
            em.remove(cliCred);
            em.getTransaction().commit();
        } else
            return false;
        return true;
    }
}
