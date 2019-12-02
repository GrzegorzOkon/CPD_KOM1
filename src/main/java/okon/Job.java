package okon;

import java.util.List;

public class Job {
    private String ip;
    private Integer port;
    private String user;
    private String password;
    private List<String> queries;
    private List<String> headers;

    public String getIp() { return ip; }

    public void setIp(String ip) { this.ip = ip; }

    public Integer getPort() { return port; }

    public void setPort(Integer port) { this.port = port; }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<String> getQueries() {
        return queries;
    }

    public void setQueries(List<String> queries) { this.queries = queries; }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
}
