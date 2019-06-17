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
                List<Message> messages = execute(job);

                synchronized (messageList) {
                    for (Message message : messages)
                        messageList.add(message);
                }
            }
        }
    }

    public List<Message> execute(Job job) {
        List<Message> message = null;

        try (SybConnection connection = connectionFactory.build(job)) {
            message = connection.execute();
        } catch (Exception e) {
            throw new AppException(e);
        }

        return message;
    }
}
