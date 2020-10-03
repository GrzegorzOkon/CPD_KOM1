package okon;

import okon.exception.AppException;
import okon.exception.ConnectionException;

import java.util.ArrayList;
import java.util.List;

public class QueryService {
    private final Gateway db;

    public QueryService(Gateway db) {
        this.db = db;
    }

    public List<Report> execute(Job job) {
        List<Report> messages = new ArrayList<>();
        try {
            messages = db.execute(job.getQueries(), job.getHeaders());
        } catch (ConnectionException e) {
            Report message = new ExceptionMessage();
            message.setHeader("" + "Blad polaczenia z baza danych " + job.getIp());
            messages.add(message);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
        }
        return messages;
    }
}
