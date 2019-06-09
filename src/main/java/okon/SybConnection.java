package okon;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SybConnection implements Closeable {
    private final Connection connection;

    public SybConnection(DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
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

        String sql1 = "select top 10 count(xml_name) as Ilosc, xml_name as Komunikat from aesdb..customs_message " +
                "where doc_time >= dateadd(hh,-1,getdate()) group by xml_name order by Ilosc desc at isolation read uncommitted";

        String sql2 = "select top 20 doc_time as Czas, xml_name as Komunikat, ref_no as Numer from aesdb..customs_message where doc_time >= dateadd(hh, -1, getdate()) " +
                "order by doc_time desc at isolation read uncommitted";

        String sql3 = "select top 10 count(xml_name) as Ilosc, xml_name as Komunikat from aisdb..ics_mess " +
                "where doc_time >= dateadd(hh,-1,getdate()) group by xml_name order by Ilosc desc at isolation read uncommitted";

        String sql4 = "select top 20 doc_time as Czas, xml_name as Komunikat, ref_no as Numer from aisdb..ics_mess where doc_time >= dateadd(hh, -1, getdate()) " +
                "order by doc_time desc at isolation read uncommitted";

        String headers[] = {headline1, headline2, headline3, headline4};
        String[] queries = {sql1, sql2, sql3, sql4};

        for (int i = 0; i < queries.length; i++) {
            try(Statement query = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); ResultSet rs = query.executeQuery(queries[i]);) {
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