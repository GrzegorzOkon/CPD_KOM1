package okon;

import java.util.List;

import static okon.CPD_KOM1App.*;

public class ReportProducerThread extends Thread {
    @Override
    public void run() {
        while (!jobQueue.isEmpty()) {
            Job job = null;
            synchronized (jobQueue) {
                if (!jobQueue.isEmpty()) {
                    job = jobQueue.poll();
                }
            }
            if (job != null) {
                List<Report> messages = new QueryService(GatewayFactory.make(job)).execute(job);
                synchronized (messageList) {
                    for (Report message : messages)
                        messageList.add(message);
                }
            }
        }
    }
}