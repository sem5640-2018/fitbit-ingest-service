package beans;

public final class EnvriomentVariableBean {

    //App Config
    private static final String serviceName = System.getenv("SERVICE_NAME");

    // Fitbit Info
    private static final String fitbitClientId = System.getenv("fitbitClientId");
    private static final String fitbitClientSecret = System.getenv("fitbitClientSecret");
    private static final String fitbitClientCallback = System.getenv("fitbitClientCallback");

    //Aberfitness Info
    private static final String aberfitnessClientId = System.getenv("ABERFITNESS_CLI_ID");
    private static final String aberfitnessClientSecret = System.getenv("ABERFITNESS_CLI_SECRET");

    //Base URL Defs (Both of these will be the same, If running locally appBaseUrl will be localhost)
    private static final String appBaseUrl = System.getenv("APP_BASE_URL");
    private static final String systemBaseUrl = System.getenv("SYS_BASE_URL");
    //Application URLs
    private static final String fitbitIngestUrl = appBaseUrl + System.getenv("FITBIT_INGEST_URL");
    private static final String gatekeeperUrl = systemBaseUrl + System.getenv("GATEKEEPER_URL");
    //Fitbit Ingest URL Defs
    private static final String fitbitIngestLoginUrl = fitbitIngestUrl + System.getenv("FI_LOGIN_URL");
    private static final String fitbitIngestPromptUrl = fitbitIngestUrl + System.getenv("FI_PROMPT_URL");
    //Gatekeeper URL Defs
    private static final String gatekeeperAuthoriseUrl = gatekeeperUrl + System.getenv("GK_AUTH_URL");
    private static final String gatekeeperTokenUrl = gatekeeperUrl + System.getenv("GK_TOKEN_URL");
    private static final String gatekeeperRevokeUrl = gatekeeperUrl + System.getenv("GK_REVOKE_URL");
    private static final String gatekeeperJWKUrl = gatekeeperUrl + System.getenv("GK_JWK_URL");

    private EnvriomentVariableBean() {
    }

    public static String getServiceName() {
        return serviceName;
    }

    public static String getFitbitClientId() {
        return fitbitClientId;
    }

    public static String getFitbitClientSecret() {
        return fitbitClientSecret;
    }

    public static String getFitbitClientCallback() {
        return fitbitClientCallback;
    }

    public static String getAberfitnessClientId() {
        return aberfitnessClientId;
    }

    public static String getAberfitnessClientSecret() {
        return aberfitnessClientSecret;
    }

    public static String getAppBaseUrl() {
        return appBaseUrl;
    }

    public static String getSystemBaseUrl() {
        return systemBaseUrl;
    }

    public static String getFitbitIngestUrl() {
        return fitbitIngestUrl;
    }

    public static String getGatekeeperUrl() {
        return gatekeeperUrl;
    }

    public static String getFitbitIngestLoginUrl() {
        return fitbitIngestLoginUrl;
    }

    public static String getFitbitIngestPromptUrl() {
        return fitbitIngestPromptUrl;
    }

    public static String getGatekeeperAuthoriseUrl() {
        return gatekeeperAuthoriseUrl;
    }

    public static String getGatekeeperTokenUrl() {
        return gatekeeperTokenUrl;
    }

    public static String getGatekeeperRevokeUrl() {
        return gatekeeperRevokeUrl;
    }

    public static String getGatekeeperJWKUrl() {
        return gatekeeperJWKUrl;
    }

    public static boolean isAberfitnessDataPresent() {
        return aberfitnessClientId != null && aberfitnessClientSecret != null;
    }

    public static boolean isFitbitDataPresent() {
        return fitbitClientId != null && fitbitClientSecret != null && fitbitClientCallback != null;
    }
}
