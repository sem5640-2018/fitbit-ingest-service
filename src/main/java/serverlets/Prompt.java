package serverlets;

import beans.OAuthBean;
import config.EnvironmentVariableClass;
import datacollection.FitbitDataCollector;
import datacollection.FitbitDataConverter;
import datacollection.FitbitDataProcessor;
import datacollection.ProcessedData;
import persistence.TokenMap;
import scribe_java.GatekeeperLogin;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebServlet(name = "Prompt", urlPatterns = {"/Prompt", "/GetFitbit"})
public class Prompt extends HttpServlet {

    @PersistenceContext
    private EntityManager em;

    @EJB
    OAuthBean oAuthBean;

    @EJB
    GatekeeperLogin gatekeeperLogin;

    private final FitbitDataCollector collector = new FitbitDataCollector(oAuthBean);
    private final FitbitDataConverter converter = new FitbitDataConverter();
    private final FitbitDataProcessor processor = new FitbitDataProcessor();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String state = request.getParameter("state");
        String userID = null;

        if (state != null &&state.equals("gateAccess")){
            userID = gatekeeperLogin.getUser_id();
        }

        if(userID == null) {
            gatekeeperLogin.redirectToGatekeeper(response, EnvironmentVariableClass.getFitbitIngestPromptUrl(), "gateAccess");
            return;
        }

        TokenMap userToken = TokenMap.getTokenMap(em, userID);

        // Retrieve all the JSON strings needed for processing
        ConcurrentLinkedQueue<ProcessedData> data = collector.getAllUsersSynchronous(new TokenMap[]{userToken});

        // Convert all our strings to usable objects
        data = converter.convertActivityData(data);

        // Process all out data
        processor.ProcessSynchronous(data);

        response.sendRedirect(EnvironmentVariableClass.getHeathDataRepoUrl());
    }

}
