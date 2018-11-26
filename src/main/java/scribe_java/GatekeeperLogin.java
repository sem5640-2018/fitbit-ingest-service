package scribe_java;


import beans.OAuthBean;
import com.github.scribejava.core.model.OAuth2AccessToken;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

@Stateful
public class GatekeeperLogin implements Serializable {

    @EJB
    OAuthBean oAuthBean;

    private OAuth2AccessToken accessToken;
    private String user_id;

    public GatekeeperLogin() {
        //
    }

    public void redirectToGatekeeper(HttpServletResponse response, String callback, String state) throws IOException {
        oAuthBean.initGatekeeperService(callback, state);
        String url = oAuthBean.getAberfitnessService().getAuthorizationUrl();
        response.sendRedirect(url);
    }

    public void getGatekeeperGetAccessToken(HttpServletRequest request) {
        accessToken = null;
        String str = request.getParameter("code");
        if (str != null) {
            try {
                accessToken = oAuthBean.getAberfitnessService().getAccessToken(str);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getUser_id() {
        String at = accessToken.getAccessToken();
        System.out.println("ID_TOKEN: " + at);
        String strings[] = at.split("\\.");
        System.out.println("HEADER = [" + strings[0] + "] PAYLOAD = [" + strings[1] + "] SIGNITURE = [" + strings[2] + "]");

        JsonObject jwtHeader = Json.createReader(
                new ByteArrayInputStream(Base64.getDecoder().decode(strings[1])))
                .readObject();
        System.out.println("SUBJECT: " + jwtHeader.getString("sub"));
        user_id = jwtHeader.getString("sub");
        return user_id;
    }
}
