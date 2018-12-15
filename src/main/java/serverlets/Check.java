package serverlets;

import persistence.TokenMap;
import persistence.TokenMapDAO;
import scribe_java.AberFitnessClientLogin;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * This servlet will act as the check endpoint that will allow other microservices to check id a user id belongs to a
 * user who had authorised Fitbit access.
 *
 * @author James H Britton
 */
@WebServlet(name = "Check", urlPatterns = {"/api/Check"})
public class Check extends HttpServlet {

    @EJB
    private TokenMapDAO tokenMapDAO;

    private static final String paramName = "userId";

    /**
     * This function will execute Get request, for this serverlet it will mean checking for a Token Map and returning a
     * status code 200 for true and 204 for false.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException if can't access the request header
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId;
        Map<String, String[]> paramMap = request.getParameterMap();
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String authHead[] = authorization.split(" ", 2);

            if (authHead[1] == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No Access Token Parameter!");
                return;
            }

            if (!paramMap.containsKey(paramName)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No User ID Parameter!");
                return;
            }

            userId = paramMap.get(paramName)[0];

            if (AberFitnessClientLogin.isInvalidAccessToken(authHead[1], null)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access Token!");
                return;
            }

            TokenMap tokenMap = tokenMapDAO.getByUid(userId);

            if (tokenMap != null)
                response.setStatus(HttpServletResponse.SC_OK);
            else
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authorization Header Not Set or Not Bearer");
        }

    }

}
