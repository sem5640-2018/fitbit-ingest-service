package scheduling;

import beans.ActivityMappingBean;
import beans.OAuthBean;
import config.AuthStorage;
import datacollection.FitbitDataCollector;
import datacollection.FitbitDataProcessor;
import datacollection.FitbitDataConverter;
import datacollection.ActivityMapLoading;
import datacollection.ProcessedData;
import datacollection.mappings.ActivityMap;
import logging.AuditHelper;
import persistence.TokenMap;
import persistence.TokenMapDAO;
import scribe_java.GatekeeperLogin;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Scheduling EJB for kicking of the scheduling tasks that will run once the application has started up.
 *
 * @author James H Britton
 * @author "jhb15@aber,ac,uk"
 * @version 0.1
 */
@Startup
@Singleton
public class SchedulingBean {

    @EJB
    OAuthBean oAuthBean;

    @EJB
    ActivityMappingBean activityMappingBean;

    @EJB
    TokenMapDAO tokenMapDAO;

    @EJB
    GatekeeperLogin gatekeeperLogin;

    private FitbitDataCollector collector;
    private final FitbitDataConverter converter = new FitbitDataConverter();
    private FitbitDataProcessor processor;
    private final ActivityMapLoading loading = new ActivityMapLoading();

    /**
     * This method is ran once the EJB is created.
     */
    @PostConstruct
    public void atStartup() {
        System.out.println("Scheduling EJB Initialised!");
        collector = new FitbitDataCollector(oAuthBean, tokenMapDAO);
        processor = new FitbitDataProcessor(activityMappingBean);
        updateClientAccessToken();

        // These are needed on start up.
        updateActivityMappings();
    }

    /**
     * Function for storing a Client Credential Access Token in the AuthStorage static class
     */
    @Schedule(hour = "*", minute = "*/30", persistent = false)
    private void updateClientAccessToken() {
        AuthStorage.setApplicationToken(gatekeeperLogin.getAccessToken());
    }

    /**
     * This method is ran every hour.
     */
    @Schedule(hour = "*/1", minute = "1", persistent = false)
    public void getFitbitData() {
        System.out.println("Starting Get Fitbit Data Task");
        List<TokenMap> allTokens = tokenMapDAO.getAll();

        if (allTokens == null || allTokens.size() <= 0) {
            System.out.println("Not Fitbit Credentials to Use!");
            return;
        }

        // Retrieve all the JSON strings needed for processing
        ConcurrentLinkedQueue<ProcessedData> data = collector.getAllUsersInfo(allTokens.toArray(new TokenMap[0]));

        // Convert all our strings to usable objects
        data = converter.convertActivityData(data);

        // Process all out data
        processor.ProcessData(data);

        AuditHelper auditHelper = new AuditHelper();
        // Update last accessed
        Date now = new Date();
        for (TokenMap map : allTokens) {
            map.setLastAccessed(now);
            tokenMapDAO.update(map);
            auditHelper.sendAudit(AuditHelper.FITBIT_DATA_GRAB_MSG, "Collected at: " + map.getLastAccessed(), map.getUserID());
        }
    }

    /**
     * This method is ran every half hour.
     */
    @Schedule(hour = "*", minute = "*/30", persistent = false)
    public void updateActivityMappings() {
        System.out.println("Starting Get Activity Mapping Task");
        GatekeeperOAuth2AccessToken accessToken = gatekeeperLogin.getAccessToken();
        ActivityMap[] map = loading.checkMappings(accessToken);
        if (map == null) return;

        activityMappingBean.UpdateMappings(map);
    }

    /**
     * This method is ran once the EJB is destroyed.
     */
    @PreDestroy
    public void atEnd() {
        System.out.println("Destroying Scheduling EJB");
    }
}
