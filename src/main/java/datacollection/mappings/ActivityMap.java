package datacollection.mappings;

import java.util.LinkedList;

class Mappings {
    private String MappingKey;
    private String Source;

    String getSource() {
        return Source;
    }

    String getMappingKey() {
        return MappingKey;
    }
}

public class ActivityMap {
    private int ID;
    private String Name;
    private LinkedList<Mappings> Mappings;

    public String getName() {
        return Name;
    }

    public LinkedList<String> getAllUsedIds() {
        LinkedList<String> allIds = new LinkedList<>();

        for (Mappings source : this.Mappings) {
            if (!source.getSource().equals("Fitbit"))
                continue;
            allIds.add(source.getMappingKey());
        }

        return allIds;
    }

    int getID() {
        return ID;
    }
}
