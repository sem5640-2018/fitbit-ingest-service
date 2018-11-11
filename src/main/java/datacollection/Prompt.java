package datacollection;

import persistence.TokenMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet(name = "Prompt", urlPatterns = {"/Prompt", "/GetFitbit"})
public class Prompt extends HttpServlet {

    @PersistenceContext
    EntityManager em;

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

        TokenMap tm = new TokenMap();
        tm.setAccessToken("testAT");
        tm.setRefreshToken("testRT");
        tm.setUserID("testUID");
        tm.setExpiresIn(3600);
        tm.setFitbitUid("testFitbitUID");
        tm.setLastAccessed(new Date());

        em.getTransaction().begin();
        em.persist(tm);
        em.getTransaction().commit();

        PrintWriter out = response.getWriter();
        out.println("<p>Manual Prompt Unimplemented!</p>");

        out.close();

    }

}
