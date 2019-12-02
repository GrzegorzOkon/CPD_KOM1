package okon;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String header = null;
    private int[] sizes = null;
    private String[] labels = null;
    private List<String[]> rows = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int[] getSizes() { return sizes; }

    public void setSizes(int[] sizes) { this.sizes = sizes; }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public List<String[]> getRows() {
        return rows;
    }

    public void setRows(List<String[]> rows) {
        this.rows = rows;
    }
}