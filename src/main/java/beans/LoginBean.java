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
    private static final StorageManager store = new StorageManager();
    private static final String PROTECTED_RESOURCE_URL = "https://api.fitbit.com/1/user/%s/profile.json";

    private String token;
    private String authLocation;

    public LoginBean() {
        // Do nothing
    }

    /**
     * This method is called just before the
     *
     */
    public void onLoad() {
        final String clientId = "22D4RT";
        final String clientSecret = "547470906547f1d7d0ffc7e55fc4a733";
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
                    return;
                }

                final FitBitOAuth2AccessToken accessToken = (FitBitOAuth2AccessToken) oauth2AccessToken;

                store.commitTokenMap("test", accessToken.getAccessToken(), accessToken.getRefreshToken());

            } catch (Exception e) {
                e.printStackTrace();
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
