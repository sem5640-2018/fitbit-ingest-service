package beans;

import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class OAuthBean {

    @Inject
    EnvriomentVariableBean variableBean;

    private OAuth20Service service;

    public OAuthBean() {
        service = new ServiceBuilder(variableBean.getFitbitClientId())
                .apiSecret(variableBean.getFitbitClientSecret())
                .scope("activity profile")
                .callback(variableBean.getFitbitClientCallback())
                .state("some_params")
                .build(FitbitApi20.instance());
    }

    public OAuth20Service getService() {
        return service;
    }
}
