package datacollection;

import com.google.gson.Gson;
import datacollection.mappings.FitBitJSON;
import datacollection.mappings.FitbitSteps;
import datacollection.mappings.Steps;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Fitbit Data Converter is intended to be the object that takes in JSON from a Fitbit API response and converts it into
 * the format the Heath Data Repository is expecting.
 */
public class FitbitDataConverter {

    private static Gson gson = new Gson();

    public FitbitDataConverter() {
    }

    /**
     * @param input a linked list of the data with the Raw JSON strings
     * @return the list with all the processed data added
     */
    public ConcurrentLinkedQueue<ProcessedData> convertActivityData(ConcurrentLinkedQueue<ProcessedData> input) {
        for (ProcessedData data : input) {
            for (ActivityJSON json : data.getActivityJSON()) {
                System.out.println(json.JSON);
                FitBitJSON activityClass = gson.fromJson(json.JSON, FitBitJSON.class);
                activityClass.setFromDate(json.date);
                activityClass.setUserID(data.getInputToken().getUserID());
                data.addProcessedActivity(activityClass);
            }

            for (ActivityJSON json : data.getStepsJSON()) {
                System.out.println(json.JSON);
                FitbitSteps overView = gson.fromJson(json.JSON, FitbitSteps.class);
                for (Steps activityClass: overView.getActivities()) {
                    activityClass.setUserID(data.getInputToken().getUserID());
                    data.addProcessedSteps(activityClass);
                }
            }
        }

        return input;
    }
}
