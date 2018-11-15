package datacollection;

import java.util.Date;

public class Activity {
    private long activityId;
    private long activityParentId;
    private long calories;
    private String description;
    private float distance;
    private long duration;
    private boolean hasStartTime;
    private boolean isFavorite;
    private long logId;
    private String name;
    private String startTime;
    private long steps;

    private Date javaDate;

    public Activity() {

    }

    public Date getJavaDate() {
        return javaDate;
    }

    public void setJavaDate(Date javaDate) {
        this.javaDate = javaDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }
}
