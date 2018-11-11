package persistence;

import org.junit.Assert;
import org.junit.Test;

public class TestClientCredential extends PersistenceTest {

    //Test Creds
    String ct_app_id = "testCreateAppId";
    String ct_app_secret = "testCreateAppSecret";
    String ct_service = "testCreateService";

    String ut_app_id = "testUpdateAppId";
    String ut_app_secret = "testUpdateAppSecret";
    String ut_service = "testUpdateService";

    String dt_app_id = "testDeleteAppId";
    String dt_app_secret = "testDeleteAppSecret";
    String dt_service = "testDeleteService";

    private void commitClientCred(String id, String secret, String service) {
        ClientCredentials creds = new ClientCredentials();
        creds.setClientId(id);
        creds.setClientSecret(secret);
        creds.setService(service);
        em.getTransaction().begin();
        em.persist(creds);
        em.getTransaction().commit();
    }

    @Test
    public void testCommitClientCredential() {
        commitClientCred(ct_app_id, ct_app_secret, ct_service);

        ClientCredentials cliCred = ClientCredentials.getClientCredentials(em, ct_service);

        Assert.assertEquals(ct_app_id, cliCred.getClientId());
        Assert.assertEquals(ct_app_secret, cliCred.getClientSecret());
    }

    @Test
    public void testUpdateClientCredential() {
        commitClientCred(ut_app_id, ut_app_secret, ut_service);

        ClientCredentials cliCred = ClientCredentials.getClientCredentials(em, ut_service);

        Assert.assertEquals(ut_app_id, cliCred.getClientId());
        Assert.assertEquals(ut_app_secret, cliCred.getClientSecret());

        ut_app_id = "updatedID";
        ut_app_secret = "updatedSecret";

        em.getTransaction().begin();
        cliCred.setClientId(ut_app_id);
        cliCred.setClientSecret(ut_app_secret);
        em.getTransaction().commit();

        ClientCredentials cliCredIn = ClientCredentials.getClientCredentials(em, ut_service);

        Assert.assertEquals(ut_app_id, cliCredIn.getClientId());
        Assert.assertEquals(ut_app_secret, cliCredIn.getClientSecret());
    }

    @Test
    public void testDeleteClientCredential() {
        commitClientCred(dt_app_id, dt_app_secret, dt_service);
        Assert.assertNotNull(ClientCredentials.getClientCredentials(em, dt_service));

        ClientCredentials.removeByService(em, dt_service);
        Assert.assertNull(ClientCredentials.getClientCredentials(em, dt_service));
    }

    @Test
    public void testDeleteClientCredential_Faliure() {
        Assert.assertFalse(ClientCredentials.removeByService(em, "fake_service"));
    }
}
