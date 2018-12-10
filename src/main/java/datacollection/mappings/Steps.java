package datacollection.mappings;

import java.util.Date;

public class Steps {
    private Date dateTime;
    private long value;

    public Date getDateTime() {
        return dateTime;
    }

    public long getValue() {
        return value;
    }

    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
