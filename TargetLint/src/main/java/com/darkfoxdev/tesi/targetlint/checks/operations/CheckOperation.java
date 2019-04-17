package com.darkfoxdev.tesi.targetlint.checks.operations;

import com.darkfoxdev.tesi.targetlint.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * The type Check Operation.
 * It's the processing unit of a {@link com.darkfoxdev.tesi.targetlint.checks.Check Check}: every check operation takes a list of {@link com.darkfoxdev.tesi.targetlint.Match matches} and, through the {@link CheckOperation#check(List) check(...)} method, filters them returning only the relevant ones.
 * Check operations can be chained in series (using {@link CheckOperation#and(CheckOperation) and(...)} method) or in parallel (using {@link CheckOperation#or(CheckOperation) or(...)} method).
 */
public abstract class CheckOperation {


    private Function<List<Match>,List<Match>> operation = this::check;

    /**
     * Perform the check operation.
     *
     * @param matches the list of matches
     * @return the list of filtered matches
     */
    public abstract List<Match> check (List<Match> matches);

    /**
     * Returns a new check operation which, once executed, returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} satisfying both check operation.
     *
     * @param c the second check operation
     * @return the new check operation
     */
    public CheckOperation and (CheckOperation c) {
        return new CheckOperation() {
            @Override
            public List<Match> check(List<Match> matches) {
                return c.check(operation.apply(matches));
            }
        };
    }

    /**
     * Returns a new check operation which, once executed, returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} satisfying at least one check operation.
     *
     * @param c the second check operation
     * @return the new check operation
     */
    public CheckOperation or (CheckOperation c) {
        return new CheckOperation() {
            @Override
            public List<Match> check(List<Match> matches) {
                List<Match> m1 = c.check(matches);
                for (Match m : operation.apply(matches)) {
                    if (!m1.contains(m)){
                        m1.add(m);
                    }
                }
                return m1;
            }};
    }

    /**
     * Returns a new check operation which, once executed, returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} not satisfying the old check operation.
     *
     * @return the new check operation
     */
    public CheckOperation not () {
        return new CheckOperation() {
            @Override
            public List<Match> check(List<Match> matches) {
                List<Match> result = new ArrayList<>(matches);
                List<Match> m1 = operation.apply(matches);
                for (Match m : m1) {
                    if (result.contains(m)){
                        result.remove(m);
                    }
                }
                return result;
            }
        };
    }



}


