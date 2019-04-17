package com.darkfoxdev.tesi.targetlint;


/**
 * The type Tl issue.
 */
public class TLIssue  {

    private String name;
    private String briefDescription;
    private String explanation;

    private String category;
    private int severity;
    private int priority;

    public TLIssue(String name, String briefDescription, String explanation, String category, int severity, int priority) {
        this.name = name;
        this.briefDescription = briefDescription;
        this.explanation = explanation;
        this.category = category;
        this.severity = severity;
        this.priority = priority;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets brief description.
     *
     * @return the brief description
     */
    public String getBriefDescription() {
        return briefDescription;
    }

    /**
     * Gets explanation.
     *
     * @return the explanation
     */
    public String getExplanation() {
        return explanation;
    }

    public String getCategory() {
        return category;
    }

    public int getSeverity() {
        return severity;
    }

    public int getPriority() {
        return priority;
    }
}
