package com.darkfoxdev.tesi.targetlint;

/**
 * The Lint detector interface it's the contact point between Android Lint and TargetLint.
 * The object implementing this interface is in charge of:
 * <ul>
 *     <li>converting the AST to TLAST</li>
 *     <li>instantiating the {@link com.darkfoxdev.tesi.targetlint.TLBridge} and notify of the creation of a new {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}</li>
 *     <li>generating the report for Android Lint</li>
 * </ul>
 */
public interface LintDetectorInterface {

    /**
     * Generates the report for Android Lint.
     *
     * @param issue     the issue
     * @param match     the match
     * @param message   the message
     */
    void report (TLIssue issue, Match match, String message);

    /**
     * Report an error message using the TargetLint Logger.
     * It's used for debugging a TargetLint detector.
     *
     * @param message the message
     */
    void reportError(String message);
}
