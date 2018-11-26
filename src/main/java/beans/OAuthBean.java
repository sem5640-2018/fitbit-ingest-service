package beans;

import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import scribe_java.GatekeeperApi;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class OAuthBean {

    @EJB
    EnvriomentVariableBean variableBean;

    private OAuth20Service fitbitService;
    private OAuth20Service aberfitnessService;

    public OAuthBean() {
        //Empty
        System.out.println("Constructing");
    }

    @PostConstruct
    public void init() {
        if (variableBean.isFitbitDataPresent()) {
            fitbitService = new ServiceBuilder(variableBean.getFitbitClientId())
                    .apiSecret(variableBean.getFitbitClientSecret())
                    .scope("activity profile")
                    .callback(variableBean.getFitbitClientCallback())
                    .state("fitbit_auth")
                    .build(FitbitApi20.instance());
        }
    }

    public OAuth20Service getFitbitService() {
        return fitbitService;
    }

    public void initGatekeeperService(String callback, String state) {
        if (variableBean.isAberfitnessDataPresent()) {
            aberfitnessService = new ServiceBuilder(variableBean.getAberfitnessClientId())
                    .apiSecret(variableBean.getAberfitnessClientSecret())
                    .scope("openid profile offline_access")
                    .callback(callback)
                    .state(state)
                    .build(GatekeeperApi.instance());
        }
    }

    public OAuth20Service getAberfitnessService() { return aberfitnessService; }
}
