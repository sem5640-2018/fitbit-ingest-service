package datacollection;

import beans.ActivityMappingBean;

import javax.ejb.EJB;
import java.text.SimpleDateFormat;

public class HealthDataFormat {

    @EJB
    ActivityMappingBean mappingBean;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private String start_time;
    private String activity_type;
    private String quantity;
    private long duration;
    private float distance;

    public HealthDataFormat(Activity input) {
        ActivityMap map = mappingBean.getMapFromID(Long.toString(input.getActivityId()));

        start_time = sdf.format(input.getJavaDate());
        quantity = Long.toString(input.getSteps());
        // If we have a mapping for this item
        if (map == null) {
            activity_type = input.getDescription();
            duration = input.getDuration();
            distance = input.getDistance();
        } else {
            activity_type = map.getName();

            if (map.getUses_duration())
                duration = input.getDuration();

            if (map.getUses_distance())
                distance = input.getDistance();
        }
    }

    public float getDistance() {
        return distance;
    }

    public long getDuration() {
        return duration;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public String getStart_time() {
        return start_time;
    }
}
