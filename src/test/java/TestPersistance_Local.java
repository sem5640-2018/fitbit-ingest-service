import org.junit.Assert;
import org.junit.Test;

public class TestPersistance_Local {

    //Test Tokens
    String user1 = "jhb15";
    String accessToken1 = "access_toke_1";
    String refreshToken1 = "refresh_toke_1";

    String user2 = "ajb10";
    String accessToken2 = "access_toke_2";
    String refreshToken2 = "refresh_toke_2";

    //Test Creds
    String app_id1 = "Application ID 123";
    String app_secret1 = "Applicatio Secret 123";

    String app_id2 = "Application ID 999";

    @Test
    public void TestCommitToken() {
        StorageManager store = new StorageManager();
        String input;

        int ret = store.initEM();
        if (ret == -1) {
            Assert.fail();
            return;
        }

        store.commitTokenMap(user1, accessToken1, refreshToken1);
        input = store.getToken(user1, false);

        Assert.assertEquals(accessToken1, input);

        accessToken1 = "test_update_toke_1";
        store.commitTokenMap(user1, accessToken1, refreshToken1);
        input = store.getToken(user1, false);

        Assert.assertEquals(accessToken1, input);

        store.closeEM();
    }

    @Test
    public void TestRemoveToken() {
        //TODO implement test
        StorageManager store = new StorageManager();
        String input;

        int ret = store.initEM();
        if (ret == -1) {
            Assert.fail();
            return;
        }

        store.commitTokenMap(user2, accessToken2, refreshToken2);
        input = store.getToken(user2, false);

        Assert.assertEquals(accessToken2, input);

        store.removeTokenMap(user2);

        Assert.assertFalse(store.doesRecordExist(user2));

        store.closeEM();
    }

    @Test
    public void TestRemoveToken_NonExistant() {
        StorageManager store = new StorageManager();
        String input;

        int ret = store.initEM();
        if (ret == -1) {
            Assert.fail();
            return;
        }

        store.removeTokenMap("fake_uid");

    }

    @Test
    public void TestStoreAppCreds() {
        StorageManager storeManager = new StorageManager();
        String[] input = {"", ""};

        int ret = storeManager.initEM();
        if (ret == -1) {
            Assert.fail();
            return;
        }

        storeManager.storeAppCreds(app_id1, app_secret1);
        input[0] = storeManager.getAppId();
        input[1] = storeManager.getAppSecret();

        Assert.assertEquals(app_id1, input[0]);
        Assert.assertEquals(app_secret1, input[1]);

        storeManager.storeAppCreds(app_id2, app_secret1);
        input[0] = storeManager.getAppId();

        Assert.assertEquals(app_id2, input[0]);

        storeManager.closeEM();
    }
}
