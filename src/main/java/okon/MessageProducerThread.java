package okon;

import okon.exception.AppException;
import okon.exception.ConnectionException;

import java.util.ArrayList;
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
        List<Message> messages = new ArrayList<>();
        try (SybConnection connection = connectionFactory.build(job)) {
            messages = connection.execute();
        } catch (ConnectionException e) {
            Message message = new ExceptionMessage();
            message.setHeader("" + "Blad polaczenia z baza danych " + job.getIp());
            messages.add(message);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return messages;
    }
}