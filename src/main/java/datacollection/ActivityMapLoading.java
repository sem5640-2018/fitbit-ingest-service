package datacollection;

import config.EnvironmentVariableClass;
import com.google.gson.Gson;
import datacollection.mappings.ActivityMap;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityMapLoading {

    private static Gson gson = new Gson();
    private static GatekeeperOAuth2AccessToken accessToken;

    public ActivityMap[] checkMappings(GatekeeperOAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
        String endPointLocation = EnvironmentVariableClass.getHeathDataRepoGetActivityTypesUrl();

        try {
            String returnedJSON = getHTML(endPointLocation);
            return gson.fromJson(returnedJSON, ActivityMap[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        String accessTokenAuth = accessToken.getTokenType() + " " + accessToken.getAccessToken();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", accessTokenAuth);
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
