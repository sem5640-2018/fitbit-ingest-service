package scribe_java;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Gatekeeper Scribe-Java custom api class for using the Scribe-Java library for authenticating with glados.
 * @Author James H Britton
 * @Version 0.1
 */
public class GatekeeperApi extends DefaultApi20 {

    //private static String baseUrl = "https://gatekeeper.aberfitness.biz";//TODO extract to environment variables
    private static String baseUrl = System.getenv("GATEKEEPER_BASEURL");

    public GatekeeperApi() {
        //
    }

    private static class InstanceHolder {
        private static final GatekeeperApi INSTANCE = new GatekeeperApi();
    }

    public static GatekeeperApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return baseUrl + "/access_token"; //TODO Correct URL needed
    }

    @Override
    public String getRefreshTokenEndpoint() {
        return baseUrl + "/refresh_token"; //TODO Correct URL needed
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return baseUrl + "/revoke_token"; //TODO Correct URL needed
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return baseUrl + "/authorise"; //TODO Correct URL needed
    }
}
