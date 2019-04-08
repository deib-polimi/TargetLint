package com.darkfoxdev.tesi.targetlint.checks.operations;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.targets.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This check operation is defined by a second target and an operation of type Function&lt;Match,T&gt;.
 * The {@link CheckOperation#check(List) check(...)} method applies the operation to both the input {@link com.darkfoxdev.tesi.targetlint.Match matches} and second target's matches.
 * Output matches are the ones for which exists a match (at least one) in the second {@link com.darkfoxdev.tesi.targetlint.targets.Target target} that, once applied the operation, give the same result.
 *
 * <blockquote>For example, in a BroadcastReceiver, we can look for registrations without unregistrations:
 *    <ul>
 *       <li>first target targets the registerReceiver(...) method</li>
 *       <li>second target targets the unregisterReceiver(...) method</li>
 *       <li>create a new ReferenceMatchCheckOperation with an operation that extract the argument name from the call</li>
 *       <li>negate the check operation so that return matches for which doesn't exists an unregisterReceiver(...) call</li>
 *    </ul>
 *  </blockquote>
 *
 * @param <T> the type parameter
 */
public class ReferenceMatchCheckOperation<T> extends CheckOperation {

    private Target t2;

    private Function<Match,T> operation;

    /**
     * Constructor.
     *
     * @param secondTarget  the second target
     * @param operation     the operation
     */
    public ReferenceMatchCheckOperation(Target secondTarget,Function<Match,T> operation) {
        this.operation = operation;
        this.t2 = secondTarget;
    }

    @Override
    public List<Match> check(List<Match> matches) {
        List<Match> result = new ArrayList<>();
        matches.forEach(m -> {
            T var = operation.apply(m);
            if (var != null) {
                if (t2.getMatches(m.getContext()).stream().anyMatch(tm -> var.equals(operation.apply(tm)))) {
                    result.add(m);
                }}
        });
        return result;
    }

}