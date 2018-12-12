package serverlets;

import beans.ActivityMappingBean;
import beans.OAuthBean;
import datacollection.FitbitDataCollector;
import datacollection.FitbitDataConverter;
import datacollection.FitbitDataProcessor;
import datacollection.ProcessedData;
import persistence.TokenMap;
import persistence.TokenMapDAO;
import scribe_java.GatekeeperLogin;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Serverlet providing the Prompt api Endpoint for the Fitbit Ingest Service
 *
 * @author James H Britton
 * @author Jack Thomson
 */
@WebServlet(name = "Prompt", urlPatterns = {"/api/Prompt"})
public class Prompt extends HttpServlet {

    @PersistenceContext
    private EntityManager em;

    @EJB
    OAuthBean oAuthBean;

    @EJB
    GatekeeperLogin gatekeeperLogin;

    @EJB
    TokenMapDAO tokenMapDAO;

    @EJB
    ActivityMappingBean activityMappingBean;

    private static final String paramName = "userId";

    private FitbitDataCollector collector;
    private final FitbitDataConverter converter = new FitbitDataConverter();
    private FitbitDataProcessor processor;

    @Override
    public void init() {
        collector = new FitbitDataCollector(oAuthBean, tokenMapDAO);
        processor = new FitbitDataProcessor(activityMappingBean);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userId;
        Map<String, String[]> paramMap = request.getParameterMap();
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String authHead[] = authorization.split(" ", 2);

            if (authHead[1] == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No Access Token in Auth Header!");
                return;
            }

            if (!paramMap.containsKey("userId")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No User ID Parameter!");
                return;
            }

            userId = paramMap.get(paramName)[0];

            if (gatekeeperLogin.isInvalidAccessToken(authHead[1], null)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access Token!");
                return;
            }

            TokenMap userToken = TokenMap.getTokenMap(em, userId);

            if (userToken == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No Fitbit Access Given");
                return;
            }

            // Retrieve all the JSON strings needed for processing
            ConcurrentLinkedQueue<ProcessedData> data = collector.getAllUsersSynchronous(new TokenMap[]{userToken});

            // Convert all our strings to usable objects
            data = converter.convertActivityData(data);

            // Process all out data
            processor.ProcessSynchronous(data);

            userToken.setLastAccessed(new Date());
            tokenMapDAO.update(userToken);

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authorization Header Not Set or Not Bearer");
        }
    }

}
