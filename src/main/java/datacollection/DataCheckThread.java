package datacollection;

import persistence.TokenMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataCheckThread implements Runnable {
    private ConcurrentLinkedQueue<TokenMap> input;
    private ConcurrentLinkedQueue<String> output;
    private String address;

    DataCheckThread(ConcurrentLinkedQueue<TokenMap> input, ConcurrentLinkedQueue<String> out) {
        // Create Shallow copy to the global linked queue
        this.input = input;
        this.output = out;

        // Needed for API requests with FitBit
        Date date = new Date();
        String textDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        this.address = "https://api.fitbit.com/1/user/-/activities/date/" + textDate + ".json";

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
        /*final OAuth2AccessToken oauth2AccessToken = new FitBitOAuth2AccessToken(tokenMap.getAccessToken());

        if (!(oauth2AccessToken instanceof FitBitOAuth2AccessToken)) {
            return;
        }

        final FitBitOAuth2AccessToken accessToken = (FitBitOAuth2AccessToken) oauth2AccessToken;

        final OAuthRequest request = new OAuthRequest(Verb.GET,
                String.format(this.address, accessToken.getUserId()));
        request.addHeader("x-li-format", "json");

        // TODO with a global service to access
        //service.signRequest(accessToken, request);

        //TODO will send of API request for activity data
        String s = "some JSON to be parsed";

*/
    }

}
