package okon;

public class ReportFormatter {
    private final StringBuilder format = new StringBuilder();

    public ReportFormatter(int[] sizes) {
        setFormat(sizes);
    }

    private void setFormat(int[] sizes) {
        for (int i = 0; i < sizes.length; i++) {
            format.append("%-" + Integer.sum(sizes[i], 2) + "s ");
        }
    }

    public String format(String[] input) {
        return String.format(format.toString(), input);
    }
}
