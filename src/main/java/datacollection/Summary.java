package datacollection;

import java.util.List;

public class Summary {
    private long activityCalories;
    private long caloriesBMR;
    private long caloriesOut;

    private List<Distances> distances;

    private float elevation;
    private long fairlyActiveMinutes;
    private long floors;
    private long lightlyActiveMinutes;
    private long marginalCalories;
    private long sedentaryMinutes;
    private long steps;
    private long veryActiveMinutes;

    public Summary() {

    }

    public long getVeryActiveMinutes() {
        return veryActiveMinutes;
    }

    public long getSteps() {
        return steps;
    }

    public long getSedentaryMinutes() {
        return sedentaryMinutes;
    }

    public long getMarginalCalories() {
        return marginalCalories;
    }

    public long getLightlyActiveMinutes() {
        return lightlyActiveMinutes;
    }

    public long getFloors() {
        return floors;
    }

    public long getFairlyActiveMinutes() {
        return fairlyActiveMinutes;
    }

    public float getElevation() {
        return elevation;
    }

    public List<Distances> getDistances() {
        return distances;
    }

    public long getCaloriesOut() {
        return caloriesOut;
    }

    public long getCaloriesBMR() {
        return caloriesBMR;
    }

    public long getActivityCalories() {
        return activityCalories;
    }
}
