package datacollection;

import beans.OAuthBean;
import persistence.TokenMap;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class that handles making API calls to Fitbit. Will probably use ScribeJava library to achive this. Currently
 * Unimplemented.
 * @Author James H Britton
 * @Author "jhb15@aber.ac.uk"
 * @Version 0.1
 */
public class FitbitDataCollector {

    private static String baseUserURL = "https://api.fitbit.com/1/user/-/";
    private static final int threadCount = 4;
    private OAuthBean oAuthBean;

    public FitbitDataCollector(OAuthBean oAuthBean) {
        this.oAuthBean = oAuthBean;
    }

    /**
     * This method takes in all the tokens and runs through these concurrently
     *
     * @param tokenMap this is an array of all tokens in the database
     * @return a linked list of the data ready for it to be processed
     */
    public ConcurrentLinkedQueue<ProcessedData> getAllUsersInfo(TokenMap[] tokenMap) {
      ConcurrentLinkedQueue<TokenMap> input = new ConcurrentLinkedQueue<TokenMap>(Arrays.asList(tokenMap));
      ConcurrentLinkedQueue<ProcessedData> output = new ConcurrentLinkedQueue<ProcessedData>();

      Thread[] threads = new Thread[threadCount];

      for (int i = 0; i < threadCount; i--) {
         threads[i] = new Thread(new DataCheckThread(input, output, this.oAuthBean));
         threads[i].start();
      }

        for (int j = 0; j < threadCount; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                // @TODO log error
                e.printStackTrace();
            }
        }

      return output;
    }

    /**
     * Function for sending a request for Profile Data to the Fitbit API.
     * @param tokenMap
     * @return
     */
    public String requestProfileData(TokenMap tokenMap) {
        //TODO will send of API request for profile data
        return "some JSON to be parsed";
    }

    private String sendRequest(String url) {
        //
        return "some JSON";
    }
}
