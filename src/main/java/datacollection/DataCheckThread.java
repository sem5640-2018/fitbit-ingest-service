package datacollection;

import beans.OAuthBean;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import persistence.TokenMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataCheckThread implements Runnable {
    private ConcurrentLinkedQueue<TokenMap> input;
    private ConcurrentLinkedQueue<String> output;
    private String address;
    private OAuthBean oAuthBean;

    DataCheckThread(ConcurrentLinkedQueue<TokenMap> input, ConcurrentLinkedQueue<String> out, OAuthBean oAuthBean) {
        // Create Shallow copy to the global linked queue
        this.input = input;
        this.output = out;
        this.oAuthBean = oAuthBean;

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
        final OAuthRequest request = new OAuthRequest(Verb.GET,
                String.format(this.address, tokenMap.getUserID()));
        request.addHeader("x-li-format", "json");

        oAuthBean.getService().signRequest(tokenMap.getAccessToken(), request);

        try {
            final Response response = oAuthBean.getService().execute(request);
            output.add(response.getBody());
        } catch (Exception err) {
            // @TODO LOg error with request
        }
    }

}
