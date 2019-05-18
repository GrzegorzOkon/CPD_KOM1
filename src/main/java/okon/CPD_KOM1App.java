package okon;

import org.w3c.dom.Element;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CPD_KOM1App {
    static final ConnectionFactory connectionFactory = new ConnectionFactory();
    static Queue<DataSource> dataSourceQueue;
    static final List<Message> messageList = new ArrayList();

    private final DataSourceBuilder dataSourceBuilder = new DataSourceBuilder();

    public static void main (String args[]) {
        CPD_KOM1App cpd_kom1_app = new CPD_KOM1App();

        dataSourceQueue = cpd_kom1_app.loadConfiguration("./settings/config.xml");

        cpd_kom1_app.startThreadPool(dataSourceQueue.size());

        cpd_kom1_app.save("CPD_KOM1.txt", messageList);
    }

    private Queue<DataSource> loadConfiguration(String pathname) {
        ConfigurationParser parser = new ConfigurationParser();
        Element root = parser.parseXml(new File(pathname));

        return dataSourceBuilder.build(root);
    }

    private void startThreadPool(int threadSum) {
        Thread[] threads = new Thread[threadSum];

        for (int i = 0; i < threadSum; i++) {
            threads[i] = new MessageProducerThread();
        }

        for (int i = 0; i < threadSum; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threadSum; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                throw new AppException(e);
            }
        }
    }

    public void save(String fileName, List<Message> content) {
        try (FileOutputStream out = new FileOutputStream(new java.io.File(fileName))) {
            out.write("* Ostatnie wysłane komunikaty *".getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("* System AES".getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("Ilosc    Komunikat ".getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("-----   -----------".getBytes());
            out.write(System.getProperty("line.separator").getBytes());

            Message message = content.get(0);

            for (Message.Row row : message.getRows()) {
                String formattedRow = new MessageFormatter(row).format();

                out.write(formattedRow.getBytes());
                out.write(System.getProperty("line.separator").getBytes());
            }

            out.write(System.getProperty("line.separator").getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("doc_time                     xml_name".getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("--------                     --------".getBytes());
            out.write(System.getProperty("line.separator").getBytes());

            message = content.get(1);

            for (Message.Row row : message.getRows()) {
                String formattedRow = new MessageFormatter(row).format();

                out.write(formattedRow.getBytes());
                out.write(System.getProperty("line.separator").getBytes());
            }

            out.write(System.getProperty("line.separator").getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("* System AIS".getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("Ilosc    Komunikat ".getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("-----   -----------".getBytes());
            out.write(System.getProperty("line.separator").getBytes());

            message = content.get(2);

            for (Message.Row row : message.getRows()) {
                String formattedRow = new MessageFormatter(row).format();

                out.write(formattedRow.getBytes());
                out.write(System.getProperty("line.separator").getBytes());
            }

            out.write(System.getProperty("line.separator").getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("doc_time                     xml_name".getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.write("--------                     --------".getBytes());
            out.write(System.getProperty("line.separator").getBytes());

            message = content.get(3);

            for (Message.Row row : message.getRows()) {
                String formattedRow = new MessageFormatter(row).format();

                out.write(formattedRow.getBytes());
                out.write(System.getProperty("line.separator").getBytes());
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
