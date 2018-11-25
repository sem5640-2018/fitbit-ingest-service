package beans;

import javax.ejb.Singleton;

@Singleton(name = "EnvironmentVariableEJB")
public class EnvironmentVariableBean {

    // Fitbit Info
    private static final String fitbitClientId = System.getenv("fitbitClientId");
    private static final String fitbitClientSecret = System.getenv("fitbitClientSecret");
    private static final String fitbitClientCallback = System.getenv("fitbitClientCallback");

    //Aberfitness Info
    private static final String aberfitnessClientId = System.getenv("ABERFITNESS_CLI_ID");
    private static final String aberfitnessClientSecret = System.getenv("ABERFITNESS_CLI_SECRET");
    private static final String aberfitnessClientCallback = System.getenv("ABERFITNESS_CLI_CALLBACK");
    private static final String aberfitnessActivityEndPoint = System.getenv("ABERFITNESS_CLI_CALLBACK");


    public EnvironmentVariableBean() {
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

    public String getAberfitnessClientId() {
        return aberfitnessClientId;
    }

    public String getAberfitnessClientSecret() {
        return aberfitnessClientSecret;
    }

    public String getAberfitnessClientCallback() {
        return aberfitnessClientCallback;
    }

    public String getAberfitnessActivityEndPoint() {
        return aberfitnessActivityEndPoint;
    }

    public boolean isAberfitnessDataPresent() {
        return aberfitnessClientId != null && aberfitnessClientSecret != null && aberfitnessClientCallback != null;
    }

    public boolean isFitbitDataPresent() {
        return fitbitClientId != null && fitbitClientSecret != null && fitbitClientCallback != null;
    }
}
