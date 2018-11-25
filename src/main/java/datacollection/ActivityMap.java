package datacollection;

import java.util.List;

public class ActivityMap {
    private String name;
    private String quantity_unit;
    private Boolean uses_duration;
    private Boolean uses_distance;
    private List<String> fitbit_activity_ids;

    public List<String> getFitbit_activity_ids() {
        return fitbit_activity_ids;
    }

    public Boolean getUses_distance() {
        return uses_distance;
    }

    public Boolean getUses_duration() {
        return uses_duration;
    }

    public String getQuantity_unit() {
        return quantity_unit;
    }

    public String getName() {
        return name;
    }
}
