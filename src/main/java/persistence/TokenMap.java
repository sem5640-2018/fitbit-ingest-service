package persistence;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Represents a Client Credential record in the fitbit ingest database.
 * @author James Britton
 * @author jhb15@aber.ac.uk
 * @version 0.1
 */
@Entity
@Table(name = "token_map")
@NamedQueries({
        @NamedQuery(name = "TokenMap.findAll", query = "SELECT c FROM TokenMap c"),
        @NamedQuery(name = "TokenMap.findByUid", query = "SELECT c FROM TokenMap c WHERE c.userID = :uId")
})
public class TokenMap implements Serializable {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="user_id")
    private String userID;

    @Column(name= "access_token")
    private String accessToken;

    @Column(name = "expires_in")
    private int expiresIn;

    @Column(name= "refresh_token")
    private String refreshToken;

    @Column(name = "fitbit_uid")
    private String fitbitUid;

    @Column(name = "last_accessed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccessed;

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
     * Empty Constructor
     */
    public TokenMap() {
        //
    }

    /**
     * Constructor that takes parameters.
     * @param userID user id of current user
     * @param accessToken access token obtained from fitbit
     * @param refreshToken refresh token obtained from fitbit
     */
    public TokenMap(String userID, String accessToken, String refreshToken) {
        this.userID = userID;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() { return expiresIn; }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getFitbitUid() {
        return fitbitUid;
    }

    public void setFitbitUid(String fitbitUid) {
        this.fitbitUid = fitbitUid;
    }

    public Date getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "TokenMap{" +
                "id=" + id +
                ", userID='" + userID + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", fitbitUid='" + fitbitUid + '\'' +
                ", lastAccessed=" + lastAccessed +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Static function for running 'TokenMap.findByUid' Names Query with appropriate error checking to avoid code
     * duplication.
     * @param em initialised EntityManager
     * @param userID User ID to lookup record in table
     * @return return TokenMap if found, if not found returns null
     */
    public static TokenMap getTokenMap(EntityManager em, String userID) {
        TokenMap tm;
        try {
            Query query = em.createNamedQuery("TokenMap.findByUid", TokenMap.class);
            query.setParameter("uId", userID);
            tm = (TokenMap) query.getSingleResult();
        } catch (NoResultException nre) {
            tm = null;
        }
        return tm;
    }

    /**
     * Static function for running 'TokenMap.findAll' Named Query with appropriate error checking to avoid code
     * duplication.
     * @param em initialised Entity Manager
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<TokenMap> getAllTokenMap(EntityManager em) {
        List<TokenMap> tokenMapList;
        try {
            Query query = em.createNamedQuery("TokenMap.findAll", TokenMap.class);
            tokenMapList = query.getResultList();
        } catch (NoResultException nre) {
            tokenMapList = null;
        }
        return tokenMapList;
    }

    /**
     * Static function for removing a Token Map by using just the user id.
     * @param em initialised Entity Manager
     * @param userID user ID of Token Map we wish to remove
     * @return
     */
    public static boolean removeByUid(EntityManager em, String userID) {
        TokenMap tm = getTokenMap(em, userID);
        if (tm != null) {
            em.getTransaction().begin();
            em.remove(tm);
            em.getTransaction().commit();
            return true;
        } else
            return false;
    }
}
