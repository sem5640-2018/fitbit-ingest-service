package datacollection;

import datacollection.mappings.FitBitJSON;
import datacollection.mappings.Steps;
import persistence.TokenMap;

import java.util.LinkedList;

class ActivityJSON {
    String JSON;
    String date;

    ActivityJSON(String JSON, String date) {
        this.JSON = JSON;
        this.date = date;
    }
}

public class ProcessedData {
    private TokenMap InputToken;

    private LinkedList<ActivityJSON> ActivityJSON;
    private LinkedList<ActivityJSON> StepsJSON;

    private LinkedList<FitBitJSON> ProcessedActivities;
    private LinkedList<Steps> ProcessedSteps;

    public ProcessedData(TokenMap inputToken) {
        this.InputToken = inputToken;

        ActivityJSON = new LinkedList<>();
        StepsJSON = new LinkedList<>();
        ProcessedSteps = new LinkedList<>();

        ProcessedActivities = new LinkedList<>();
    }

    // Activity JSON
    LinkedList<ActivityJSON> getActivityJSON() {
        return ActivityJSON;
    }
    // Activity JSON
    LinkedList<ActivityJSON> getStepsJSON() {
        return StepsJSON;
    }

    void addActivityJSON(ActivityJSON input) {
        ActivityJSON.add(input);
    }

    void addStepsJSON(ActivityJSON input) {
        StepsJSON.add(input);
    }

    // Processed Data
    LinkedList<FitBitJSON> getProcessedActivities() {
        return ProcessedActivities;
    }

    public void addProcessedActivity(FitBitJSON input) {
        ProcessedActivities.add(input);
    }

    // Processed Steps
    LinkedList<Steps> getProcessedSteps() {
        return ProcessedSteps;
    }

    public void addProcessedSteps(Steps input) {
        ProcessedSteps.add(input);
    }

    TokenMap getInputToken() {
        return InputToken;
    }
}
