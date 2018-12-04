package datacollection;

import beans.OAuthBean;
import persistence.TokenMap;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class that handles making API calls to Fitbit. Will probably use ScribeJava library to achive this. Currently
 * Unimplemented.
 *
 * @author James H Britton
 * @author "jhb15@aber.ac.uk"
 * @version 0.1
 */
public class FitbitDataCollector {
    private static final int threadCount = 4;
    private OAuthBean oAuthBean;

    public FitbitDataCollector(OAuthBean oAuthBean) {
        this.oAuthBean = oAuthBean;
    }

    public ConcurrentLinkedQueue<ProcessedData> getAllUsersSynchronous(TokenMap[] tokenMap) {
        ConcurrentLinkedQueue<TokenMap> input = new ConcurrentLinkedQueue<>(Arrays.asList(tokenMap));
        ConcurrentLinkedQueue<ProcessedData> output = new ConcurrentLinkedQueue<>();

        DataCheckThread dataCheckThread = new DataCheckThread(input, output, oAuthBean);
        dataCheckThread.run();

        return output;
    }

    /**
     * This method takes in all the tokens and runs through these concurrently
     *
     * @param tokenMap this is an array of all tokens in the database
     * @return a linked list of the data ready for it to be processed
     */
    public ConcurrentLinkedQueue<ProcessedData> getAllUsersInfo(TokenMap[] tokenMap) {
        ConcurrentLinkedQueue<TokenMap> input = new ConcurrentLinkedQueue<>(Arrays.asList(tokenMap));
        ConcurrentLinkedQueue<ProcessedData> output = new ConcurrentLinkedQueue<>();

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new DataCheckThread(input, output, oAuthBean));
            threads[i].start();
        }

        for (int j = 0; j < threadCount; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return output;
    }
}
