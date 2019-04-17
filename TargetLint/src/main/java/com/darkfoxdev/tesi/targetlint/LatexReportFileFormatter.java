package com.darkfoxdev.tesi.targetlint;

import com.darkfoxdev.tesi.targetlint.tlast.TLClass;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LatexReportFileFormatter extends ReportFileFormatter {

    private List<ReportIssue> reports = new ArrayList<>();

    private class ReportIssue {
        public Match m;
        public TLIssue issue;
        public String message;

        public ReportIssue(Match m, TLIssue issue, String message) {
            this.m = m;
            this.issue = issue;
            this.message = message;
        }
    }

    @Override
    String getFilePath() {
        return System.getProperty("user.home") + "/lintLogs/";
    }

    @Override
    String getFileExtension() {
        return "tex";
    }

    @Override
    String createReportString(String filename, Match m, TLIssue issue, String message) {
        reports.add(new ReportIssue(m, issue, message));
        return "";

    }

    @Override
    String createFileHeader(String filename) {
        return "";
    }

    private TLElement getInstructionInMethod(TLElement partialInstruction) {

        if (partialInstruction.getParent() instanceof TLMethod) {
            return partialInstruction;
        } else {
            return getInstructionInMethod(partialInstruction.getParent());
        }


    }

    @Override
    String createFileFooter(String filename) {
        StringBuilder resultBuilder = new StringBuilder();
        List<String> issues = reports.stream().map(r -> r.issue.getName()).distinct().collect(Collectors.toList());
        resultBuilder.append("\\lstset{moredelim=[is][\\color{red}]{@!}{!@}}");
        for (String i : issues) {
            List<ReportIssue> issueReports = reports.stream().filter(r -> r.issue.getName().equals(i)).collect(Collectors.toList());
            resultBuilder.append("\\section{");
            resultBuilder.append(issueReports.get(0).issue.getBriefDescription());
            resultBuilder.append("}\n \\subsection{Issue Description}");
            resultBuilder.append(issueReports.get(0).issue.getExplanation());
            resultBuilder.append("\n \\subsection{Report}");

            List<String> files = issueReports.stream().map(ir -> ir.m.getElement().getLocation().getFile().getName()).distinct().collect(Collectors.toList());
            for (String file : files) {
                resultBuilder.append("\n\\paragraph{");
                resultBuilder.append(file);
                resultBuilder.append("}\n\\begin{itemize} ");
                List<ReportIssue> reportIssuesOnFile = issueReports.stream().filter(ri -> ri.m.getElement().getLocation().getFile().getName().equals(file)).collect(Collectors.toList());
                reportIssuesOnFile.forEach(ri -> {
                    resultBuilder.append("\n\\item ");
                    resultBuilder.append(ri.message);
                    resultBuilder.append("\n\nLocation: ");
                    resultBuilder.append(ri.m.getElement().getLocationString(false));
                    resultBuilder.append("\n");

                    resultBuilder.append(createListing(ri.m.getElement()));

                });
                resultBuilder.append("\n\\end{itemize} ");
            }

        }
        return resultBuilder.toString();
    }

    private String createListing(TLElement element) {
        StringBuilder resultBuilder = new StringBuilder();
        File file = element.getLocation().getFile();
        int line = element.getLocation().getStart().getLine();
        int linesToShow = 3;
        int startLine = line - linesToShow / 2 > 0 ? line - linesToShow / 2 : 0;
        try (Stream<String> lines = Files.lines(file.toPath())) {
            resultBuilder.append("\n \\begin{lstlisting}[firstnumber= " + startLine + "]\n");
            List<String> fileLines = lines.skip(startLine).collect(Collectors.toList());
            List<String> stringToAdd = new ArrayList<>();

            int stopIndex = fileLines.size() >= linesToShow ? linesToShow : fileLines.size();
            fileLines.subList(0, stopIndex).forEach(l -> {
                String lineToAppend = l;
                if (fileLines.indexOf(l) == line - startLine) {
                    lineToAppend = lineToAppend.replace(element.getSource(), "@!" + element.getSource() + "!@");
                }
                stringToAdd.add(lineToAppend+"\n");

            });
            int min = Integer.MAX_VALUE;
            Pattern p = Pattern.compile("^[\\s\\t]*");
            for (String l : stringToAdd.stream().filter(s -> !s.equals("\n")).collect(Collectors.toList())) {
                Matcher m = p.matcher(l);
                  if(m.find()) {
                      min = Integer.min(min,m.end());
                  }
            }
            final int i = min;
            stringToAdd.forEach(l-> {
                if (l.equals("\n")) {
                    resultBuilder.append(l);
                } else {
                    resultBuilder.append(l.length() > i ? l.substring(i) : l);
                }
            });

        } catch (Exception e) {
            TLBridge.log(e.getStackTrace());
        } finally {
            resultBuilder.append("\\end{lstlisting}\n");
        }
        return resultBuilder.toString();
    }

}