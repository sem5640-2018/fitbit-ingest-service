package datacollection;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataProcessThread implements Runnable {

    private String format = "yyyy-MM-dd:H:m";
    private DateFormat df = new SimpleDateFormat(format);
    private Gson gson = new Gson();
    private ConcurrentLinkedQueue<ProcessedData> input;

    public DataProcessThread(ConcurrentLinkedQueue<ProcessedData> input) {
        // Create Shallow copy to the global linked queue
        this.input = input;
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

        // @TODO send all new relevant activities to the Heath data Repository
        LinkedList<String> readyToSend = getPacketsToSend(allActivities);
        readyToSend.size();
    }

    private LinkedList<String> getPacketsToSend(LinkedList<Activity> readyToSend) {
        LinkedList<String> toSend = new LinkedList<String>();
        for (Activity activity: readyToSend) {
            HealthDataFormat formattedData = new HealthDataFormat(activity);
            toSend.add(gson.toJson(formattedData));
        }
        return toSend;
    }

    private LinkedList<Activity> getRelevantActivities(ProcessedData input, LinkedList<Activity> allActivities) {
      LinkedList<Activity> relevantActivities = new LinkedList<Activity>();

      for(Activity activity: allActivities) {
          if (isRelevant(activity, input.getInputToken().getLastAccessed()))
              relevantActivities.add(activity);
      }

      return relevantActivities;
    }

    private LinkedList<Activity> getAllActivities(ProcessedData input) {
      LinkedList<Activity> activities = new LinkedList<Activity>();

      for(FitBitJSON fitbitClass: input.getProcessedActivities()) {
          for (Activity activity: fitbitClass.getActivities()) {

              // To deal with Fitbits API formatting getting an end date for an activity.....
              // Not sure of a better way to approach this.
              String dateStr = fitbitClass.getFromDate() + ":" + activity.getStartTime();
              try {
                  Date startTime = df.parse(dateStr);
                  activity.setJavaDate(startTime);
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
}