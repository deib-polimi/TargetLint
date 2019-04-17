package com.darkfoxdev.tesi.targetlint.checks;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.targets.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Check.
 * It's responsible of filtering out the {@link com.darkfoxdev.tesi.targetlint.Match matches} by processing the {@link CheckOperation check operations}.
 * Resulting matches are the ones for which a report should be filed.
 */
public class Check {

    private Target target;
    private List<CheckOperation> operations;
    private String message;

    private Check(Target target, String message, CheckOperation... operations) {
        this.target = target;
        this.operations = Arrays.asList(operations);
        this.message = message;
    }

    /**
     * Create and return a check.
     *
     * @param tlDetector the detector
     * @param target     the target
     * @param message    the message
     * @param operations the operations
     * @return the check
     */
    public static Check createCheck(TLDetector tlDetector, Target target, String message, CheckOperation... operations) {
        Check c = new Check(target, message, operations);
        tlDetector.getChecks().add(c);
        return c;
    }

    /**
     * Message getter.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Executes the check operations returning the filtered list of {@link com.darkfoxdev.tesi.targetlint.Match matches}.
     *
     * @return the list of matches
     */
    public List<Match> process() {

        List<Match> result = new ArrayList<>();
        for (TLElement reference : target.getMatches().keySet()) {
            List<Match> partialResult = target.getMatches().get(reference);
            for (CheckOperation operation : operations) {
                partialResult = operation.check(partialResult);
            }
            result.addAll(partialResult);
        }
        return result;
    }

}

