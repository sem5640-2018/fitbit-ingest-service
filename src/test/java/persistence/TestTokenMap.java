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

    @Test
    public void testCommitTokenMap() {
        String input;

        store.commitTokenMap(ct_user, ct_accessToken, ct_refreshToken);

        input = store.getToken(ct_user, false);
        Assert.assertEquals(ct_accessToken, input);

        input = store.getToken(ct_user, true);
        Assert.assertEquals(ct_refreshToken, input);

        ct_accessToken = "test_update_toke_1";
        store.commitTokenMap(ct_user, ct_accessToken, ct_refreshToken);
        input = store.getToken(ct_user, false);

        Assert.assertEquals(ct_accessToken, input);
    }

    @Test
    public void testEditTokenMap() {
        String input;

        store.commitTokenMap(ut_user, ut_accessToken, ut_refreshToken);

        ut_accessToken = "test_update_access_token";
        ut_refreshToken = "test_update_refresh_token";
        store.commitTokenMap(ut_user, ut_accessToken, ut_refreshToken);

        input = store.getToken(ut_user, false);
        Assert.assertEquals(ut_accessToken, input);

        input = store.getToken(ut_user, true);
        Assert.assertEquals(ut_refreshToken, input);
    }

    @Test
    public void testDeleteTokenMap() {
        store.commitTokenMap(dt_user, dt_accessToken, dt_refreshToken);
        Assert.assertNotNull(store.getTokenMap(dt_user));

        store.removeTokenMap(dt_user);
        Assert.assertNull(store.getTokenMap(dt_user));
    }

    @Test
    public void testDeleteTokenMap_Faliure() {
        Assert.assertFalse(store.removeTokenMap("fake_uid"));
    }
}
