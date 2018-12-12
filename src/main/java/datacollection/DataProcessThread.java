package datacollection;

import beans.ActivityMappingBean;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import config.EnvironmentVariableClass;
import datacollection.mappings.Activity;
import datacollection.mappings.FitBitJSON;
import datacollection.mappings.HealthDataFormat;
import datacollection.mappings.Steps;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DataProcessThread implements Runnable {

    private String format = "yyyy-MM-dd:H:m";
    private DateFormat df = new SimpleDateFormat(format);
    private Gson gson = new Gson();
    private String postURL;
    private GatekeeperOAuth2AccessToken accessToken;
    private AtomicInteger counter;
    private ActivityMappingBean activityMappingBean;
    private OAuth20Service gatekeeperService;

    private ConcurrentLinkedQueue<ProcessedData> input;

    DataProcessThread(ConcurrentLinkedQueue<ProcessedData> input, GatekeeperOAuth2AccessToken accessToken, AtomicInteger atomicInteger,
                      ActivityMappingBean activityMappingBean, OAuth20Service gatekeeperService) {
        // Create Shallow copy to the global linked queue
        this.input = input;
        this.accessToken = accessToken;
        this.counter = atomicInteger;
        postURL = EnvironmentVariableClass.getHeathDataRepoAddActivityUrl();
        this.activityMappingBean = activityMappingBean;
        this.gatekeeperService = gatekeeperService;
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

        LinkedList<Steps> allSteps = getRelevantSteps(input, input.getProcessedSteps());
        LinkedList<String> readyToSend = getPacketsToSend(allActivities, allSteps);
        sendData(readyToSend);
    }

    private LinkedList<String> getPacketsToSend(LinkedList<Activity> readyToSend, LinkedList<Steps> stepsToSend) {
        LinkedList<String> toSend = new LinkedList<>();
        for (Activity activity : readyToSend) {
            try {
                HealthDataFormat formattedData = new HealthDataFormat(activity, activityMappingBean);
                // If not set, then we can't send the data
                if (formattedData.getActivity_type() < 0)
                    continue;

                toSend.add(gson.toJson(formattedData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Steps steps : stepsToSend) {
            try {
                HealthDataFormat formattedData = new HealthDataFormat(steps, activityMappingBean);
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

    private LinkedList<Steps> getRelevantSteps(ProcessedData input, LinkedList<Steps> allSteps) {
        LinkedList<Steps> relevantActivities = new LinkedList<>();

        for (Steps activity : allSteps) {
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
        return lastChecked == null || activity.getJavaDate().getTime() + activity.getDuration() > lastChecked.getTime();
    }

    private boolean isRelevant(Steps activity, Date lastChecked) {
        long endTime = activity.getDateTime().getTime() + 86400000;
        return lastChecked == null || (endTime > lastChecked.getTime() && endTime < new Date().getTime());
    }

    private void sendData(LinkedList<String> dataToSend) {
        for (String jsonData : dataToSend) {
            try {
                counter.incrementAndGet();
                doPost(jsonData);
            } catch (Exception e) {
                counter.decrementAndGet();
                e.printStackTrace();
            }
        }
    }

    private void doPost(String rawData) throws Exception {
        String accessTokenString = "";
        if (accessToken != null)
            accessTokenString = accessToken.getAccessToken();

        OAuthRequest request = new OAuthRequest(Verb.POST, postURL);
        request.addHeader("Content-Type", "application/json;charset=UTF-8");
        request.setPayload(rawData);
        gatekeeperService.signRequest(accessTokenString, request);
        Response response = gatekeeperService.execute(request);
        System.out.println(response.getCode() + "|" + response.getMessage() + "|" + response.getBody());
    }
}