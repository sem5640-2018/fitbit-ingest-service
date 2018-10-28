import org.junit.Assert;
import org.junit.Test;

public class TestPersistance {
    String user1 = "jhb15";
    String accessToken1 = "access_toke_1";
    String refreshToken1 = "refresh_toke_1";

    String user2 = "ajb10";
    String accessToken2 = "access_toke_2";
    String refreshToken2 = "refresh_toke_2";

    @Test
    public void TestCommitToken() {
        TokenStorage store = new TokenStorage();
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
    }

    @Test
    public void TestRemoveToken() {
        //TODO implement test
        TokenStorage store = new TokenStorage();
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
    }
}
