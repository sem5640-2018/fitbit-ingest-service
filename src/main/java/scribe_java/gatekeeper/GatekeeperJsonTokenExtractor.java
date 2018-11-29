package scribe_java.gatekeeper;

import beans.EnvironmentVariableClass;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.regex.Pattern;

public class GatekeeperJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern OPEN_ID_REGEX_PATTERN = Pattern.compile("\"id_token\"\\s*:\\s*\"(\\S*?)\"");

    protected GatekeeperJsonTokenExtractor() {
    }

    public static GatekeeperJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    private JWTClaimsSet getJWTClaimSet(String accessToken) throws MalformedURLException, ParseException, JOSEException, BadJOSEException {
        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
        JWKSource keySrc = new RemoteJWKSet( new URL(EnvironmentVariableClass.getGatekeeperJWKUrl()));
        JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
        JWSKeySelector keySelector = new JWSVerificationKeySelector(jwsAlgorithm, keySrc);
        jwtProcessor.setJWSKeySelector(keySelector);
        SecurityContext ctx = null;
        return jwtProcessor.process(accessToken, ctx);
    }

    @Override
    protected GatekeeperOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
                                                  String refreshToken, String scope, String response) {
        String open_id_json = extractParameter(response, OPEN_ID_REGEX_PATTERN, false);
        JWTClaimsSet claimsSet;
        try {
            claimsSet = getJWTClaimSet(open_id_json);
            return new GatekeeperOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                    claimsSet.getSubject(), response);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
            return null;
        }
    }

    private static class InstanceHolder {

        private static final GatekeeperJsonTokenExtractor INSTANCE = new GatekeeperJsonTokenExtractor();
    }
}
