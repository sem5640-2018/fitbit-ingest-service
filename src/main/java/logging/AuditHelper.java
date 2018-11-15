package logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Audit Helper for generating audit loggs to send off to the glados service.
 * @Author James H Britton
 * @Author 'jhb15@aber.ac.uk'
 * @Version 0.1
 */
public class AuditHelper {

    private static String destUrl = "glados.aberfitness.biz";
    private static String addAuditUrl = "/audit/new";
    private static String serviceName = "fitbit-ingest-service"; //TODO needs to be an envirment variable probably.
    private static String currentUser = "test_user"; //TODO needs to come from logged in user.

    private Gson gson;

    /**
     * Constructor for the Audit Helper that initialises gson.
     */
    public AuditHelper() {
        gson = new GsonBuilder().create();
    }

    /**
     * Sends an Audit Log to GLADOS using the API.
     * @param severity Audit Severity
     * @param msg Audit Message
     */
    public int sendAudit(String severity, String msg) {

        String content = "[" + severity + "]: " + msg;
        AuditObj audObj = new AuditObj(content, serviceName, new Date(), currentUser);
        String outJson = gson.toJson(audObj);

        System.out.println("Audit JSON to be sent to GLADOS: " + outJson);
        //TODO implement API connection!
        return 501; //Unimplemented.
    }
}
