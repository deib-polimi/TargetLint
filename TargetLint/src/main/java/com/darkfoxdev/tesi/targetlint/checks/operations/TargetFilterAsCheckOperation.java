package com.darkfoxdev.tesi.targetlint.checks.operations;

import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides a way to reuse {@link com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter target filters} as check operations.
 * The {@link CheckOperation#check(List) check(...)} method applies the {@link com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter#evaluate(TLElement) evaluate(...)} method of the target filter.
 */
public class TargetFilterAsCheckOperation extends CheckOperation {

    private TargetFilter filter;

    /**
     * Instantiates a new check operation from a {@link com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter target filter}.
     *
     * @param f the target filter
     */
    public TargetFilterAsCheckOperation(TargetFilter f) {
        this.filter = f;
    }

    @Override
    public List<Match> check(List<Match> matches) {
        return matches.stream().filter(m -> filter.evaluate(m.getElement())).collect(Collectors.toList());
    }



}
