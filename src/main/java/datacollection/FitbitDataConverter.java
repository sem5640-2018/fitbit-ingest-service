package datacollection;

import com.google.gson.Gson;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Fitbit Data Converter is intended to be the object that takes in JSON from a Fitbit API response and converts it into
 * the format the Heath Data Repository is expecting.
 */
public class FitbitDataConverter {

    private static Gson gson = new Gson();

    public FitbitDataConverter() {
        //TODO implement
    }

    /**
     * @param input a linked list of the data with the Raw JSON strings
     * @return the list with all the processed data added
     */
    public ConcurrentLinkedQueue<ProcessedData> convertActivityData(ConcurrentLinkedQueue<ProcessedData> input) {
        // TODO check if threading will be faster

        for (ProcessedData data: input) {
            for (String json: data.getActivityJSON()) {
                data.addProcessedActivity(gson.fromJson(json, FitBitJSON.class));
            }
        }

        return input;
    }

    public String convertProfileData() {
        //TODO implement
        return "some JSON to be sent to Heath Data Repo";
    }
}
