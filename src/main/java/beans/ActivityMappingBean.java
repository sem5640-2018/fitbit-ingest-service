package beans;

import datacollection.ActivityMap;

import javax.ejb.Singleton;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton(name = "ActivityMappingBean")
public class ActivityMappingBean {
    ConcurrentLinkedQueue<ActivityMap> mappings;

    public ActivityMappingBean() {
        mappings = new ConcurrentLinkedQueue<>();
    }

    public void UpdateMappings(List<ActivityMap> input) {
        mappings.clear();
        mappings.addAll(input);
    }

    public ActivityMap getMapFromID(String idToCheck) {
        for (ActivityMap activityMap: mappings) {
            if (activityMap.isMatching(idToCheck))
                return activityMap;
        }
        return null;
    }
}
