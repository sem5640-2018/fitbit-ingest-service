package beans;

import com.google.gson.Gson;
import datacollection.DataProcessThread;
import datacollection.FitBitJSON;
import datacollection.FitbitDataProcessor;
import datacollection.ProcessedData;
import org.junit.Assert;
import org.junit.Test;
import persistence.TokenMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JSONTest {

    private String sampleJSON = "{\"activities\":[{\"activityId\":51007,\"activityParentId\":90019,\"calories\":230,\"description\":\"7mph\",\"distance\":2.04,\"duration\":1097053,\"hasStartTime\":true,\"isFavorite\":true,\"logId\":1154701,\"name\":\"Treadmill, 0% Incline\",\"startTime\":\"00:25\",\"steps\":3783}],\"goals\":{\"caloriesOut\":2826,\"distance\":8.05,\"floors\":150,\"steps\":10000},\"summary\":{\"activityCalories\":230,\"caloriesBMR\":1913,\"caloriesOut\":2143,\"distances\":[{\"activity\":\"tracker\",\"distance\":1.32},{\"activity\":\"loggedActivities\",\"distance\":0},{\"activity\":\"total\",\"distance\":1.32},{\"activity\":\"veryActive\",\"distance\":0.51},{\"activity\":\"moderatelyActive\",\"distance\":0.51},{\"activity\":\"lightlyActive\",\"distance\":0.51},{\"activity\":\"sedentaryActive\",\"distance\":0.51},{\"activity\":\"Treadmill, 0% Incline\",\"distance\":3.28}],\"elevation\":48.77,\"fairlyActiveMinutes\":0,\"floors\":16,\"lightlyActiveMinutes\":0,\"marginalCalories\":200,\"sedentaryMinutes\":1166,\"steps\":0,\"veryActiveMinutes\":0}}";


    /**
     * Tests the sample JSON given by FitBit parses correctly
     */
    @Test
    public void TestJSONParse() {
        Gson gson = new Gson();
        FitBitJSON fitBitJSON = gson.fromJson(sampleJSON, FitBitJSON.class);

        Assert.assertNotNull(fitBitJSON);
    }


    @Test
    public void TestProcess() throws InterruptedException {
        TokenMap tm = new TokenMap();
        tm.setUserID("");
        tm.setAccessToken("");
        tm.setRefreshToken("");
        tm.setFitbitUid("FB");
        tm.setExpiresIn(3600);
        tm.setLastAccessed(new Date(0));
        ProcessedData data = new ProcessedData(tm);

        Gson gson = new Gson();
        FitBitJSON fitBitJSON = gson.fromJson(sampleJSON, FitBitJSON.class);
        fitBitJSON.setFromDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        for(int i = 0; i < 10; i++) {
            data.addProcessedActivity(fitBitJSON);
        }
        ConcurrentLinkedQueue<ProcessedData> queue = new ConcurrentLinkedQueue<ProcessedData>();

        for(int i = 0; i < 100000; i++) {
            queue.add(data);
        }

        Thread t = new Thread(new DataProcessThread(queue));
        t.start();

        t.join();
    }

    @Test
    public void TestConcurrency() {
        TokenMap tm = new TokenMap();
        tm.setUserID("");
        tm.setAccessToken("");
        tm.setRefreshToken("");
        tm.setFitbitUid("FB");
        tm.setExpiresIn(3600);
        tm.setLastAccessed(new Date(0));
        ProcessedData data = new ProcessedData(tm);

        Gson gson = new Gson();
        FitBitJSON fitBitJSON = gson.fromJson(sampleJSON, FitBitJSON.class);
        fitBitJSON.setFromDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        for(int i = 0; i < 10; i++) {
            data.addProcessedActivity(fitBitJSON);
        }
        FitbitDataProcessor processor = new FitbitDataProcessor();

        ConcurrentLinkedQueue<ProcessedData> queue = new ConcurrentLinkedQueue<ProcessedData>();
        for(int i = 0; i < 100000; i++) {
            queue.add(data);
        }

        processor.ProcessData(queue);
    }
}
