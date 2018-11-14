package beans;

import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import persistence.TokenMap;
import persistence.TokenMapDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;

@SessionScoped
@ManagedBean(name = "LoginBean")
public class LoginBean implements Serializable {

    @Inject
    OAuthBean oAuthBean;

    @Inject
    TokenMapDAO tokenMapDAO;


    private String token;
    private String authLocation;
    private String error;

    public LoginBean() {
        // Do nothing
    }

    /**
     * This method is called just before the page loads
     *
     */
    public void onLoad() {


        if (token != null) {
            try {
                final OAuth2AccessToken oauth2AccessToken = oAuthBean.getService().getAccessToken(token);
                if (!(oauth2AccessToken instanceof FitBitOAuth2AccessToken)) {
                    token = null;
                    // @TODO Log error
                    error = "Invalid token provided, please retry";
                    return;
                }

                final FitBitOAuth2AccessToken accessToken = (FitBitOAuth2AccessToken) oauth2AccessToken;
                TokenMap map = new TokenMap("test", accessToken.getAccessToken(), accessToken.getRefreshToken());
                tokenMapDAO.save(map);

            } catch (Exception e) {
                // @TODO Log error
                error = "Could not store token";
                token = null;
            }
        } else {
           authLocation = oAuthBean.getService().getAuthorizationUrl();
        }
    }

    /**
     * This method is used set the parameter given after a user authenticates with FitBit
     *
     * @param token the code provided by Fitbit
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * This method is used for mapping the token parameter
     *
     * @return token this is the provided parameter
     */
    public String getToken() { return this.token; }

    /**
     * This method is used for retrieving the errors if any
     *
     * @return error if there is one otherwise null
     */
    public String getError() { return this.error; }

    /**
     * This method is called when the login button is pressed,
     * it will redirect the user to the authentication page provided by FitBit
     */
    public void loginButtonPress() {
        if (authLocation == null) return;

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(authLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
