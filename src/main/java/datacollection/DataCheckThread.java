package datacollection;

import persistence.TokenMap;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DataCheckThread implements Runnable {

    private ConcurrentLinkedQueue<TokenMap> input;
    private ConcurrentLinkedQueue<String> output;

    DataCheckThread(ConcurrentLinkedQueue<TokenMap> input, ConcurrentLinkedQueue<String> out) {
        // Create Shallow copy to the global linked queue
        this.input = input;
        this.output = out;
    }


    /**
     * This method is called automatically when the thread is started
     */
    public void run() {
        TokenMap toCheck = input.poll();

        while (toCheck != null) {
            requestActivityData(toCheck);
            toCheck = input.poll();
        }
    }

    /**
     * Function for sending a request for Activity Data to the Fitbit API.
     * @param tokenMap TokenMap for user we want data for.
     * @return JSON Array with activity data
     */
    private void requestActivityData(TokenMap tokenMap) {

        //TODO will send of API request for activity data
        String s = "some JSON to be parsed";

        output.add(s);
    }

}
