package logging;

import beans.OAuthBean;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.AuthStorage;
import config.EnvironmentVariableClass;
import scribe_java.GatekeeperLogin;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.Date;

/**
 * Audit Helper for generating audit loggs to send off to the glados service.
 * @author James H Britton
 * @author 'jhb15@aber.ac.uk'
 * @version  0.1
 */
@Singleton
public class AuditHelper implements Serializable {

    @EJB
    OAuthBean oAuthBean;

    @EJB
    GatekeeperLogin gatekeeperLogin;

    private Gson gson;

    /**
     * Constructor for the Audit Helper that initialises gson.
     */
    public AuditHelper() {
        gson = new GsonBuilder().create();
    }

    private GatekeeperOAuth2AccessToken getAccessToken() {
        GatekeeperOAuth2AccessToken retAT = AuthStorage.getApplicationToken();
        if (retAT != null &&  gatekeeperLogin.validateAccessToken(retAT.getAccessToken(), AuthStorage.clientCredScope.split(" "))) {
            return retAT;
        } else {
            System.out.println("[AuditHelper.getAccessToken] Invalid Client Cred Access Token, Retrieving New One");
            gatekeeperLogin.getGatekeeperGrantAccessToken(EnvironmentVariableClass.getFitbitIngestLoginUrl(), "gateAccess");
            return AuthStorage.getApplicationToken();
        }
    }


    /**
     * Sends an Audit Log to GLADOS using the API.
     * @param msg Audit Message
     * @param detail Audit Details
     * @param currentUser user id of which the Audit Log Belongs to.
     * @return 201 created, 401 unauthorised, 500 server error, 999 Exception Thrown
     */
    public int sendAudit(String msg, String detail, String currentUser) {
        try {
            GatekeeperOAuth2AccessToken accessToken = getAccessToken();

            String content = "[" + msg + "]: " + detail;
            AuditObj audObj = new AuditObj(content, EnvironmentVariableClass.getServiceName(), new Date(), currentUser);
            String outJson = gson.toJson(audObj);

            System.out.println("Audit JSON to be sent to GLADOS: " + outJson);
            final OAuthRequest request = new OAuthRequest(Verb.POST, EnvironmentVariableClass.getGladosAddAudit());
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
            request.setPayload(outJson);
            oAuthBean.getAberfitnessService().signRequest(accessToken, request);

            final Response response = oAuthBean.getAberfitnessService().execute(request);

            return response.getCode();

        } catch (Exception e) {
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
