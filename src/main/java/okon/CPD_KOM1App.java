package okon;

import okon.config.ConfigurationParamsReader;
import okon.exception.AppException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CPD_KOM1App {
    static Queue<Job> jobQueue;
    static final List<Report> messageList = new ArrayList();

    static {
        jobQueue = ConfigurationParamsReader.readConfigurationParams(new File("./settings/config.xml"));
    }

    public static void main (String args[]) {
        new CPD_KOM1App().startThreadPool(jobQueue.size());
        new ReportPrinter().print(messageList);
    }

    private void startThreadPool(int threadSum) {
        Thread[] threads = new Thread[threadSum];
        for (int i = 0; i < threadSum; i++) {
            threads[i] = new ReportProducerThread();
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

    static String getJarFileName() {
        String path = CPD_KOM1App.class.getResource(CPD_KOM1App.class.getSimpleName() + ".class").getFile();
        path = path.substring(0, path.lastIndexOf('!'));
        path = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf('.'));
        return path;
    }
}