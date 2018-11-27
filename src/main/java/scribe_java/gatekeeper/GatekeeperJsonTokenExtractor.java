package scribe_java.gatekeeper;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public class GatekeeperJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern USER_ID_REGEX_PATTERN = Pattern.compile("\"sub\"\\s*:\\s*\"(\\S*?)\"");
    private static final Pattern OPEN_ID_REGEX_PATTERN = Pattern.compile("\"id_token\"\\s*:\\s*\"(\\S*?)\"");
    //private static final Pattern ACCESS_TOKEN_REGEX_PATTERN = Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");

    protected GatekeeperJsonTokenExtractor() {
    }

    public static GatekeeperJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected GatekeeperOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
                                                  String refreshToken, String scope, String response) {
        String open_id_json = extractParameter(response, OPEN_ID_REGEX_PATTERN, false);
        String jwtSections[] = open_id_json.split("\\.", 3);
        byte[] json_bytes = Base64.getDecoder().decode(jwtSections[1]);
        String decoded_open_id = new String(json_bytes, StandardCharsets.UTF_8);
        return new GatekeeperOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(decoded_open_id, USER_ID_REGEX_PATTERN, false), response);
    }

    /**
     * Related documentation: https://dev.fitbit.com/build/reference/web-api/oauth2/
     */
    /*@Override
    public void generateError(String response) {
        final String errorInString = extractParameter(response, ERROR_REGEX_PATTERN, true);
        final String errorDescription = extractParameter(response, ERROR_DESCRIPTION_REGEX_PATTERN, false);

        OAuth2AccessTokenErrorResponse.ErrorCode errorCode;
        try {
            errorCode = OAuth2AccessTokenErrorResponse.ErrorCode.valueOf(errorInString);
        } catch (IllegalArgumentException iaE) {
            //non oauth standard error code
            errorCode = null;
        }

        throw new OAuth2AccessTokenErrorResponse(errorCode, errorDescription, null, response);
    }*/

    private static class InstanceHolder {

        private static final GatekeeperJsonTokenExtractor INSTANCE = new GatekeeperJsonTokenExtractor();
    }
}
