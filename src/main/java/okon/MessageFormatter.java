package okon;

public class MessageFormatter {
    private final StringBuilder format = new StringBuilder();

    public MessageFormatter(int[] sizes) {
        setFormat(sizes);
    }

    private void setFormat(int[] sizes) {
        for (int i = 0; i < sizes.length; i++) {
            format.append("%-" + sizes[i] + "s ");
        }
    }

    public String format(String[] input) {
        return String.format(format.toString(), input);
    }
}
