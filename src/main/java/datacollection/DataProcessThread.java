package datacollection;

import config.AuthStorage;
import config.EnvironmentVariableClass;
import com.google.gson.Gson;
import datacollection.mappings.Activity;
import datacollection.mappings.FitBitJSON;
import datacollection.mappings.HealthDataFormat;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataProcessThread implements Runnable {

    private String format = "yyyy-MM-dd:H:m";
    private DateFormat df = new SimpleDateFormat(format);
    private Gson gson = new Gson();
    private URL postURL;

    private ConcurrentLinkedQueue<ProcessedData> input;

    DataProcessThread(ConcurrentLinkedQueue<ProcessedData> input) {
        // Create Shallow copy to the global linked queue
        this.input = input;
        try {
            postURL = new URL(EnvironmentVariableClass.getHeathDataRepoAddActivityUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called automatically when the thread is started
     */
    public void run() {
        ProcessedData toCheck = input.poll();
        while (toCheck != null) {
            checkData(toCheck);

            toCheck = input.poll();
        }
    }

    private void checkData(ProcessedData input) {
        LinkedList<Activity> allActivities = getAllActivities(input);
        allActivities = getRelevantActivities(input, allActivities);

        LinkedList<String> readyToSend = getPacketsToSend(allActivities);
        sendData(readyToSend);
    }

    private LinkedList<String> getPacketsToSend(LinkedList<Activity> readyToSend) {
        LinkedList<String> toSend = new LinkedList<>();
        for (Activity activity : readyToSend) {
            try {
                HealthDataFormat formattedData = new HealthDataFormat(activity);
                // If not set, then we can't send the data
                if (formattedData.getActivity_type() < 0)
                    continue;

                toSend.add(gson.toJson(formattedData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toSend;
    }

    private LinkedList<Activity> getRelevantActivities(ProcessedData input, LinkedList<Activity> allActivities) {
        LinkedList<Activity> relevantActivities = new LinkedList<>();

        for (Activity activity : allActivities) {
            if (isRelevant(activity, input.getInputToken().getLastAccessed()))
                relevantActivities.add(activity);
        }

        return relevantActivities;
    }

    private LinkedList<Activity> getAllActivities(ProcessedData input) {
        LinkedList<Activity> activities = new LinkedList<>();

        for (FitBitJSON fitbitClass : input.getProcessedActivities()) {
            for (Activity activity : fitbitClass.getActivities()) {

                // To deal with Fitbits API formatting getting an end date for an activity.....
                // Not sure of a better way to approach this.
                String dateStr = fitbitClass.getFromDate() + ":" + activity.getStartTime();
                try {
                    Date startTime = df.parse(dateStr);
                    activity.setJavaDate(startTime);
                    activity.setUserID(fitbitClass.getUserID());
                    activities.add(activity);
                } catch (Exception e) {
                    System.out.println(dateStr);
                    e.printStackTrace();
                }
            }
        }
        return activities;
    }

    private boolean isRelevant(Activity activity, Date lastChecked) {
        return activity.getJavaDate().getTime() + activity.getDuration() > lastChecked.getTime();
    }

    private void sendData(LinkedList<String> dataToSend) {
        for (String jsonData : dataToSend) {
            try {
                doPost(jsonData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doPost(String rawData) throws Exception {
        GatekeeperOAuth2AccessToken accessToken = AuthStorage.getApplicationToken();
        if(accessToken == null)
            throw new Exception("Application Access Token Not Set");
        String accessTokenAuth = accessToken.getTokenType() + " " + accessToken.getAccessToken();

        String type = "application/x-www-form-urlencoded";
        String encodedData = URLEncoder.encode(rawData, "UTF-8");
        HttpURLConnection conn = (HttpURLConnection) postURL.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", accessTokenAuth);
        conn.setRequestProperty("Content-Type", type);
        conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
        OutputStream os = conn.getOutputStream();
        os.write(encodedData.getBytes());
    }
}