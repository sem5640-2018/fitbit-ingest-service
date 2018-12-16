package datacollection;

import beans.ActivityMappingBean;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import config.AuthStorage;
import config.EnvironmentVariableClass;
import scribe_java.GatekeeperApi;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FitbitDataProcessor {
    private static final int threadCount = 4;

    ActivityMappingBean activityMappingBean;

    public FitbitDataProcessor(ActivityMappingBean activityMappingBean) {
        this.activityMappingBean = activityMappingBean;
    }

    /**
     * Creates a new Gatekeeper Scribe Service for use within the thread sending data to the Heath Data Repo.
     * @return initialized OAuth Service
     */
    private OAuth20Service getNewGatekeeperService() {
        return new ServiceBuilder(EnvironmentVariableClass.getAberfitnessClientId())
                .apiSecret(EnvironmentVariableClass.getAberfitnessClientSecret())
                .scope(AuthStorage.clientCredScope)
                .build(GatekeeperApi.instance());
    }

    public void ProcessSynchronous(ConcurrentLinkedQueue<ProcessedData> input) {
        AtomicInteger counter = new AtomicInteger();
        counter.set(0);
        Thread processThread = new Thread(new DataProcessThread(input, AuthStorage.getApplicationToken(), counter, activityMappingBean, getNewGatekeeperService()));
        processThread.start();

        try {
            processThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Sent " + counter.get() + ", data packets to health data repository");
    }

    public void ProcessData(ConcurrentLinkedQueue<ProcessedData> input) {
        Thread[] threads = new Thread[threadCount];
        AtomicInteger counter = new AtomicInteger();
        counter.set(0);

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new DataProcessThread(input, AuthStorage.getApplicationToken(), counter, activityMappingBean, getNewGatekeeperService()));
            threads[i].start();
        }

        for (int j = 0; j < threadCount; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Sent " + counter.get() + " data packets to health data repository");
    }
}
