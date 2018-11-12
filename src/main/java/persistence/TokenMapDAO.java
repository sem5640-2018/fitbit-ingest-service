package persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TokenMapDAO {

    @PersistenceContext
    EntityManager em;

    public List<TokenMap> getAll() {
        return TokenMap.getAllTokenMap(em);
    }

    public TokenMap getByUid(String uid) {
        return TokenMap.getTokenMap(em, uid);
    }

    public long save(TokenMap tokenMap) {
        long ret = -1;
        TokenMap tmIn = TokenMap.getTokenMap(em, tokenMap.getUserID());
        if (tmIn == null) {
            em.persist(tokenMap);
            ret = tokenMap.getId();
        }
        return ret;
    }

    public TokenMap update(TokenMap tokenMap) {
        return em.merge(tokenMap);
    }

    public boolean remove(TokenMap tokenMap) {
        TokenMap tmIn = getByUid(tokenMap.getUserID());
        if (tmIn != null) {
            em.remove(tmIn);
            return true;
        }
        return false;
    }
}
