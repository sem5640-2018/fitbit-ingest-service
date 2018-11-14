package beans;

import javax.ejb.Singleton;

@Singleton(name = "EnvriomentVariableEJB")
public class EnvriomentVariableBean {

    // Fitbit Info
    private String fitbitClientId;
    private String fitbitClientSecret;
    private String fitbitClientCallback;

    public EnvriomentVariableBean() {
        fitbitClientId = System.getenv("fitbitClientId");
        fitbitClientSecret = System.getenv("fitbitClientSecret");
        fitbitClientCallback = System.getenv("fitbitClientCallback");
    }

    public String getFitbitClientSecret() {
        return fitbitClientSecret;
    }

    public String getFitbitClientId() {
        return fitbitClientId;
    }

    public String getFitbitClientCallback() {
        return fitbitClientCallback;
    }
}
