package datacollection;

import config.AuthStorage;
import scribe_java.gatekeeper.GatekeeperOAuth2AccessToken;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FitbitDataProcessor {
    private static final int threadCount = 4;

    /**
     * Function for generating the String that will be placed in the Authorisation Header of any outbound requests in the
     * a DataProcessThread.
     * @return String example "Bearer someAccessToken"
     */
    private String generateBearerString() {
        GatekeeperOAuth2AccessToken at = AuthStorage.getApplicationToken();
        if (at == null)
            return "";
        return at.getTokenType() + " " + at.getAccessToken();
    }

    public void ProcessSynchronous(ConcurrentLinkedQueue<ProcessedData> input) {
        DataProcessThread processThread = new DataProcessThread(input, generateBearerString());
        processThread.run();
    }

    public void ProcessData(ConcurrentLinkedQueue<ProcessedData> input) {
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new DataProcessThread(input, generateBearerString()));
            threads[i].start();
        }

        for (int j = 0; j < threadCount; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
