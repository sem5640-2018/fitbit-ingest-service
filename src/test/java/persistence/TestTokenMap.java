package persistence;

import org.junit.Assert;
import org.junit.Test;

public class TestTokenMap extends PersistenceTest {

    //Test Tokens
    String ct_user = "commitTrestUser";
    String ct_accessToken = "commitTestAccessToken";
    String ct_refreshToken = "commitTestRefreshToken";

    String ut_user = "updateTestUser";
    String ut_accessToken = "updateTestAccessToken";
    String ut_refreshToken = "updateTestRefreshToken";

    String dt_user = "delTestUser";
    String dt_accessToken = "delTestAccessToken";
    String dt_refreshToken = "delTestRefreshToken";

    private void commitTokenMap(String user, String accessToken, String refreshToken) {
        TokenMap tm = new TokenMap();
        tm.setUserID(user);
        tm.setAccessToken(accessToken);
        tm.setRefreshToken(refreshToken);
        tm.setFitbitUid("FB" + user);
        tm.setExpiresIn(3600);
        em.getTransaction().begin();
        em.persist(tm);
        em.getTransaction().commit();
    }

    @Test
    public void testCommitTokenMap() {

        commitTokenMap(ct_user, ct_accessToken, ct_refreshToken);

        TokenMap tmIn = TokenMap.getTokenMap(em, ct_user);

        Assert.assertEquals(ct_accessToken, tmIn.getAccessToken());
        Assert.assertEquals(ct_refreshToken, tmIn.getRefreshToken());
        Assert.assertEquals("FB" + ct_user, tmIn.getFitbitUid());
    }

    @Test
    public void testEditTokenMap() {
        commitTokenMap(ut_user, ut_accessToken, ut_refreshToken);

        TokenMap tm = TokenMap.getTokenMap(em, ut_user);

        Assert.assertEquals(ut_accessToken, tm.getAccessToken());
        Assert.assertEquals(ut_refreshToken, tm.getRefreshToken());

        ut_accessToken = "test_update_access_token";
        ut_refreshToken = "test_update_refresh_token";

        em.getTransaction().begin();
        tm.setAccessToken(ut_accessToken);
        tm.setRefreshToken(ut_refreshToken);
        em.getTransaction().commit();

        TokenMap inTM = TokenMap.getTokenMap(em, ut_user);

        Assert.assertEquals(ut_accessToken, inTM.getAccessToken());
        Assert.assertEquals(ut_refreshToken, inTM.getRefreshToken());
    }

    @Test
    public void testTokenCount() {
        long start = TokenMap.getAllTokenMap(em).size();

        commitTokenMap(ct_user, ct_accessToken, ct_refreshToken);

        long end = TokenMap.getAllTokenMap(em).size();
        Assert.assertTrue(start < end);
    }

    @Test
    public void testDeleteTokenMap() {
        commitTokenMap(dt_user, dt_accessToken, dt_refreshToken);
        Assert.assertNotNull(TokenMap.getTokenMap(em, dt_user));

        TokenMap.removeByUid(em, dt_user);
        Assert.assertNull(TokenMap.getTokenMap(em, dt_user));
    }

    @Test
    public void testDeleteTokenMap_Faliure() {
        Assert.assertFalse(TokenMap.removeByUid(em, "fake_uid"));
    }
}
