package okon;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SybConnection implements Closeable {
    private final Connection connection;
    private final List<String> queries;

    public SybConnection(Job job) {
        try {
            connection = job.getDataSource().getConnection();
            queries = job.getQueries();
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public List<Message> execute() {
        List<Message> messages = new ArrayList<>();

        String headline1 = "* System AES. Pogrupowane komunikaty z ostatniej godziny";
        String headline2 = "* System AES. Ostatnio wysłane dokumenty";
        String headline3 = "* System AIS. Pogrupowane komunikaty z ostatniej godziny";
        String headline4 = "* System AIS. Ostatnio wysłane dokumenty";

        String headers[] = {headline1, headline2, headline3, headline4};

        for (int i = 0; i < queries.size(); i++) {
            try(Statement query = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); ResultSet rs = query.executeQuery(queries.get(i));) {
                Message message = new Message();

                message.setHeader(headers[i]);
                message.setSizes(getColumnDisplaySizes(rs));
                message.setLabels(getColumnLabels(rs.getMetaData()));
                message.setRows(getDataRows(rs));

                messages.add(message);
            } catch (SQLException e) {
                throw new AppException(e);
            }
        }

        return messages;
    }

    private int[] getColumnDisplaySizes(ResultSet rs) {
        int[] sizes = null;

        try {
            sizes = new int[rs.getMetaData().getColumnCount()];

            for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                sizes[i - 1] = rs.getMetaData().getColumnName(i).length();
            }

            rs.beforeFirst();
            while (rs.next()) {
                for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString(i) != null) {
                        if (rs.getString(i).length() > sizes[i - 1]) {
                            sizes[i - 1] = rs.getString(i).length();
                        }
                    } else {
                        if (sizes[i - 1] < 4) {
                            sizes[i - 1] = 4;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return sizes;
    }

    private String[] getColumnLabels(ResultSetMetaData rsmd) {
        String[] labels = null;

        try {
            labels = new String[rsmd.getColumnCount()];

            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                labels[i - 1] = rsmd.getColumnName(i);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return labels;
    }

    private List<String[]> getDataRows(ResultSet rs) {
        List<String[]> rows = new ArrayList<>();

        try {
            int columnCount = rs.getMetaData().getColumnCount();

            rs.beforeFirst();
            while (rs.next()) {
                String[] row = new String[columnCount];

                for(int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }

                rows.add(row);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return rows;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }
}