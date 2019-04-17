package com.darkfoxdev.tesi.targetlint.checks.operations;

import com.darkfoxdev.tesi.targetlint.Match;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * This class provides check operations for numeric comparison on how many {@link com.darkfoxdev.tesi.targetlint.Match matches} are found.
 */
public class MatchNumberCheckOperation extends CheckOperation {

    /**
     * Creates a check operations that returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} only if they are more than n.
     *
     * @param n the number n
     * @return the matches if they are more than n; an empty list otherwise.
     */
    public static MatchNumberCheckOperation createGreaterThanCheckOperation(int n) {
        return new MatchNumberCheckOperation((i) -> i > n);
    }

    /**
     * Creates a check operations that returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} only if they are more or equal to n.
     *
     * @param n the number n
     * @return the matches if they are more or equal to n; an empty list otherwise.
     */
    public static MatchNumberCheckOperation createGreaterEqualThanCheckOperation(int n) {
        return new MatchNumberCheckOperation((i) -> i >= n);
    }

    /**
     * Creates a check operations that returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} only if they are less than n.
     *
     * @param n the number n
     * @return the matches if they are less than n; an empty list otherwise.
     */
    public static MatchNumberCheckOperation createLessThanCheckOperation(int n) {
        return new MatchNumberCheckOperation((i) -> i < n);
    }

    /**
     * Creates a check operations that returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} only if they are less or equal to n.
     *
     * @param n the number n
     * @return the matches if they are less or equal to n; an empty list otherwise.
     */
    public static MatchNumberCheckOperation createLessEqualThanCheckOperation(int n) {
        return new MatchNumberCheckOperation((i) -> i <= n);
    }

    /**
     * Creates a check operations that returns the {@link com.darkfoxdev.tesi.targetlint.Match matches} only if they are exactly n.
     *
     * @param n the number n
     * @return the matches if they are exactly n; an empty list otherwise.
     */
    public static MatchNumberCheckOperation createEqualCheckOperation (int n) {
        return new MatchNumberCheckOperation((i) -> i == n);
    }

    private Function<Integer,Boolean> conditionOperation;

    private MatchNumberCheckOperation (Function<Integer,Boolean> operation) {
        this.conditionOperation = operation;
    }

    @Override
    public List<Match> check(List<Match> matches) {
        if (conditionOperation.apply(matches.size())) {
            return matches;
        } else {
            return Collections.emptyList();
        }
    }
}
