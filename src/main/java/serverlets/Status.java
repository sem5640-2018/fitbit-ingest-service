package serverlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This will act as the Status endpoint. Has scope to be extended if needed.
 *
 * @author James H Britton
 */
@WebServlet(name = "Status", urlPatterns = {"/api/status", "/api/Status"})
public class Status extends HttpServlet {

    /**
     * This is a simple function that just returns status code 200.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
