package scribe_java;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.nimbusds.jwt.JWTClaimsSet;
import config.AuthStorage;
import config.EnvironmentVariableClass;
import scribe_java.gatekeeper.GatekeeperJsonTokenExtractor;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import java.util.List;

/**
 * Added this class to avoid the EJB not intialized madness when we didn't really need to use any EJB functionality.
 * @author James H Britton
 */
public class AberFitnessClientLogin {

    private OAuth20Service aberFitnessService;

    /**
     * Constructs class initialising the OAuth service for rretrieving Client Credential Tokens
     */
    public AberFitnessClientLogin() {
        aberFitnessService = new ServiceBuilder(EnvironmentVariableClass.getAberfitnessClientId())
                .apiSecret(EnvironmentVariableClass.getAberfitnessClientSecret())
                .scope(AuthStorage.clientCredScope)
                .build(GatekeeperApi.instance());
    }

    /**
     * Gets new Client Credential Token from Gatekeeper
     */
    private void retrieveNewClientCredAccessToken() {

        try {
            OAuth2AccessToken inAccessToken = aberFitnessService.getAccessTokenClientCredentialsGrant();

            if (!(inAccessToken instanceof GatekeeperOAuth2AccessToken))
                throw new Exception("inAccessToken isn't instanceof GatekeeperOAuth2AccessToken");

            AuthStorage.setApplicationToken((GatekeeperOAuth2AccessToken) inAccessToken);
        } catch (Exception e) {
            System.err.println("[GatekeeperLogin.retrieveNewClientCredAccessToken] Message:" + e.getMessage() + " Cause: " + e.getCause());
        }
    }

    /**
     * Gets stored Client Cred Access Token or if a valid one isn't stored it will retrieve a new one.
     * @return GatekeeperOAuth2AccessToken access token
     */
    public GatekeeperOAuth2AccessToken getAccessToken() {
        GatekeeperOAuth2AccessToken retAT = AuthStorage.getApplicationToken();
        if (retAT != null &&  !isInvalidAccessToken(retAT.getAccessToken(), AuthStorage.clientCredScope.split(" "))) {
            return retAT;
        } else {
            System.out.println("[AuditHelper.getAccessToken] Invalid Client Cred Access Token, Retrieving New One");
            retrieveNewClientCredAccessToken();
            return AuthStorage.getApplicationToken();
        }
    }

    public static boolean isInvalidAccessToken(String accessToken, String[] expectedAud) {
        try {
            JWTClaimsSet claimsSet = GatekeeperJsonTokenExtractor.instance().getJWTClaimSet(accessToken);
            System.out.println("Token Issued By: " + claimsSet.getIssuer());

            List<String> audience = claimsSet.getAudience();

            if (expectedAud == null && !audience.contains(AuthStorage.fitbitScope))
                throw new Exception("Access Token Audience does not include Fitbit Ingest!");

            if (expectedAud != null){
                for (String s: expectedAud) {
                    if (!audience.contains(s))
                        throw new Exception("Access Token Audience does not include Glados & Heath Data Repo!");
                }
            }

            return false;
        } catch (Exception e) {
            System.err.println("[GatekeeperLogin.validateAccessToken] Message:" + e.getMessage() + " Cause: " + e.getCause());
            return true;
        }
    }
}
