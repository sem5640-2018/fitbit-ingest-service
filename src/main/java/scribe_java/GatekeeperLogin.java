package scribe_java;


import beans.OAuthBean;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.nimbusds.jwt.JWTClaimsSet;
import config.AuthStorage;
import scribe_java.gatekeeper.GatekeeperJsonTokenExtractor;
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

    public GatekeeperLogin() {
        //
    }

    public void redirectToGatekeeper(HttpServletResponse response, String callback, String state) throws IOException {
        oAuthBean.initGatekeeperService(callback, state, "openid profile offline_access");
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
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getGatekeeperGrantAccessToken(String callback, String state, String scope) {
        oAuthBean.initGatekeeperService(callback, state, scope);

        try {
            OAuth2AccessToken inAccessToken = oAuthBean.getAberfitnessService().getAccessTokenClientCredentialsGrant();

            if (!(inAccessToken instanceof GatekeeperOAuth2AccessToken))
                throw new Exception("inAccessToken isn't instanceof GatekeeperOAuth2AccessToken");

            AuthStorage.setApplicationToken((GatekeeperOAuth2AccessToken) inAccessToken);
            return;

        } catch (Exception e) {
            System.err.println("[GatekeeperLogin.getGatekeeperGrantAccessToken] Message:" + e.getMessage() + " Cause: " + e.getCause());
        }
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            JWTClaimsSet claimsSet = GatekeeperJsonTokenExtractor.instance().getJWTClaimSet(accessToken);
            System.out.println("Token Issued By: " + claimsSet.getIssuer());

            /*if (!claimsSet.getAudience().contains("fitbit-ingest-service")) //Disabled for testing purposes.
                throw new Exception("Access Token Audience does not include Fitbit Ingest!");*/

            return true;
        } catch (Exception e) {
            System.err.println("[GatekeeperLogin.validateAccessToken] Message:" + e.getMessage() + " Cause: " + e.getCause());
            return false;
        }
    }

    public String getUser_id() {
        if (userAccessToken != null)
            return userAccessToken.getUserId();
        return null;
    }
}
