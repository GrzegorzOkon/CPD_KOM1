package okon;

import javax.sql.DataSource;
import java.util.List;

public class Job {
    private DataSource dataSource;
    private List<String> queries;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getQueries() {
        return queries;
    }

    public void setQueries(List queries) {
        this.queries = queries;
    }
}
