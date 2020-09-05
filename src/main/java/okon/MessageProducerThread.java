package okon;

import java.util.List;

import static okon.CPD_KOM1App.*;

public class MessageProducerThread extends Thread {
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
                List<Message> messages = new QueryService(GatewayFactory.make(job)).execute(job);
                synchronized (messageList) {
                    for (Message message : messages)
                        messageList.add(message);
                }
            }
        }
    }
}