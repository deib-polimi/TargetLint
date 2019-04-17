package com.darkfoxdev.tesi.targetlint;



public class CSVReportFileFormatter extends ReportFileFormatter {


    @Override
    String getFilePath() {
        return System.getProperty("user.home")+ "/lintLogs/";
    }

    @Override
    String getFileExtension() {
        return "csv";
    }


    @Override
    String createReportString(String filename,Match m, TLIssue issue, String message) {
        int line = m.getElement().getLocation().getStart().getLine();
        int column = m.getElement().getLocation().getStart().getColumn();
        String location = "File: " + m.getElement().getLocation().getFile().getName() + " Line: " + line + " Column: " + column;
        return "\"" + issue.getBriefDescription() + "\"," + "\"" + message + "\"," + location + ",\"" + m.getElement().getSource() + ",\"\n";
    }

    @Override
    String createFileHeader(String filename) {
      return "Issue,Message,Location,Instruction\n";

    }

    @Override
    String createFileFooter(String filename) {
        return "FINE\n";

    }
}
