package okon;

import java.util.List;

public interface Gateway extends AutoCloseable {
    public List<Report> execute(List<String> queries, List<String> headers);

    @Override
    void close() throws Exception;
}
