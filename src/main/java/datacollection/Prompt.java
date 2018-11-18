package datacollection;

import persistence.TokenMapDAO;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
