package beans;

import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import persistence.StorageManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

@SessionScoped
@ManagedBean(name = "LoginBean")
public class LoginBean implements Serializable {

    // Used as a singleton to store access tokens
    private static StorageManager store;

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
        if (store == null) {
            // @TODO Log error
            error = "No store configured";
            return;
        }

        final String clientId = store.getAppId();
        final String clientSecret = store.getAppSecret();

        if (clientId == null || clientSecret == null) {
            // @TODO Log error
            error = "No client ID or client secret";
            return;
        }

        OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .scope("activity profile")
                .callback("http://localhost:8080/injest_exploded/Login.xhtml")
                .state("some_params")
                .build(FitbitApi20.instance());

        if (token != null) {
            try {
                final OAuth2AccessToken oauth2AccessToken = service.getAccessToken(token);
                if (!(oauth2AccessToken instanceof FitBitOAuth2AccessToken)) {
                    token = null;
                    // @TODO Log error
                    error = "Invalid token provided, please retry";
                    return;
                }

                final FitBitOAuth2AccessToken accessToken = (FitBitOAuth2AccessToken) oauth2AccessToken;
                store.commitTokenMap("test", accessToken.getAccessToken(), accessToken.getRefreshToken());

            } catch (Exception e) {
                // @TODO Log error
                error = "Could not store token";
                token = null;
            }
        } else {
           authLocation = service.getAuthorizationUrl();
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

    /**
     * This method is used for testing
     *
     * @param manager the storage manager to use
     */
    public static void setStorageManager(StorageManager manager) {
        store = manager;
    }
}
