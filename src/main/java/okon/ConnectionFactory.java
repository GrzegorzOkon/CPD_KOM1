package okon;

public class ConnectionFactory {
    public SybConnection build(Job job) {
        return new SybConnection(job);
    }
}
