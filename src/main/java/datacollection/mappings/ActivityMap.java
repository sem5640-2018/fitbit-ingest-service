package datacollection.mappings;

import java.util.LinkedList;

class Mappings {
    private String mappingKey;
    private String source;

    String getSource() {
        return source;
    }

    String getMappingKey() {
        return mappingKey;
    }
}

public class ActivityMap {
    private int id;
    private String name;
    private LinkedList<Mappings> mappings;

    public String getName() {
        return name;
    }

    public LinkedList<String> getAllUsedIds() {
        LinkedList<String> allIds = new LinkedList<>();

        for (Mappings source : this.mappings) {
            if (!source.getSource().equals("Fitbit"))
                continue;
            allIds.add(source.getMappingKey());
        }

        return allIds;
    }

    int getID() {
        return id;
    }
}
