package datacollection.mappings;

import beans.ActivityMappingBean;

import javax.ejb.EJB;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HealthDataFormat {
    @EJB
    ActivityMappingBean mappingBean;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private String UserID;
    private String StartTimestamp;
    private String EndTimestamp;

    private String Source = "Fitbit";
    private int ActivityType = -1;
    private long CaloriesBurnt;

    private float AverageHeartRate;
    private long StepsTaken;
    private float MetresTravelled;
    private float MetresElevationGained;

    public HealthDataFormat(Activity input) {

        // Mapping bean is not working
        if (mappingBean == null)
            return;

        ActivityMap map = mappingBean.getMapFromID(Long.toString(input.getActivityId()));
        if (map == null)
            return;

        UserID = input.getUserID();
        StartTimestamp = sdf.format(input.getJavaDate());
        EndTimestamp = sdf.format(getEndDate(input));

        // If we have a mapping for this item
        ActivityType = map.getID();
        CaloriesBurnt = input.getCalories();

        AverageHeartRate = -1;
        StepsTaken = input.getSteps();
        MetresTravelled = input.getDistance();
        MetresElevationGained = -1;
    }

    public float getDistance() {
        return MetresTravelled;
    }

    public int getActivity_type() {
        return ActivityType;
    }

    public String getStart_time() {
        return StartTimestamp;
    }

    public long getStepsTaken() {
        return StepsTaken;
    }

    public long getCaloriesBurnt() {
        return CaloriesBurnt;
    }

    private static Date getEndDate(Activity input) {
        return new Date(input.getJavaDate().getTime() + input.getDuration());
    }

    public String getEndTimestamp() {
        return EndTimestamp;
    }

    public String getSource() {
        return Source;
    }

    public float getAverageHeartRate() {
        return AverageHeartRate;
    }

    public float getMetresElevationGained() {
        return MetresElevationGained;
    }

    public String getUserID() {
        return UserID;
    }
}
