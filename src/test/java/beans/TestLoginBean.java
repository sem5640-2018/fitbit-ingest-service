package beans;

import org.junit.Assert;
import org.junit.Test;
import persistence.PersistenceTest;

public class TestLoginBean extends PersistenceTest {

    private static LoginBean bean;

    public TestLoginBean() {
        bean = new LoginBean();

        bean.setStorageManager(store);
    }


    /**
     * This checks the load method doesn't add anything to the store if there isnt a valid key
     */
    @Test
    public void checkLoadWithoutKey() {
        long startLength = store.getTokenCount();

        bean.onLoad();

        long endLength = store.getTokenCount();

        // Checking nothing has been added to the store
        Assert.assertEquals(startLength, endLength);
    }

    /**
     * This checks the load method doesn't add anything to the store if there isnt a valid key
     */
    @Test
    public void checkLoadWithInvalidKey() {
        long startLength = store.getTokenCount();

        bean.setToken("INVALIDTOKEN");
        bean.onLoad();

        long endLength = store.getTokenCount();

        // Checking nothing has been added to the store
        Assert.assertEquals(startLength, endLength);
    }
}
