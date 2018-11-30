package scribe_java.gatekeeper;

import com.github.scribejava.core.model.OAuth2AccessToken;
import java.util.Objects;

public class GatekeeperOAuth2AccessToken extends OAuth2AccessToken {

    private final String userId;

    public GatekeeperOAuth2AccessToken(String accessToken, String openIdToken, String rawResponse) {
        this(accessToken, null, null, null, null, openIdToken, rawResponse);
    }

    public GatekeeperOAuth2AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken,
                                       String scope, String userId, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }


    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hashCode(userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }

        return Objects.equals(userId, ((GatekeeperOAuth2AccessToken) obj).getUserId());
    }
}
