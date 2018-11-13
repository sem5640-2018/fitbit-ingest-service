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

    @Column(name = "service")
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
     */
    public ClientCredentials(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Getter for id used in table
     * @return the id of Token Map
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id used in the table.
     * @param id desired id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the client id
     * @return client id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Setter for the client id
     * @param clientId desired client id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Getter for the client secret
     * @return client secret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Setter for the client secret
     * @param clientSecret desired client secret
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * Getter for the service string.
     * @return service string
     */
    public String getService() {
        return service;
    }

    /**
     * Setter for the service string.
     * @param service desired service string
     */
    public void setService(String service) {
        this.service = service;
    }


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
