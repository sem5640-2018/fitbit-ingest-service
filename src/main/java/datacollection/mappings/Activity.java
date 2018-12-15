package datacollection.mappings;

import java.util.Date;

public class Activity {
    private long activityId;
    private long activityParentId;
    private long calories;
    private String description;
    private float distance;
    private long duration;
    private long logId;
    private String name;
    private String startTime;
    private long steps;

    private String userID;

    private Date javaDate;

    public Activity() {

    }

    public void setUserID(String id) {
        userID = id;
    }

    public String getUserID() {
        return userID;
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

    public String getDescription() {
        return description;
    }

    public long getSteps() {
        return steps;
    }

    public float getDistance() {
        return distance;
    }

    public long getActivityId() {
        return activityId;
    }

    public long getActivityParentId() {
        return activityParentId;
    }

    public long getCalories() {
        return calories;
    }

    public long getLogId() {
        return logId;
    }

    public String getName() {
        return name;
    }
}
