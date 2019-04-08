package com.darkfoxdev.tesi.targetlint;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.uast.UastDetector;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Tl bridge.
 */
public class TLBridge {

    private static boolean debug = true;
    private static boolean fileReport = false;

    private LintDetectorInterface lintDetector;
    /**
     * The constant detectorClasses.
     */
    public static List<Class<? extends TLDetector>> detectorClasses = new ArrayList<>();
    private List<TLDetector> detectors = new ArrayList<>();
    private String projectName = "default";


    /**
     * Instantiates a new Tl bridge.
     *
     * @param lintDetectorInterface the lint detector interface
     */
    public TLBridge(LintDetectorInterface lintDetectorInterface) {

        try {
            String directory = System.getProperty("user.home") + "/.android/targetLint/";
            ExtensionLoader<TLDetector> loader = new ExtensionLoader<>();
            detectorClasses = loader.loadJars(directory, TLDetector.class);
        } catch (Exception e) {
           log(e.getStackTrace());
        }
        this.lintDetector = lintDetectorInterface;
        detectorClasses.forEach(dc -> {
            try {
                 detectors.add(dc.getConstructor(TLBridge.class).newInstance(this));
            } catch (Exception e) {
                log(e.getStackTrace());
            }
        });

    }

    /**
     * Log.
     *
     * @param stackTraceElements the stack trace elements
     */
    public static void log (StackTraceElement[] stackTraceElements) {
        String logMessage = "";
                for (StackTraceElement element : stackTraceElements) {
                    logMessage = logMessage + element.toString() + "\n";
                }
        log(logMessage);
    }

    /**
     * Log.
     *
     * @param message the message
     */
    public static void log (String message) {
        if (debug) {
           UastDetector.log(message); //TODO Su file!
        }
    }

    private void createReportFile (String project) {
        try {
            String header = "Issue,Message,Location,Instruction\n";
            Path file = Paths.get("/Users/Pietro/lintLog/" + project + ".csv");
            if (Files.exists(file)) {
                if (Files.lines(file).count() > 1 ) {
                    Files.delete(file);
                }
            }
            if (!Files.exists(file)) {
                Files.createFile(file);
                Files.write(file, header.getBytes(), StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            log(e.getMessage());
            log(e.getStackTrace());
        }
    }

    private void reportToFile (Match m, TLIssue issue, String message) {
        try {
            Path file = Paths.get("/Users/Pietro/lintLog/" + getProjectName() + ".csv");
            int line = m.getElement().getLocation().getStart().getLine();
            int column = m.getElement().getLocation().getStart().getColumn();
            String location = "File: " + m.getElement().getLocation().getFile().getName() + " Line: " + line + " Column: " + column;
            String log = "\"" + issue.getBriefDescription()  + "\"," + "\""+ message + "\"," + location + ",\"" + m.getElement().getSource() + ",\"\n";
            Files.write(file, log.getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            log(e.getStackTrace());
        }
    }

    /**
     * Gets detectors.
     *
     * @return the detectors
     */
    public List<TLDetector> getDetectors() {
        return detectors;
    }

    /**
     * PossibleMatchFound.
     *
     * @param element the element
     */
    public void possibleMatchFound (TLElement element) {//TODO change to possibleMatchFound
        detectors.forEach(d -> d.isPossibleMatch(element));


    }

    /**
     * After check file.
     */
    public void afterCheckFile() {
        detectors.forEach(d -> d.afterCheckFile());
    }

    /**
     * After check project.
     */
    public void afterCheckProject() {
        detectors.forEach(d -> d.afterCheckProject());
        this.projectName = "default";
    }

    /**
     * Before check file.
     */
    public void beforeCheckFile() {
        detectors.forEach(d -> d.beforeCheckFile());
    }

    /**
     * Before check project.
     *
     * @param projectName the project name
     */
    public void beforeCheckProject(String projectName) {
        this.projectName = projectName;
       if (fileReport) {
           createReportFile(projectName);
       }
        detectors.forEach(d -> d.beforeCheckProject());
    }

    /**
     * Gets project name.
     *
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Report.
     *
     * @param m       the m
     * @param issue   the issue
     * @param message the message
     */
    public void report (Match m, TLIssue issue, String message) {
        lintDetector.report(issue,m,message);
        if (fileReport) {
            reportToFile(m, issue, message);
        }
    }

    /**
     * Report error.
     *
     * @param message the message
     */
    public void reportError (String message) {
        lintDetector.reportError(message);
    }




}

