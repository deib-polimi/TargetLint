package com.darkfoxdev.tesi.targetlint;

import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import static com.darkfoxdev.tesi.targetlint.TLBridge.log;

public abstract class ReportFileFormatter {

    abstract String getFilePath();
    abstract String getFileExtension();
    abstract String createReportString(String filename,Match m, TLIssue issue, String message);
    abstract String createFileHeader(String filename);
    abstract String createFileFooter(String filename);

    protected void createFile(String fileName) {
        try {

            Path file = Paths.get(getFilePath() + fileName + "." + getFileExtension());
            if (Files.exists(file)) {
                if (Files.lines(file).count() > 1) {
                    Files.move(file,Paths.get(getFilePath() + fileName + "_" + Files.getLastModifiedTime(file).toMillis() + "." + getFileExtension()));
                }
            }
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (Exception e) {
            TLBridge.log(e.getStackTrace());
        }
    }

    protected void createReport(Match m, TLIssue issue,String message,String filename) {
        String content = createReportString(filename,m,issue,message);
        writeToFile(content,filename);
    }

    protected void createHeader(String filename) {
        String content = createFileHeader(filename);
        writeToFile(content,filename);
    }

    protected void createFooter(String filename) {
        String content = createFileFooter(filename);
        writeToFile(content,filename);
    }

    private void writeToFile(String content,String filename) {
        Path file = Paths.get(getFilePath() + filename + "." + getFileExtension());
        try{
            Files.write(file, content.getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            log(e.getStackTrace());
        }
    }
}

