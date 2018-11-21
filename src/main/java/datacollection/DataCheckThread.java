package datacollection;

import beans.OAuthBean;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import persistence.TokenMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataCheckThread implements Runnable {
    private ConcurrentLinkedQueue<TokenMap> input;
    private ConcurrentLinkedQueue<ProcessedData> output;
    private OAuthBean oAuthBean;
    private Date now;

    DataCheckThread(ConcurrentLinkedQueue<TokenMap> input, ConcurrentLinkedQueue<ProcessedData> out, OAuthBean oAuthBean) {
        // Create Shallow copy to the global linked queue
        this.input = input;
        this.output = out;
        this.oAuthBean = oAuthBean;

        // Store the start of the requests
        now = new Date();
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
     */
    private void requestActivityData(TokenMap tokenMap) {
        ProcessedData toReturn = new ProcessedData(tokenMap);
        Date lastAccessed = tokenMap.getLastAccessed();
        LinkedList<String> addressesToPoll = new LinkedList<String>();

        addressesToPoll.add(dateToFormat(now));
        if (lastAccessed != null && doNeedPreviousDay(lastAccessed))
            addressesToPoll.add(dateToFormat(lastAccessed));
        else if (lastAccessed == null) {
            for (int i = 0; i < 7; i++) {
                addressesToPoll.add(dateToFormat(DaysDate(i)));
            }
        }

        // Used if we want to poll from previous days
        /*else if (lastAccessed == null) {
            for (int i = 0; i < 7; i++) {
                addressesToPoll.add(dateToFormat(DaysDate(i)));
            }
        }*/

        try {
            // Refresh token on start
            final OAuth2AccessToken accessToken = oAuthBean.getFitbitService().refreshAccessToken(tokenMap.getRefreshToken());

            for (String date : addressesToPoll) {
                final String activities = "https://api.fitbit.com/1/user/-/activities/date/" + date + ".json";
                final String steps = "https://api.fitbit.com/1/user/-/activities/steps/date/" + date + "/1d.json";

                // Request Activities
                OAuthRequest request = new OAuthRequest(Verb.GET,
                        String.format(activities, tokenMap.getUserID()));
                request.addHeader("x-li-format", "json");
                oAuthBean.getFitbitService().signRequest(accessToken, request);
                Response response = oAuthBean.getFitbitService().execute(request);
                toReturn.addActivityJSON(new ActivityJSON(response.getBody(), date));

                // Request Steps
                request = new OAuthRequest(Verb.GET,
                        String.format(steps, tokenMap.getUserID()));
                request.addHeader("x-li-format", "json");
                oAuthBean.getFitbitService().signRequest(accessToken, request);
                response = oAuthBean.getFitbitService().execute(request);
                toReturn.addStepsJSON(new ActivityJSON(response.getBody(), date));
            }
        }
       catch (Exception err) {
            // @TODO LOg error with request
            err.printStackTrace();
           // RETURN TERMINATE UPDATE
           return;
        }

        output.add(toReturn);
    }

    /**
     * @param lastChecked this parameter is the date to check
     * @return a boolean representing if now and the input are on the same calendar day
     */
    private boolean doNeedPreviousDay(Date lastChecked) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(lastChecked).equals(fmt.format(now));
    }

    /**
     * @param toFormat the date to be turned into a request url
     * @return The uri to send for the JSON request
     */
    private String dateToFormat(Date toFormat) {
        return new SimpleDateFormat("yyyy-MM-dd").format(toFormat);
    }

    private Date DaysDate(int daysBack) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysBack);
        return cal.getTime();
    }
}
