package datacollection;

class Mappings {
    private String SourceIdentifier;

    public String getSourceIdentifier() {
        return SourceIdentifier;
    }
}

class Source {
    private int Id;
    private Mappings Mappings;

    public int getId() {
        return Id;
    }

    public datacollection.Mappings getMappings() {
        return Mappings;
    }
}

public class ActivityMap {
    private int Id;
    private String Name;
    private Source Source;

    public String getName() {
        return Name;
    }

    public datacollection.Source getSource() {
        return Source;
    }

    public String getKey() {
        return Source.getMappings().getSourceIdentifier();
    }

    public int getId() {
        return Source.getId();
    }
}
