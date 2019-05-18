package okon;

import javax.sql.DataSource;

import java.util.List;

import static okon.CPD_KOM1App.connectionFactory;
import static okon.CPD_KOM1App.dataSourceQueue;
import static okon.CPD_KOM1App.messageList;

public class MessageProducerThread extends Thread {

    @Override
    public void run() {
        while (!dataSourceQueue.isEmpty()) {
            DataSource job = null;

            synchronized (dataSourceQueue) {
                if (!dataSourceQueue.isEmpty()) {
                    job = dataSourceQueue.poll();
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

    public List<Message> execute(DataSource dataSource) {
        List<Message> message = null;

        try (SybConnection connection = connectionFactory.build(dataSource)) {
            message = connection.execute();
        } catch (Exception e) {
            throw new AppException(e);
        }

        return message;
    }
}
