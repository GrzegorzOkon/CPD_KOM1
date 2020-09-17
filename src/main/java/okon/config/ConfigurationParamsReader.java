package okon.config;

import okon.Job;
import okon.exception.ConfigurationException;
import okon.security.HexDecryptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConfigurationParamsReader {
    public static Queue<Job> readConfigurationParams(File file) {
        Element root = parseXml(file);
        Queue<Job> jobs = new LinkedList<>();
        NodeList children = root.getElementsByTagName("server");
        if (children != null && children.getLength() > 0) {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) child;
                    List<String> headers = new ArrayList<>();
                    List<String> queries = new ArrayList<>();
                    String dbVendor = element.getElementsByTagName("db_vendor").item(0).getTextContent();
                    String dbIp = element.getElementsByTagName("db_ip").item(0).getTextContent();
                    Integer dbPort = Integer.valueOf(element.getElementsByTagName("db_port").item(0).getTextContent());
                    String dbUser = element.getElementsByTagName("db_user").item(0).getTextContent();
                    String dbPassword = element.getElementsByTagName("db_pswd").item(0).getTextContent();
                    for (int j = 0; j < element.getElementsByTagName("file_header").getLength(); j++) {
                        headers.add(element.getElementsByTagName("file_header").item(j).getTextContent());
                    }
                    for (int k = 0; k < element.getElementsByTagName("db_query").getLength(); k++) {
                        queries.add(element.getElementsByTagName("db_query").item(k).getTextContent());
                    }
                    Job job = new Job(dbVendor, dbIp, dbPort, HexDecryptor.convert(dbUser), HexDecryptor.convert(dbPassword), queries, headers);
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }

    private static Element parseXml(File file) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(file);
            return document.getDocumentElement();
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage());
        }
    }
}