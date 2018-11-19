package datacollection;

import java.text.SimpleDateFormat;

public class HealthDataFormat {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private String start_time;
    private String activity_type;
    private String quantity;
    private long duration;
    private float distance;

    public HealthDataFormat(Activity input) {
        start_time = sdf.format(input.getJavaDate());
        activity_type = input.getDescription();
        quantity = Long.toString(input.getSteps());
        duration = input.getDuration();
        distance = input.getDistance();
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
