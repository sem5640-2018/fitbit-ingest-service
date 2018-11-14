package datacollection;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import persistence.TokenMap;
import persistence.TokenMapDAO;

@WebServlet(name = "Prompt", urlPatterns = {"/Prompt", "/GetFitbit"})
public class Prompt extends HttpServlet {

    @EJB
    TokenMapDAO tokenMapDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //TODO Code to trigger data retrieval
        //Get current user
        //if ok
            //Get Data
            //Wait till finfished
            //redirct user back to origin
        //else
            //Invalid Request response
        
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);

    }

}
