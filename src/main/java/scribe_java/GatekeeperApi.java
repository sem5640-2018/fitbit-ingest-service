package scribe_java;

import com.github.scribejava.core.builder.api.DefaultApi20;
import scribe_java.gatekeeper.GatekeeperJsonTokenExtractor;

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

    @Override
    public GatekeeperJsonTokenExtractor getAccessTokenExtractor() {
        return GatekeeperJsonTokenExtractor.instance();
    }

    private static class InstanceHolder {
        private static final GatekeeperApi INSTANCE = new GatekeeperApi();
    }

    public static GatekeeperApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return baseUrl + "/token"; //TODO Correct URL needed
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return baseUrl + "/revocation"; //TODO Correct URL needed
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return baseUrl + "/authorize"; //TODO Correct URL needed
    }
}
