package scheduling;

import beans.OAuthBean;
import datacollection.FitbitDataCollector;
import datacollection.FitbitDataConverter;
import datacollection.FitbitDataProcessor;
import datacollection.ProcessedData;
import persistence.TokenMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Scheduling EJB for kicking of the scheduling tasks that will run once the application has started up.
 * @Author James H Britton
 * @Author "jhb15@aber,ac,uk"
 * @Version 0.1
 */
@Startup
@Singleton
public class SchedulingBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    OAuthBean oAuthBean;

    private final FitbitDataCollector collector = new FitbitDataCollector(oAuthBean);
    private final FitbitDataConverter converter = new FitbitDataConverter();
    private final FitbitDataProcessor processor = new FitbitDataProcessor();

    /**
     * This method is ran once the EJB is created.
     */
    @PostConstruct
    public void atStartup() {
        System.out.println("Scheduling EJB Initialised!");
    }

    /**
     * This method is ran every hour.
     */
    @Schedule(hour = "*/1", minute = "0", second = "0", persistent = false)
    public void getFitbitData() {
        List<TokenMap> allTokens = TokenMap.getAllTokenMap(em);

        if (allTokens == null || allTokens.size() > 0) {
            // @TODO Log the fact we have no tokens
            return;
        }

        // Retrieve all the JSON strings needed for processing
        ConcurrentLinkedQueue<ProcessedData> data = collector.getAllUsersInfo(allTokens.toArray(new TokenMap[0]));

        // Convert all our strings to usable objects
        data = converter.convertActivityData(data);

        // Process all out data
        processor.ProcessData(data);
    }

    /**
     * This method is ran every half hour.
     */
    @Schedule(hour= "*", minute = "*/30", second = "0", persistent = false)
    public void updateActivityMappings() {
        System.out.println("Method stub for Update Activity Mappings!");
        //TODO implement
    }

    /**
     * This method is ran once the EJB is destroyed.
     */
    @PreDestroy
    public void atEnd() {
        System.out.println("Destroying Scheduling EJB");
    }
}
