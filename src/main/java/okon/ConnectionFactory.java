package okon;

import javax.sql.DataSource;

public class ConnectionFactory {
    public SybConnection build(Job job) {
        return new SybConnection(job);
    }
}
