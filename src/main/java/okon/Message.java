package okon;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private List<Row> rows = new ArrayList<>();

    public void addRow(Row row) {
        rows.add(row);
    }

    public List<Row> getRows() {
        return rows;
    }

    class Row {
        private String count;
        private String xmlName;

        public Row(String count, String xmlName) {
            this.count = count;
            this.xmlName = xmlName;
        }

        public String getXmlName() {
            return xmlName;
        }

        public void setXmlName(String xmlName) {
            this.xmlName = xmlName;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
