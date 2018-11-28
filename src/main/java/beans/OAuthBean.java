package beans;

import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import scribe_java.GatekeeperApi;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

@Singleton
public class OAuthBean {

    private OAuth20Service fitbitService;
    private OAuth20Service aberfitnessService;

    public OAuthBean() {
        //Empty
        System.out.println("Constructing");
    }

    @PostConstruct
    public void init() {
        fitbitService = createFitbitClient();
    }

    private OAuth20Service createFitbitClient() {
        if (EnvironmentVariableClass.isFitbitDataPresent()) {
            fitbitService = new ServiceBuilder(EnvironmentVariableClass.getFitbitClientId())
                    .apiSecret(EnvironmentVariableClass.getFitbitClientSecret())
                    .scope("activity profile")
                    .callback(EnvironmentVariableClass.getFitbitIngestLoginUrl())
                    .state("some_params")
                    .build(FitbitApi20.instance());
            return fitbitService;
        }
        return null;
    }

    public void initGatekeeperService(String callback, String state) {
        if (aberfitnessService != null && aberfitnessService.getCallback().equals(callback))
            return;

        if (EnvironmentVariableClass.isAberfitnessDataPresent()) {
            aberfitnessService = new ServiceBuilder(EnvironmentVariableClass.getAberfitnessClientId())
                    .apiSecret(EnvironmentVariableClass.getAberfitnessClientSecret())
                    .scope("openid profile offline_access")
                    .callback(callback)
                    .state(state)
                    .build(GatekeeperApi.instance());
        }
    }

    public OAuth20Service getFitbitService() {
        return fitbitService == null ? createFitbitClient() : fitbitService;
    }

    public OAuth20Service getAberfitnessService() {
        return aberfitnessService;
    }
}