package persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This is a class for creating a Data Access Object EJB for the Token Map table.
 * @Author James H Britton
 * @Author 'jhb15@aber.ac.uk'
 * @Version 0.1
 */
@Stateless
public class TokenMapDAO {

    @PersistenceContext
    EntityManager em;

    /**
     * Function for listing all token map records.
     * @return List of Token Maps
     */
    public List<TokenMap> getAll() {
        return TokenMap.getAllTokenMap(em);
    }

    /**
     * Get all Token Map by using User ID function
     * @param uid User ID of Token Map
     * @return a Token Map with the requested User ID
     */
    public TokenMap getByUid(String uid) {
        return TokenMap.getTokenMap(em, uid);
    }

    /**
     * Used fo committing a Token Map to the database
     * @param tokenMap token map to be saved
     * @return id of the new db record
     */
    public long save(TokenMap tokenMap) {
        long ret = -1;
        TokenMap tmIn = TokenMap.getTokenMap(em, tokenMap.getUserID());
        if (tmIn == null) {
            em.persist(tokenMap);
            ret = tokenMap.getId();
        }
        return ret;
    }

    /**
     * Used for updating a token map record in the database
     * @param tokenMap modified token map
     * @return updated Token Map
     */
    public TokenMap update(TokenMap tokenMap) {
        return em.merge(tokenMap);
    }

    /**
     * Function for removing a token map from the database.
     * @param tokenMap token map to be removed
     * @return true for success, false for failure
     */
    public boolean remove(TokenMap tokenMap) {
        return removeByUserID(tokenMap.getUserID());
    }

    /**
     * Function for removing a users Token Map by using just there user ID.
     * @param userID user id of Token Map to be removed.
     * @return true for success, false for failure
     */
    public boolean removeByUserID(String userID) {
        TokenMap tmIn = getByUid(userID);
        if (tmIn != null) {
            em.remove(tmIn);
            return true;
        }
        return false;
    }
}
