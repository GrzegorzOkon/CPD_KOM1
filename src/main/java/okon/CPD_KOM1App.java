package okon;

import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class CPD_KOM1App {
    static boolean CONSOLE = false;
    static boolean FILE = false;
    static final ConnectionFactory connectionFactory = new ConnectionFactory();
    static Queue<Job> jobQueue;
    static final List<Message> messageList = new ArrayList();

    private final JobBuilder taskBuilder = new JobBuilder();

    public static void main (String args[]) {
        setResultOutput(args);
        CPD_KOM1App cpd_kom1_app = new CPD_KOM1App();
        jobQueue = cpd_kom1_app.loadConfiguration("./settings/config.xml");
        cpd_kom1_app.startThreadPool(jobQueue.size());
        cpd_kom1_app.print(messageList);
    }

    static void setResultOutput(String[] args) {
        if (isConsoleOutput(args)) {
            CONSOLE = true;
        } else {
            FILE = true;
        }
    }

    static boolean isConsoleOutput(String[] args) {
        if (args.length > 0) {
            return true;
        }
        return false;
    }

    static String getJarFileName() {
        String path = CPD_KOM1App.class.getResource(CPD_KOM1App.class.getSimpleName() + ".class").getFile();
        path = path.substring(0, path.lastIndexOf('!'));
        path = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf('.'));

        return path;
    }

    private Queue<Job> loadConfiguration(String pathname) {
        ConfigurationParser parser = new ConfigurationParser();
        Element root = parser.parseXml(new File(pathname));

        return taskBuilder.build(root);
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

    private void print(List<Message> content) {
        if(CONSOLE) {
            printToConsole(content);
        }
        if (FILE){
            printToFile(content);
        }
    }

    private void printToConsole(List<Message> content) {
        for(Message message : content) {
            MessageFormatter formatter = new MessageFormatter(message.getSizes());
            System.out.println(message.getHeader());
            System.out.println();
            System.out.println(formatter.format(message.getLabels()));
            Arrays.fill(message.getLabels(), "---");
            System.out.println(formatter.format(message.getLabels()));
            for (String[] row : message.getRows()) {
                System.out.println(formatter.format(row));
            }
            System.out.println();
            System.out.println();
        }
    }

    private void printToFile(List<Message> content) {
        try (FileOutputStream out = new FileOutputStream(new java.io.File(CPD_KOM1App.getJarFileName() + ".txt"))) {
            for(Message message : content) {
                MessageFormatter formatter = new MessageFormatter(message.getSizes());
                out.write(message.getHeader().getBytes());
                out.write(System.getProperty("line.separator").getBytes());
                out.write(System.getProperty("line.separator").getBytes());
                out.write(formatter.format(message.getLabels()).getBytes());
                out.write(System.getProperty("line.separator").getBytes());
                Arrays.fill(message.getLabels(), "---");
                out.write(formatter.format(message.getLabels()).getBytes());
                out.write(System.getProperty("line.separator").getBytes());
                for (String[] row : message.getRows()) {
                    out.write(formatter.format(row).getBytes());
                    out.write(System.getProperty("line.separator").getBytes());
                }
                out.write(System.getProperty("line.separator").getBytes());
                out.write(System.getProperty("line.separator").getBytes());
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}