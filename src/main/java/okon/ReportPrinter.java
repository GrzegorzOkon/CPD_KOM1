package okon;

import okon.exception.AppException;

import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

public class ReportPrinter {
    public void print(List<Report> reports) {
        printToConsole(reports);
        printToFile(reports);
    }

    private void printToConsole(List<Report> reports) {
        for (int i = 0; i < reports.size(); i++) {
            printInterlinerSpacingIfNeeded(i);
            if (reports.get(i) instanceof ReportContent) {
                ReportFormatter formatter = new ReportFormatter(reports.get(i).getSizes());
                System.out.println(reports.get(i).getHeader());
                System.out.println();
                System.out.println(formatter.format(reports.get(i).getLabels()));
                System.out.println(formatter.format(Collections.nCopies(reports.get(i).getLabels().length, "---").toArray(new String[0])));
                for (String[] row : reports.get(i).getRows()) {
                    System.out.println(formatter.format(row));
                }
            } else if (reports.get(i) instanceof ExceptionMessage) {
                System.out.println(reports.get(i).getHeader());
            }
        }
    }

    private void printToFile(List<Report> reports) {
        try (Writer out = new FileWriter(new java.io.File(CPD_KOM1App.getJarFileName() + ".txt"))) {
            for (int i = 0; i < reports.size(); i++) {
                writeInterlinerSpacingIfNeeded(out, i);
                if (reports.get(i) instanceof ReportContent) {
                    ReportFormatter formatter = new ReportFormatter(reports.get(i).getSizes());
                    out.write(reports.get(i).getHeader());
                    out.write(System.getProperty("line.separator"));
                    out.write(System.getProperty("line.separator"));
                    out.write(formatter.format(reports.get(i).getLabels()));
                    out.write(System.getProperty("line.separator"));
                    out.write(formatter.format(Collections.nCopies(reports.get(i).getLabels().length, "---").toArray(new String[0])));
                    out.write(System.getProperty("line.separator"));
                    for (String[] row : reports.get(i).getRows()) {
                        out.write(formatter.format(row));
                        out.write(System.getProperty("line.separator"));
                    }
                } else if (reports.get(i) instanceof ExceptionMessage) {
                    out.write(reports.get(i).getHeader());
                }
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private void printInterlinerSpacingIfNeeded(int reportOccuring) {
        if (isInterlinearSpacingNeeded(reportOccuring)) {
            System.out.println();
            System.out.println();
        }
    }

    private void writeInterlinerSpacingIfNeeded(Writer out, int reportOccuring) {
        if (isInterlinearSpacingNeeded(reportOccuring)) {
            try {
                out.write(System.getProperty("line.separator"));
                out.write(System.getProperty("line.separator"));
            } catch (Exception e) {
                throw new AppException(e);
            }
        }
    }

    private boolean isInterlinearSpacingNeeded(int ReportOccuring) {
        if (ReportOccuring != 0)
            return true;
        return false;
    }
}