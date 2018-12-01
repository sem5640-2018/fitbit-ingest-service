package datacollection.mappings;

import java.util.List;

public class FitBitJSON {
    private List<Activity> activities;
    private Goals goals;
    private Summary summary;
    private String fromDate;

    public List<Activity> getActivities() {
        return activities;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public Summary getSummary() {
        return summary;
    }

    public Goals getGoals() {
        return goals;
    }
}
