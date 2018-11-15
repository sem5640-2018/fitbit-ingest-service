package datacollection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataProcessThread implements Runnable {
    private ConcurrentLinkedQueue<ProcessedData> input;
    private Date now;
    private static Calendar cal = Calendar.getInstance();
    private static String format = "yyyy-MM-dd:H:m";
    private static DateFormat df = new SimpleDateFormat(format);

    DataProcessThread(ConcurrentLinkedQueue<ProcessedData> input) {
        // Create Shallow copy to the global linked queue
        this.input = input;

        // Store the start of the requests
        now = new Date();
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
    }

    private LinkedList<Activity> getRelevantActivities(ProcessedData input, LinkedList<Activity> allActivities) {
      LinkedList<Activity> relevantActivities = new LinkedList<Activity>();

      for(Activity activity: relevantActivities) {
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
                  activity.setJavaDate(new Date(startTime.getTime() + activity.getDuration()));
                  activities.add(activity);
              } catch (ParseException e) {
                  e.printStackTrace();
              }
          }
      }

      return activities;
    }

    private boolean isRelevant(Activity activity, Date lastChecked) {
        return activity.getJavaDate().getTime() > lastChecked.getTime();
    }
}