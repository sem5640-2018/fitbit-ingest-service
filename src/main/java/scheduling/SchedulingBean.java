package scheduling;

import beans.OAuthBean;
import datacollection.FitbitDataCollector;
import persistence.TokenMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    @Inject
    OAuthBean oAuthBean;

    private final FitbitDataCollector collector = new FitbitDataCollector(oAuthBean);

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

        if (allTokens == null) {
            // @TODO Log the fact we have no tokens
            return;
        }

        collector.getAllUsersInfo(allTokens.toArray(new TokenMap[0]));
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
