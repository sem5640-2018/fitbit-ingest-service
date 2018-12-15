package datacollection.mappings;

import beans.ActivityMappingBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HealthDataFormat {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private String userID;
    private String startTimestamp;
    private String endTimestamp;

    private String source = "Fitbit";
    private int activityTypeId = -1;
    private long caloriesBurnt;

    private long averageHeartRate;
    private long stepsTaken;
    private float metresTravelled;
    private float metresElevationGained;

    public HealthDataFormat(Activity input, ActivityMappingBean activityMappingBean) {

        // Mapping bean is not working
        if (activityMappingBean == null)
            return;

        ActivityMap map = activityMappingBean.getMapFromID(Long.toString(input.getActivityId()));
        if (map == null)
            return;

        userID = input.getUserID();
        startTimestamp = sdf.format(input.getJavaDate());
        endTimestamp = sdf.format(getEndDate(input));

        // If we have a mapping for this item
        activityTypeId = map.getID();
        caloriesBurnt = input.getCalories();

        averageHeartRate = 0;
        stepsTaken = input.getSteps();
        metresTravelled = input.getDistance();
        metresElevationGained = 0;
    }

    public HealthDataFormat(Steps steps, ActivityMappingBean activityMappingBean) {
        // Mapping bean is not working
        if (activityMappingBean == null)
            return;

        ActivityMap map = activityMappingBean.getMapFromID("HOURLYSTEPS");
        if (map == null)
            return;

        userID = steps.getUserID();
        startTimestamp = sdf.format(steps.getDateTime());
        endTimestamp = sdf.format(getDayLater(steps.getDateTime()));

        activityTypeId = map.getID();
        caloriesBurnt = 0;

        averageHeartRate = 0;
        stepsTaken = steps.getValue();
        metresElevationGained = 0;
        metresTravelled = 0;
    }

    public float getDistance() {
        return metresTravelled;
    }

    public int getActivity_type() {
        return activityTypeId;
    }

    public String getStart_time() {
        return startTimestamp;
    }

    public long getStepsTaken() {
        return stepsTaken;
    }

    public long getCaloriesBurnt() {
        return caloriesBurnt;
    }

    private static Date getEndDate(Activity input) {
        return new Date(input.getJavaDate().getTime() + input.getDuration());
    }

    private static Date getDayLater(Date date) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        cal.add(Calendar.DATE, 1); // adds one hour
        return cal.getTime(); // returns new date object, one hour in the future
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public String getSource() {
        return source;
    }

    public float getAverageHeartRate() {
        return averageHeartRate;
    }

    public float getMetresElevationGained() {
        return metresElevationGained;
    }

    public String getUserID() {
        return userID;
    }
}
