package okon;

import java.util.List;

public class Job {
    private String vendor;
    private String ip;
    private Integer port;
    private String user;
    private String password;
    private List<String> queries;
    private List<String> headers;

    public Job(String vendor, String ip, Integer port, String user, String password, List<String> queries, List<String> headers) {
        this.vendor = vendor;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
        this.queries = queries;
        this.headers = headers;
    }

    public String getVendor() {
        return vendor;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getQueries() {
        return queries;
    }

    public List<String> getHeaders() {
        return headers;
    }
}
