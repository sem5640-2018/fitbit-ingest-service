package datacollection.mappings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FitbitSteps {
    @SerializedName("activities-steps")
    private List<Steps> activities;

    public List<Steps> getActivities() {
        return activities;
    }
}
