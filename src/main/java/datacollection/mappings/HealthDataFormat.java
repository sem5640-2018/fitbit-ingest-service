package datacollection.mappings;

import beans.ActivityMappingBean;
import datacollection.ActivityMap;

import javax.ejb.EJB;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HealthDataFormat {
    private static int SOURCE_ID = 1;

    @EJB
    ActivityMappingBean mappingBean;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private String StartTimestamp;
    private String EndTimestamp;

    private int Source;
    private int ActivityType = -1;
    private long CaloriesBurnt;

    private long StepsTaken;
    private float MetresTravelled;

    public HealthDataFormat(Activity input) {

        // Mapping bean is not working
        if (mappingBean == null)
            return;

        ActivityMap map = mappingBean.getMapFromID(Long.toString(input.getActivityId()));
        if (map == null)
            return;

        StartTimestamp = sdf.format(input.getJavaDate());
        EndTimestamp = sdf.format(getEndDate(input));

        Source = SOURCE_ID;
        // If we have a mapping for this item
        ActivityType = map.getId();
        CaloriesBurnt = input.getCalories();

        StepsTaken = input.getSteps();
        MetresTravelled = input.getDistance();
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

    public int getSource() {
        return Source;
    }
}