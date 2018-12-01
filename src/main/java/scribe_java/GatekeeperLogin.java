package scribe_java;


import beans.OAuthBean;
import com.github.scribejava.core.model.OAuth2AccessToken;
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

    private GatekeeperOAuth2AccessToken accessToken;

    public GatekeeperLogin() {
        //
    }

    public void redirectToGatekeeper(HttpServletResponse response, String callback, String state) throws IOException {
        oAuthBean.initGatekeeperService(callback, state, "openid profile offline_access");
        String url = oAuthBean.getAberfitnessService().getAuthorizationUrl();
        response.sendRedirect(url);
    }

    public void getGatekeeperGetAccessToken(HttpServletRequest request) {
        accessToken = null;
        String str = request.getParameter("code");
        if (str != null) {
            try {
                OAuth2AccessToken inAccessToken = oAuthBean.getAberfitnessService().getAccessToken(str);
                if (!(inAccessToken instanceof GatekeeperOAuth2AccessToken))
                    return;

                accessToken = (GatekeeperOAuth2AccessToken) inAccessToken;
                System.out.println("USER ID IN GATE AT: " + accessToken.getUserId());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getGatekeeperGrantAccessToken(String callback, String state, String scope) {
        accessToken = null;
        oAuthBean.initGatekeeperService(callback, state, scope);

        try {
            OAuth2AccessToken inAccessToken = oAuthBean.getAberfitnessService().getAccessTokenClientCredentialsGrant();

            if (!(inAccessToken instanceof GatekeeperOAuth2AccessToken))
                return;

            accessToken = (GatekeeperOAuth2AccessToken) inAccessToken;
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUser_id() {
        if (accessToken != null)
            return accessToken.getUserId();
        return null;
    }
}
