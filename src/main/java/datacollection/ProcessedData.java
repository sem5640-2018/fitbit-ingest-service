package datacollection;

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
    // @TODO add Steps Object

    public ProcessedData(TokenMap inputToken) {
        this.InputToken = inputToken;

        ActivityJSON = new LinkedList<>();
        StepsJSON = new LinkedList<>();

        ProcessedActivities = new LinkedList<>();
    }

    public ProcessedData(TokenMap inputToken, ActivityJSON ActivityJSON) {
        this(inputToken);

        this.ActivityJSON.add(ActivityJSON);
    }

    // Activity JSON CRUD
    public LinkedList<ActivityJSON> getActivityJSON() {
        return ActivityJSON;
    }

    public void addActivityJSON(ActivityJSON input) {
        ActivityJSON.add(input);
    }

    // Steps JSON CRUD
    public LinkedList<ActivityJSON> getStepsJSON() {
        return StepsJSON;
    }

    public void addStepsJSON(ActivityJSON input) {
        StepsJSON.add(input);
    }

    // Processed Data CRUD
    public LinkedList<FitBitJSON> getProcessedActivities() {
        return ProcessedActivities;
    }

    public void addProcessedActivity(FitBitJSON input) {
        ProcessedActivities.add(input);
    }

    public TokenMap getInputToken() {
        return InputToken;
    }
}
