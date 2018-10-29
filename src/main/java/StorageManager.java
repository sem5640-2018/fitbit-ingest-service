import javax.persistence.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.sun.security.ntlm.Client;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.hibernate.service.spi.ServiceException;

import java.io.Serializable;

public class StorageManager implements Serializable {

    private EntityManager em;

    public StorageManager() {

    }

    /**
     * Initialises the Entity Manager instance variable.
     */
    public int initEM() {
        int ret = 0;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("fitbitPU");
            em = emf.createEntityManager();
        } catch (ServiceException se) {
            System.err.println("Unable to initialise Entity Manager!\n");
            ret = -1;
        }
        return ret;
    }

    /**
     * THis function closes the Entity Manager.
     */
    public void closeEM() {
        em.close();
    }

    /**
     * This function will take a Token Map and either create one if the user doesn't already have a Token Map stored or
     * update it.
     * @param uID id of currently logged in user.
     * @param accessToken access token to be stored.
     * @param refreshToken refresh token to be stored.
     */
    public void commitTokenMap(String uID, String accessToken, String refreshToken) {
        if (!doesRecordExist(uID)) {
            createTokenMap(uID, accessToken, refreshToken);
        } else {
            updateTokenMap(uID, accessToken, refreshToken);
        }
    }

    /**
     * Gets the Access Token from Database
     * @param uId user id of logged in user
     * @param refresh boolean value TRUE for refresh token & FALSE for access token
     * @return Access Token for user
     */
    public String getToken(String uId, boolean refresh) {
        TokenMap tokenMap = getTokenMap(uId);
        if (refresh) {
            return tokenMap.getRefreshToken();
        } else {
            return tokenMap.getAccessToken();
        }
    }

    /**
     * This function removes a record from persistent storage.
     * @param uId user id of the current user
     */
    public void removeTokenMap(String uId) {
        TokenMap tm = getTokenMap(uId);
        TokenMap tokenMap = em.find(TokenMap.class, tm.getId());
        em.getTransaction().begin();
        em.remove(tokenMap);
        em.getTransaction().commit();
    }

    /**
     * Adds a TokenMap record to db.
     * @param uID user id associated with tokens
     * @param accessToken access token for accessing FitBit data
     * @param refreshToken refresh token for receiving new access token when old one expires
     */
    private void createTokenMap(String uID, String accessToken, String refreshToken) {
        try {
            TokenMap tokenMap = new TokenMap();
            tokenMap.setUserID(uID);
            tokenMap.setAccessToken(accessToken);
            tokenMap.setRefreshToken(refreshToken);
            em.getTransaction().begin();
            em.persist(tokenMap);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTokenMap(String uId, String accessToken, String refreshToken) {
        TokenMap tm = getTokenMap(uId);
        TokenMap tokenMap = em.find(TokenMap.class, tm.getId());
        em.getTransaction().begin();
        tm.setUserID(uId);
        tm.setAccessToken(accessToken);
        tm.setRefreshToken(refreshToken);
        em.getTransaction().commit();
    }

    private TokenMap getTokenMap(String uId) {
        Query q = em.createQuery("SELECT b FROM TokenMap b WHERE b.userID = :uId");
        q.setParameter("uId", uId);
        TokenMap tokeMap = (TokenMap) q.getSingleResult();
        return tokeMap;
    }

    public boolean doesRecordExist(String uId) {
        TokenMap tokeMap;
        try {
            tokeMap = getTokenMap(uId);
        } catch (NoResultException nre) {
            tokeMap = null;
        }

        if (tokeMap == null) { return false; }

        return true;
    }

    public void storeAppCreds(String appId, String appSecret) {
        if (doesCredRecordExist()) {
            updateCredRecord(appId, appSecret);
        } else {
            createCredRecord(appId, appSecret);
        }
    }

    public String getAppId() {
        ClientCredentials creds = getClientCredentials();
        return creds.getClientId();
    }

    public String getAppSecret() {
        ClientCredentials creds = getClientCredentials();
        return creds.getClientSecret();
    }

    private void  updateCredRecord(String id, String secret) {
        ClientCredentials cred = getClientCredentials();
        em.getTransaction().begin();
        cred.setClientId(id);
        cred.setClientSecret(secret);
        em.getTransaction().commit();
    }

    private void createCredRecord(String id, String secret) {
        ClientCredentials creds = new ClientCredentials();
        creds.setClientId(id);
        creds.setClientSecret(secret);
        creds.setService("fitbit");
        em.getTransaction().begin();
        em.persist(creds);
        em.getTransaction().commit();
    }

    private ClientCredentials getClientCredentials() {
        Query q = em.createQuery("SELECT b FROM ClientCredentials b WHERE b.service = :serv");
        q.setParameter("serv", "fitbit");
        ClientCredentials creds = (ClientCredentials) q.getSingleResult();
        return creds;
    }

    private boolean doesCredRecordExist() {
        ClientCredentials clientCredentials;
        try {
            clientCredentials = getClientCredentials();
        } catch (NoResultException nre) {
            clientCredentials = null;
        }

        if (clientCredentials == null) { return false; }
        return true;
    }
}
