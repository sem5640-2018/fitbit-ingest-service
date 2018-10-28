import javax.persistence.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.service.spi.ServiceException;

import java.io.Serializable;

public class TokenStorage implements Serializable {

    private EntityManager em;

    public TokenStorage() {

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
        //TODO implement
        TokenMap tm = getTokenMap(uId);
        TokenMap tokenMap = em.find(TokenMap.class, tm.getId());
        em.getTransaction().begin();
        tokenMap.setUserID(uId);
        tokenMap.setAccessToken(accessToken);
        tokenMap.setRefreshToken(refreshToken);
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
            Query q = em.createQuery("SELECT b FROM TokenMap b WHERE b.userID = :uId");
            q.setParameter("uId", uId);
            tokeMap = (TokenMap) q.getSingleResult();
        } catch (NoResultException nre) {
            tokeMap = null;
        }

        if (tokeMap == null) { return false; }

        return true;
    }
}
