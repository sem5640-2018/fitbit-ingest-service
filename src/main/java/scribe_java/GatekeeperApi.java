package scribe_java;

import beans.EnvriomentVariableBean;
import com.github.scribejava.core.builder.api.DefaultApi20;
import scribe_java.gatekeeper.GatekeeperJsonTokenExtractor;

/**
 * Gatekeeper Scribe-Java custom api class for using the Scribe-Java library for authenticating with glados.
 * @author James H Britton
 * @version 0.1
 */
public class GatekeeperApi extends DefaultApi20 {

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
        return EnvriomentVariableBean.getGatekeeperTokenUrl();
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return EnvriomentVariableBean.getGatekeeperRevokeUrl();
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return EnvriomentVariableBean.getGatekeeperAuthoriseUrl();
    }
}
