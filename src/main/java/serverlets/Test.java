/*package serverlets;

import logging.AuditHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Test", urlPatterns = {"/Test"})
public class Test extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuditHelper auditHelper = new AuditHelper();
        try {
            auditHelper.sendAudit("TEST1", "some test message1", "testUser", "testToken");
            Thread.sleep(1000);
            auditHelper.sendAudit("TEST2", "some test message2", "testUser", "testToken");
            Thread.sleep(1000);
            auditHelper.sendAudit("TEST3", "some test message3", "testUser", "testToken");
            Thread.sleep(1000);
            auditHelper.sendAudit("TEST4", "some test message4", "testUser", "testToken");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
*/