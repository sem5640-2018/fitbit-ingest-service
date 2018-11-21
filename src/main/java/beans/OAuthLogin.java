package beans;

import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import persistence.TokenMap;
import persistence.TokenMapDAO;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Login", urlPatterns = { "/" } )
public class OAuthLogin extends HttpServlet{

    @Inject
    OAuthBean oAuthBean;

    @Inject
    TokenMapDAO tokenMapDAO;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws  IOException {
        response.setContentType("text/html");

        String str = request.getParameter("code");
        if (str != null) {
            try {
                final OAuth2AccessToken oauth2AccessToken = oAuthBean.getFitbitService().getAccessToken(str);
                if (!(oauth2AccessToken instanceof FitBitOAuth2AccessToken))
                    return;

                final FitBitOAuth2AccessToken accessToken = (FitBitOAuth2AccessToken) oauth2AccessToken;
                tokenMapDAO.save(new TokenMap("Test", accessToken.getAccessToken(), accessToken.getRefreshToken()));
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final String authorizationUrl = oAuthBean.getFitbitService().getAuthorizationUrl();
        response.sendRedirect(authorizationUrl);
    }
}