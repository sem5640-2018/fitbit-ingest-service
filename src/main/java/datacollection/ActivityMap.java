package datacollection;

import java.util.LinkedList;
import java.util.List;

class SourceActivities {
    private String ServiceActivityId;

    public String getSourceIdentifier() {
        return ServiceActivityId;
    }
}

class Sources {
    private int Id;
    private LinkedList<SourceActivities> SourceActivities;
    private String Source;

    protected List<String> getAllIds() {
        LinkedList<String> ids = new LinkedList<>();
        for (SourceActivities activities: this.SourceActivities) {
            ids.add(activities.getSourceIdentifier());
        }

        return ids;
    }

    public int getId() {
        return Id;
    }

    public String getSource() {
        return Source;
    }
}

public class ActivityMap {
    private int ID;
    private String Name;
    private LinkedList<Sources> Sources;

    public String getName() {
        return Name;
    }

    public LinkedList<String> getAllUsedIds() {
        LinkedList<String> allIds = new LinkedList<>();

        for (Sources source: this.Sources) {
            if (!source.getSource().equals("Fitbit"))
                continue;
            allIds.addAll(source.getAllIds());
        }

        return allIds;
    }

    public int getID() {
        return ID;
    }
}
