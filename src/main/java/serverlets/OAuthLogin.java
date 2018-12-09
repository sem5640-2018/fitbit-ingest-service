package serverlets;

import config.EnvironmentVariableClass;
import beans.OAuthBean;
import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import logging.AuditHelper;
import persistence.TokenMap;
import persistence.TokenMapDAO;
import scribe_java.GatekeeperLogin;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Login", urlPatterns = { "/LoginPage" } )
public class OAuthLogin extends HttpServlet {

    @EJB
    OAuthBean oAuthBean;

    @EJB
    TokenMapDAO tokenMapDAO;

    @EJB
    GatekeeperLogin gatekeeperLogin;

    @EJB
    AuditHelper auditHelper;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");

        String state = request.getParameter("state");

        String callback = request.getParameter("callback");
        if (callback != null)
            gatekeeperLogin.setCallback(callback);

        if (state == null) {
            gatekeeperLogin.redirectToGatekeeper(response, EnvironmentVariableClass.getFitbitIngestLoginUrl(), "gateAccess");
        } else if (state.equals("gateAccess")) {
            gatekeeperLogin.getGatekeeperGetAccessToken(request);
            redirectToFitbit(response);
        } else if (state.equals("some_params")) {
            getFitbitAccessToken(request, gatekeeperLogin.getUser_id());
            response.sendRedirect(gatekeeperLogin.getCallback());
        }
    }

    private void redirectToFitbit(HttpServletResponse response) throws IOException {
        final String authorizationUrl = oAuthBean.getFitbitService().getAuthorizationUrl();
        response.sendRedirect(authorizationUrl);
    }

    private void getFitbitAccessToken(HttpServletRequest request, String accessT) {
        String str = request.getParameter("code");
        if (str != null) {
            try {
                final OAuth2AccessToken oauth2AccessToken = oAuthBean.getFitbitService().getAccessToken(str);
                if (!(oauth2AccessToken instanceof FitBitOAuth2AccessToken))
                    return;

                final FitBitOAuth2AccessToken accessToken = (FitBitOAuth2AccessToken) oauth2AccessToken;
                TokenMap map = new TokenMap(accessT, accessToken.getAccessToken(), accessToken.getExpiresIn(),
                        accessToken.getRefreshToken(), accessToken.getUserId());
                map.setUserID(accessT);
                tokenMapDAO.saveOrUpdate(map);
                auditHelper.sendAudit("Authorizing Fitbit Access", "User has given Aberfitness permission to access there data.", gatekeeperLogin.getUser_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}