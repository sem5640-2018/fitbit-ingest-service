package logging;

import beans.OAuthBean;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.EnvConfigException;

import javax.ejb.EJB;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Audit Helper for generating audit loggs to send off to the glados service.
 * @author James H Britton
 * @author 'jhb15@aber.ac.uk'
 * @version  0.1
 */
public class AuditHelper {

    @EJB
    private OAuthBean oAuthBean;

    private static String destUrl;
    private static String addAuditUrl;
    private static String serviceName;

    private Gson gson;

    /**
     * Constructor for the Audit Helper that initialises gson.
     */
    public AuditHelper() {
        gson = new GsonBuilder().create();
        serviceName = System.getenv("SERVICE_NAME");
        destUrl =  System.getenv("GLADOS_BASE_URL");
        addAuditUrl =  System.getenv("GLADOS_NEW_AUDIT");
        checkEnv();
    }

    private void checkEnv() {
        if (destUrl == null)
            throw new EnvConfigException("Glados Base URL environment variable not set!");
        if (addAuditUrl == null)
            throw new EnvConfigException("Glados add Audit url pattern environment variable not set!");
        if (serviceName == null)
            throw new EnvConfigException("The service name environment variable isn't set!");
    }


    /**
     * Sends an Audit Log to GLADOS using the API.
     * @param severity Audit Severity
     * @param msg Audit Message
     * @param currentUser user id of logged in user
     * @param accessToken access token for current user
     * @return 201 created, 401 unauthorised, 500 server error, 999 Exception Thrown
     */
    public int sendAudit(String severity, String msg, String currentUser, String accessToken) {
        try {

            String content = "[" + severity + "]: " + msg;
            AuditObj audObj = new AuditObj(content, serviceName, new Date(), currentUser);
            String outJson = gson.toJson(audObj);

            System.out.println("Audit JSON to be sent to GLADOS: " + outJson);
            final OAuthRequest request = new OAuthRequest(Verb.POST, destUrl + addAuditUrl);
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
            request.addBodyParameter("payload", outJson);
            oAuthBean.getAberfitnessService().signRequest(accessToken, request);

            final Response response = oAuthBean.getAberfitnessService().execute(request);

            return response.getCode();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 999;
    }
}

/**
 * Audit Object class used with GSON.
 */
class AuditObj {

    private String content;
    private String serviceName;
    private Date timestamp;
    private String userId;

    public AuditObj(String content, String serviceName, Date timestamp, String userId) {
        this.content = content;
        this.serviceName = serviceName;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AuditObj{" +
                "content='" + content + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                '}';
    }
}
