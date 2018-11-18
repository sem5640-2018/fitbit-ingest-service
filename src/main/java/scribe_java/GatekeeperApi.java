package scribe_java;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class GatekeeperApi extends DefaultApi20 {

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
        return "https://gatekeeper.aberfitness.biz/token"; //TODO Correct URL needed
    }

    /*@Override
    public String getRefreshTokenEndpoint() {
        return null;
    }*/

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://gatekeeper.aberfitness.biz/authorise"; //TODO Correct URL needed
    }
}
