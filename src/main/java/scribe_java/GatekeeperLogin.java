package scribe_java;


import beans.OAuthBean;
import com.github.scribejava.core.model.OAuth2AccessToken;
import config.AuthStorage;
import config.EnvironmentVariableClass;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Stateful
public class GatekeeperLogin implements Serializable {

    @EJB
    OAuthBean oAuthBean;

    private GatekeeperOAuth2AccessToken userAccessToken;
    private String callback;

    public GatekeeperLogin() {
        //
    }

    public void redirectToGatekeeper(HttpServletResponse response, String callback, String state) throws IOException {
        oAuthBean.initGatekeeperService(callback, state, AuthStorage.userTokenScope);
        String url = oAuthBean.getAberfitnessService().getAuthorizationUrl();
        response.sendRedirect(url);
    }

    public void getGatekeeperGetAccessToken(HttpServletRequest request) {
        userAccessToken = null;
        String str = request.getParameter("code");
        if (str != null) {
            try {
                OAuth2AccessToken inAccessToken = oAuthBean.getAberfitnessService().getAccessToken(str);
                if (!(inAccessToken instanceof GatekeeperOAuth2AccessToken))
                    return;

                userAccessToken = (GatekeeperOAuth2AccessToken) inAccessToken;
                System.out.println("USER ID IN GATE AT: " + userAccessToken.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void retrieveNewClientCredAccessToken(String callback, String state) {
        oAuthBean.initGatekeeperService(callback, state, AuthStorage.clientCredScope);

        try {
            OAuth2AccessToken inAccessToken = oAuthBean.getAberfitnessService().getAccessTokenClientCredentialsGrant();

            if (!(inAccessToken instanceof GatekeeperOAuth2AccessToken))
                throw new Exception("inAccessToken isn't instanceof GatekeeperOAuth2AccessToken");

            AuthStorage.setApplicationToken((GatekeeperOAuth2AccessToken) inAccessToken);
        } catch (Exception e) {
            System.err.println("[GatekeeperLogin.retrieveNewClientCredAccessToken] Message:" + e.getMessage() + " Cause: " + e.getCause());
        }
    }

    public GatekeeperOAuth2AccessToken getAccessToken() {
        GatekeeperOAuth2AccessToken retAT = AuthStorage.getApplicationToken();
        if (retAT != null &&  !AberFitnessClientLogin.isInvalidAccessToken(retAT.getAccessToken(), AuthStorage.clientCredScope.split(" "))) {
            return retAT;
        } else {
            System.out.println("[AuditHelper.getAccessToken] Invalid Client Cred Access Token, Retrieving New One");
            retrieveNewClientCredAccessToken(EnvironmentVariableClass.getFitbitIngestLoginUrl(), "gateAccess");
            return AuthStorage.getApplicationToken();
        }
    }

    public String getUser_id() {
        if (userAccessToken != null)
            return userAccessToken.getUserId();
        return null;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
