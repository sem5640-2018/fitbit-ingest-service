package datacollection;

import beans.ActivityMappingBean;

import javax.ejb.EJB;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthDataFormat {

    @EJB
    ActivityMappingBean mappingBean;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private String StartTimestamp;
    private String EndTimestamp;
    private String activity_type;
    private long CaloriesBurnt;
    private long StepsTaken;

    private float MetresTravelled;

    public HealthDataFormat(Activity input) {
        ActivityMap map = mappingBean.getMapFromID(Long.toString(input.getActivityId()));

        StartTimestamp = sdf.format(input.getJavaDate());
        EndTimestamp = sdf.format(getEndDate(input));
        StepsTaken = input.getSteps();
        CaloriesBurnt = input.getCalories();
        MetresTravelled = input.getDistance();
        // If we have a mapping for this item
        activity_type = map == null ? input.getDescription() : map.getName();
    }

    public float getDistance() {
        return MetresTravelled;
    }

    public String getActivity_type() {
        return activity_type;
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
}
