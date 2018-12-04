package beans;

import datacollection.mappings.ActivityMap;

import javax.ejb.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton(name = "ActivityMappingBean")
public class ActivityMappingBean {
    private Map<String, ActivityMap> mappings;

    public ActivityMappingBean() {
        mappings = Collections.synchronizedMap(new HashMap<>());
    }

    public void UpdateMappings(ActivityMap[] input) {
        mappings.clear();
        for (ActivityMap map : input) {
            for (String keys : map.getAllUsedIds()) {
                mappings.put(keys, map);
            }
        }
    }

    public ActivityMap getMapFromID(String idToCheck) {
        return mappings.get(idToCheck);
    }
}
