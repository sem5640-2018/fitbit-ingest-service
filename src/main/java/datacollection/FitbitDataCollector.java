package datacollection;

import persistence.TokenMap;

/**
 * Class that handles making API calls to Fitbit. Will probably use ScribeJava library to achive this. Currently
 * Unimplemented.
 * @Author James H Britton
 * @Author "jhb15@aber.ac.uk"
 * @Version 0.1
 */
public class FitbitDataCollector {

    private static String baseUserURL = "https://api.fitbit.com/1/user/-/";

    public FitbitDataCollector() {
        //TODO
    }

    /**
     * Function for sending a request for Activity Data to the Fitbit API.
     * @param tokenMap TokenMap for user we want data for.
     * @return JSON Array with activity data
     */
    public String requestActivityData(TokenMap tokenMap) {
        //TODO will send of API request for activity data
        return "some JSON to be parsed";
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
