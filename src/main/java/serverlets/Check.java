package serverlets;

import persistence.TokenMap;
import persistence.TokenMapDAO;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet will act as the check endpoint that will allow other microservices to check id a user id belongs to a
 * user who had authorised Fitbit access.
 * @Author James H Britton
 */
@WebServlet(name = "Check", urlPatterns = {"/Check"})
public class Check extends HttpServlet {

    @EJB
    private TokenMapDAO tokenMapDAO;

    private static final String paramName = "userId";

    /**
     * This function will execute Get request, for this serverlet it will mean checking for a Token Map and returning a
     * status code 200 for true and 204 for false.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter(paramName);

        TokenMap tokenMap = tokenMapDAO.getByUid(userId);
        if (tokenMap != null)
            response.sendError(HttpServletResponse.SC_OK);
        else
            response.sendError(HttpServletResponse.SC_NO_CONTENT); //TODO Not sure if this is appropriate for not connected.
    }

}
