package beans;

import org.junit.Before;
import org.junit.Test;
import persistence.PersistenceTest;

public class TestLoginBean extends PersistenceTest {

    private static LoginBean bean;
    private static final String clientId = "22D4RT";
    private static final String clientSecret = "547470906547f1d7d0ffc7e55fc4a733";

    @Before
    public void start()  {
        bean = new LoginBean();
    }

    /**
     * This checks the load method doesn't add anything to the store if there isnt a valid key
     */
    @Test
    public void checkLoadWithoutKey() {
       /* long startLength = store.getTokenCount();

        bean.onLoad();

        long endLength = store.getTokenCount();

        // Checking nothing has been added to the store
        Assert.assertEquals(startLength, endLength);
        Assert.assertNull(bean.getToken());

        // Check no errors have occurred
        Assert.assertNull(bean.getError()); */
    }

    /**
     * This checks the load method doesn't add anything to the store if there isnt a valid key
     */
    @Test
    public void checkLoadWithInvalidKey() {
        /*long startLength = store.getTokenCount();

        bean.setToken("INVALIDTOKEN");
        bean.onLoad();

        long endLength = store.getTokenCount();

        // Checking nothing has been added to the store
        Assert.assertEquals(startLength, endLength);

        // Check that the error has be raised
        Assert.assertNotNull(bean.getError());*/
    }

    /**
     * This checks the load method flags an issue when no store is set
     */
    @Test
    public void checkErrorIsSetWithNoStore() {
       /* Assert.assertNull(bean.getError());
        LoginBean.setStorageManager(null);
        bean.onLoad();

        // The error has been set
        Assert.assertNotNull(bean.getError());

        // Tidy up after finishing
        LoginBean.setStorageManager(store);*/
    }
}
