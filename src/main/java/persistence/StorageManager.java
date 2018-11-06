package persistence;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;

/**
 * This class represents a Storage Manager which is responsible for providing the glue code between the DB and the
 * application.
 * @author James Britton
 * @author jhb15@aber.ac.uk
 * @version 0.1
 */
public class StorageManager implements Serializable {

    @PersistenceContext
    private EntityManager em;

    /**
     * Storage Manager constructor taking one parameter the Entity Manager that should be setup outside of the object
     * an passed in when constructed.
     * @param em
     */
    public StorageManager(EntityManager em) {
        this.em = em;
    }

    /**
     * This function will take a Token Map and either create one if the user doesn't already have a Token Map stored or
     * update it.
     * @param uID id of currently logged in user.
     * @param accessToken access token to be stored.
     * @param refreshToken refresh token to be stored.
     */
    public void commitTokenMap(String uID, String accessToken, String refreshToken) {
        TokenMap tokenMap = doesTokenMapExist(uID);
        if (tokenMap == null) {
            createTokenMap(uID, accessToken, refreshToken);
        } else {
            updateTokenMap(tokenMap, accessToken, refreshToken);
        }
    }

    /**
     * Gets the Access Token from Database
     * @param uId user id of logged in user
     * @param refresh boolean value TRUE for refresh token & FALSE for access token
     * @return Access Token for user
     */
    public String getToken(String uId, boolean refresh) {
        TokenMap tokenMap = doesTokenMapExist(uId);
        if (refresh) {
            return tokenMap.getRefreshToken();
        } else {
            return tokenMap.getAccessToken();
        }
    }

    /**
     * This function removes a record from persistent storage.
     * @param uId user id of the current user
     * @return true for success, false for failure (record doesn't exist)
     */
    public boolean removeTokenMap(String uId) {
        TokenMap tokenMap = doesTokenMapExist(uId);
        if (tokenMap != null) {
            em.getTransaction().begin();
            em.remove(tokenMap);
            em.getTransaction().commit();
        } else
            return false;
        return true;
    }

    /**
     * This method serves a function for retrieving a Token Map and also Checking for it't existence in the Database
     * @param uId user id of current user
     * @return return persistence.TokenMap if it Exists, null if it does not.
     */
    public TokenMap doesTokenMapExist(String uId) {
        TokenMap tokeMap;
        try {
            Query q = em.createQuery("SELECT b FROM persistence.TokenMap b WHERE b.userID = :uId");
            q.setParameter("uId", uId);
            tokeMap = (TokenMap) q.getSingleResult();
        } catch (NoResultException nre) {
            tokeMap = null;
        }
        return tokeMap;
    }

    /**
     * This method serves as a way to test the number of items in the store
     * @return returns the number of tokens in the store and -1 if an error has occurred
     */
    public long getTokenCount() {
        int count;
        try {
            Query q = em.createQuery("SELECT COUNT(b) FROM persistence.TokenMap b");
            count = Integer.parseInt(q.getSingleResult().toString());
        } catch (NoResultException nre) {
            count = -1;
        }
        return count;
    }

    /**
     * Private method for creating/commiting the Token Map to the database.
     * @param uID user id for token map table
     * @param accessToken access token to be stored in token map table
     * @param refreshToken refresh token for token map table
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


    /**
     * Private method for updateing an existing Token Map record.
     * @param tm existing Token Map to be updated
     * @param accessToken new access token
     * @param refreshToken new refresh token
     */
    private void updateTokenMap(TokenMap tm, String accessToken, String refreshToken) {
        em.getTransaction().begin();
        tm.setAccessToken(accessToken);
        tm.setRefreshToken(refreshToken);
        em.getTransaction().commit();
    }

    /**
     * This fuction allows a user to store application credentials in the DB, and also update them if they already exist
     * @param appId app_id code from fitbit
     * @param appSecret app_secret from fitbit
     */
    public void storeAppCreds(String appId, String appSecret) {
        ClientCredentials clientCredentials = doesCredRecordExist();
        if (clientCredentials != null) {
            updateCredRecord(clientCredentials, appId, appSecret);
        } else {
            createCredRecord(appId, appSecret);
        }
    }

    /**
     * This function retrieves the app_id from the database.
     * @return app_id returned, null if no record found
     */
    public String getAppId() {
        ClientCredentials creds = doesCredRecordExist();
        if (creds == null)
            return null;
        return creds.getClientId();
    }

    /**
     * This funtion is for retrieving the app_secret
     * @return app_secret returned, null if no record found.
     */
    public String getAppSecret() {
        ClientCredentials creds = doesCredRecordExist();
        if (creds == null)
            return null;
        return creds.getClientSecret();
    }

    /**
     * This funtion removes Application Credentials
     * @return boolean value true for success and false for failure
     */
    public boolean removeAppCreds() {
        ClientCredentials creds = doesCredRecordExist();
        if (creds != null) {
            em.getTransaction().begin();
            em.remove(creds);
            em.getTransaction().commit();
            return true;
        } else
            return false;
    }

    /**
     * This function is used as check to see if fitbit record exists and also get the Credentials if it does.
     * @return fitbit credentials if exists, null if they don't exist.
     */
    public ClientCredentials doesCredRecordExist() {
        ClientCredentials clientCredentials;
        try {
            Query q = em.createQuery("SELECT b FROM persistence.ClientCredentials b WHERE b.service = :serv");
            q.setParameter("serv", "fitbit");
            clientCredentials = (ClientCredentials) q.getSingleResult();
        } catch (NoResultException nre) {
            clientCredentials = null;
        }
        return clientCredentials;
    }

    /**
     * Function for updateing an existing Client Credential record.
     * @param cred existing Client Credential
     * @param id new client id
     * @param secret new client secret
     */
    private void  updateCredRecord(ClientCredentials cred, String id, String secret) {
        em.getTransaction().begin();
        cred.setClientId(id);
        cred.setClientSecret(secret);
        em.getTransaction().commit();
    }

    /**
     * Function for creating a Client Credential when one doesn't already exist.
     * @param id client id
     * @param secret client secret
     */
    private void createCredRecord(String id, String secret) {
        ClientCredentials creds = new ClientCredentials();
        creds.setClientId(id);
        creds.setClientSecret(secret);
        creds.setService("fitbit");
        em.getTransaction().begin();
        em.persist(creds);
        em.getTransaction().commit();
    }
}
