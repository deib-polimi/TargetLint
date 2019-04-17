package com.darkfoxdev.tesi.targetlint.checks.operations;

import com.darkfoxdev.tesi.targetlint.Match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This check groups the input {@link com.darkfoxdev.tesi.targetlint.Match matches} in different sets using the grouping predicate, and then executes a list of check operations on every set.
 * Grouping predicate is of type Function&lt;Match,T&gt;, where T is the return type of the value on which matches are grouped.
 *
 * <blockquote>For example, if we target the unregisterReceiver method, in order to avoid multiple unregistrations for the same variable, we need to group the matches by the variable name and then check for each set if there is more than one match (using an appropriate {@link MatchNumberCheckOperation}).</blockquote>
 *
 * @param <T> the return type of the value on which matches are grouped
 */
public class GroupAndExecuteCheckOperation<T> extends CheckOperation {

    private Function<Match,T> groupPredicate;
    private List<CheckOperation> checkOperationsToExecute;

    /**
     * Constructor
     *
     * @param groupPredicate  the grouping predicate
     * @param checkOperations the check operations to be applied
     */
    public GroupAndExecuteCheckOperation(Function<Match,T> groupPredicate,CheckOperation... checkOperations) {
        this.groupPredicate = groupPredicate;
        this.checkOperationsToExecute = Arrays.asList(checkOperations);
    }

    @Override
    public List<Match> check(List<Match> matches) {
        List<Match> result = new ArrayList<>();
        List<T> vars = matches.stream().map(groupPredicate).filter(x -> x != null).distinct().collect(Collectors.toList());

        for (T x : vars) {
            List<Match> matchForX = matches.stream().filter(match -> x.equals(groupPredicate.apply(match))).collect(Collectors.toList());
            List<Match> partialResult = new ArrayList<>(matchForX);
            for (CheckOperation operation : checkOperationsToExecute) {
                partialResult = operation.check(partialResult);
            } result.addAll(partialResult);
        } return result;
    }
}
