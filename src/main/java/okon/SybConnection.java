package okon;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String sql = "select top 10 count(xml_name) as Ilosc, xml_name as Komunikat from aesdb..customs_message " +
                "where doc_time >= dateadd(hh,-1,getdate()) group by xml_name order by Ilosc desc at isolation read uncommitted";

        try(Statement query = connection.createStatement(); ResultSet result = query.executeQuery(sql);) {
            Message message = new Message();

            while (result.next()) {
                String count = result.getString("Ilosc");
                String xmlName = result.getString("Komunikat");

                message.addRow(message.new Row(count, xmlName));
            }

            messages.add(message);
        } catch (SQLException e) {
            throw new AppException(e);
        }

        String sql2 = "select top 20 doc_time as doc_time, xml_name as xml_name from aesdb..customs_message where doc_time >= dateadd(hh, -1, getdate()) " +
                "order by doc_time desc at isolation read uncommitted";

        try(Statement query = connection.createStatement(); ResultSet result = query.executeQuery(sql2);) {
            Message message = new Message();

            while (result.next()) {
                String doc_time = result.getString("doc_time");
                String xmlName = result.getString("xml_name");

                message.addRow(message.new Row(doc_time, xmlName));
            }

            messages.add(message);
        } catch (SQLException e) {
            throw new AppException(e);
        }

        String sql3 = "select top 10 count(xml_name) as Ilosc, xml_name as Komunikat from aisdb..ics_mess " +
                "where doc_time >= dateadd(hh,-1,getdate()) group by xml_name order by Ilosc desc at isolation read uncommitted";

        try(Statement query = connection.createStatement(); ResultSet result = query.executeQuery(sql3);) {
            Message message = new Message();

            while (result.next()) {
                String count = result.getString("Ilosc");
                String xmlName = result.getString("Komunikat");

                message.addRow(message.new Row(count, xmlName));
            }

            messages.add(message);
        } catch (SQLException e) {
            throw new AppException(e);
        }

        String sql4 = "select top 20 doc_time as doc_time, xml_name as xml_name from aisdb..ics_mess where doc_time >= dateadd(hh, -1, getdate()) " +
                "order by doc_time desc at isolation read uncommitted";

        try(Statement query = connection.createStatement(); ResultSet result = query.executeQuery(sql4);) {
            Message message = new Message();

            while (result.next()) {
                String doc_time = result.getString("doc_time");
                String xmlName = result.getString("xml_name");

                message.addRow(message.new Row(doc_time, xmlName));
            }

            messages.add(message);
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return messages;
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