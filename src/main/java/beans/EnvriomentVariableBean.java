package beans;

import javax.ejb.Singleton;

@Singleton(name = "EnvriomentVariableEJB")
public class EnvriomentVariableBean {

    // Fitbit Info
    private String fitbitClientId;
    private String fitbitClientSecret;
    private String fitbitClientCallback;

    //Aberfitness Info
    private String aberfitnessClientId;
    private String aberfitnessClientSecret;
    private String aberfitnessClientCallback;

    public EnvriomentVariableBean() {
        //Retrieval of Fitbit Data
        fitbitClientId = System.getenv("fitbitClientId");
        fitbitClientSecret = System.getenv("fitbitClientSecret");
        fitbitClientCallback = System.getenv("fitbitClientCallback");

        //Retrival of Aberfitness Data
        aberfitnessClientId = System.getenv("ABERFITNESS_CLI_ID");
        aberfitnessClientSecret = System.getenv("ABERFITNESS_CLI_SECRET");
        aberfitnessClientCallback = System.getenv("ABERFITNESS_CLI_CALLBACK");
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

    public boolean isAberfitnessDataPresent() {
        return aberfitnessClientId != null && aberfitnessClientSecret != null && aberfitnessClientCallback != null;
    }

    public boolean isFitbitDataPresent() {
        return fitbitClientId != null && fitbitClientSecret != null && fitbitClientCallback != null;
    }
}
