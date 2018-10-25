package servlets;

import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "Login", urlPatterns = { "/Login" } )
public class OAuthLogin extends HttpServlet{
    private String message;
    private OAuth20Service service;
    private static final String NETWORK_NAME = "Fitbit";
    private static final String PROTECTED_RESOURCE_URL = "https://api.fitbit.com/1/user/%s/profile.json";

    public void init() throws ServletException {
        // Do required initialization
        message = "=== Jack's OAuth Workflow ===";

        final String clientId = "22D4RT";
        final String clientSecret = "547470906547f1d7d0ffc7e55fc4a733";
        service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .scope("activity profile") // replace with desired scope
                //your callback URL to store and handle the authorization code sent by Fitbit
                .callback("http://localhost:8080/fitbit_ingest_service_Web_exploded/Login")
                .state("some_params")
                .build(FitbitApi20.instance());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Actual logic goes here.

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>");
        // Obtain the Authorization URL

        String str = request.getParameter("code");
        if (str != null) {
            try {
                final OAuth2AccessToken oauth2AccessToken = service.getAccessToken(str);
                out.println("Got the Access Token!");
                out.println("(if your curious it looks like this: " + oauth2AccessToken
                        + ", 'rawResponse'='" + oauth2AccessToken.getRawResponse() + "')");
                out.println();

                if (!(oauth2AccessToken instanceof FitBitOAuth2AccessToken)) {
                    out.println("oauth2AccessToken is not instance of FitBitOAuth2AccessToken. Strange enough. exit.");
                    return;
                }

                final FitBitOAuth2AccessToken accessToken = (FitBitOAuth2AccessToken) oauth2AccessToken;
                // Now let's go and ask for a protected resource!
                // This will get the profile for this user
                System.out.println("Now we're going to access a protected resource...");

                final OAuthRequest req = new OAuthRequest(Verb.GET,
                        String.format(PROTECTED_RESOURCE_URL, accessToken.getUserId()));
                req.addHeader("x-li-format", "json");

                service.signRequest(accessToken, req);

                final Response resp = service.execute(req);
                out.println();
                out.println(resp.getCode());
                out.println(resp.getBody());

                out.println();

                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final String authorizationUrl = service.getAuthorizationUrl();
        out.println("Now go and authorize ScribeJava here:");
        out.println(authorizationUrl);
        out.println("And paste the authorization code here");
        out.print(">>");


    }

    public void destroy() {
        // do nothing.
    }
}