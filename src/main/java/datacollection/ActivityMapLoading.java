package datacollection;

import beans.EnvironmentVariableBean;
import com.google.gson.Gson;

import javax.ejb.EJB;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityMapLoading {

    @EJB
    EnvironmentVariableBean environmentVariableBean;
    private static Gson gson = new Gson();

    public ActivityMap[] checkMappings() {
        String endPointLocation = environmentVariableBean.getAberfitnessActivityEndPoint();

        try {
            String returnedJSON = getHTML(endPointLocation);
            return gson.fromJson(returnedJSON, ActivityMap[].class);

        } catch (Exception e) {
            // @TODO Log error
            e.printStackTrace();
        }

        return null;
    }

    private static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
