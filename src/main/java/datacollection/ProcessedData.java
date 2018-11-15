package datacollection;

import persistence.TokenMap;

import java.util.LinkedList;

public class ProcessedData {
    private TokenMap InputToken;

    private LinkedList<String> ActivityJSON;
    private LinkedList<String> StepsJSON;

    private LinkedList<FitBitJSON> ProcessedActivities;
    // @TODO add Steps Object

    public ProcessedData(TokenMap inputToken) {
        this.InputToken = inputToken;

        ActivityJSON = new LinkedList<String>();
        StepsJSON = new LinkedList<String>();

        ProcessedActivities = new LinkedList<FitBitJSON>();
    }

    public ProcessedData(TokenMap inputToken, String ActivityJSON) {
        this(inputToken);

        this.ActivityJSON.add(ActivityJSON);
    }

    // Activity JSON CRUD
    public LinkedList<String> getActivityJSON() {
        return ActivityJSON;
    }

    public void addActivityJSON(String input) {
        ActivityJSON.add(input);
    }

    // Steps JSON CRUD
    public LinkedList<String> getStepsJSON() {
        return StepsJSON;
    }

    public void addStepsJSON(String input) {
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
