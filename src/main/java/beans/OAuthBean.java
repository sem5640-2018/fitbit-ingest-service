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
        if (EnvironmentVariableBean.isFitbitDataPresent()) {
            fitbitService = new ServiceBuilder(EnvironmentVariableBean.getFitbitClientId())
                    .apiSecret(EnvironmentVariableBean.getFitbitClientSecret())
                    .scope("activity profile")
                    .callback(EnvironmentVariableBean.getFitbitClientCallback())
                    .state("fitbit_auth")
                    .build(FitbitApi20.instance());
            return fitbitService;
        }
    }

    public OAuth20Service getFitbitService() {
        return fitbitService == null ? createFitbitClient() : fitbitService;
    }

    public void initGatekeeperService(String callback, String state) {
        if (EnvironmentVariableBean.isAberfitnessDataPresent()) {
            aberfitnessService = new ServiceBuilder(EnvironmentVariableBean.getAberfitnessClientId())
                    .apiSecret(EnvironmentVariableBean.getAberfitnessClientSecret())
                    .scope("openid profile offline_access")
                    .callback(callback)
                    .state(state)
                    .build(GatekeeperApi.instance());
        }
    }

    public OAuth20Service getAberfitnessService() { return aberfitnessService; }
}
