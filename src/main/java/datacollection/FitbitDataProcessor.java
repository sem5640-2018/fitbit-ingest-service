package datacollection;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FitbitDataProcessor {
    private static final int threadCount = 4;

    public void ProcessData(ConcurrentLinkedQueue<ProcessedData> input) {
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i--) {
            threads[i] = new Thread(new DataProcessThread(input));
            threads[i].start();
        }

        for (int j = 0; j < threadCount; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                // @TODO log error
                e.printStackTrace();
            }
        }
    }
}
