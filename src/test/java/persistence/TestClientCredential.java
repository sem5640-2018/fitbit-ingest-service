package persistence;

import org.junit.Assert;
import org.junit.Test;

public class TestClientCredential extends PersistenceTest {

    //Test Creds
    String ct_app_id = "testCreateAppId";
    String ct_app_secret = "testCreateAppSecret";

    String ut_app_id = "testUpdateAppId";
    String ut_app_secret = "testUpdateAppSecret";

    String dt_app_id = "testDeleteAppId";
    String dt_app_secret = "testDeleteAppSecret";

    @Test
    public void testCommitClientCredential() {
        String[] input = {"", ""};

        store.storeAppCreds(ct_app_id, ct_app_secret);
        input[0] = store.getAppId();
        input[1] = store.getAppSecret();

        Assert.assertEquals(ct_app_id, input[0]);
        Assert.assertEquals(ct_app_secret, input[1]);
    }

    @Test
    public void testUpdateClientCredential() {
        String[] input = {"", ""};

        store.storeAppCreds(ut_app_id, ut_app_secret);
        input[0] = store.getAppId();
        input[1] = store.getAppSecret();

        ut_app_id = "updatedID";
        ut_app_secret = "updatedSecret";

        store.storeAppCreds(ut_app_id, ut_app_secret);
        input[0] = store.getAppId();
        input[1] = store.getAppSecret();

        Assert.assertEquals(ut_app_id, input[0]);
        Assert.assertEquals(ut_app_secret, input[1]);
    }

    @Test
    public void testDeleteClientCredential() {
        store.storeAppCreds(dt_app_id, dt_app_secret);
        Assert.assertNotNull(store.doesCredRecordExist());

        Assert.assertTrue(store.removeAppCreds());
        Assert.assertNull(store.doesCredRecordExist());
    }

    @Test
    public void testDeleteClientCredential_Faliure() {
        if (store.doesCredRecordExist() != null) {
            store.removeAppCreds();
        }

        Assert.assertFalse(store.removeAppCreds());
    }
}
