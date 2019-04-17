package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;

/**
 * The type Number of parameters method filter.
 */
public class NumberOfParametersMethodFilter extends TargetFilter {

    private int number;

    /**
     * Instantiates a new Number of parameters method filter.
     *
     * @param number the number
     */
    public NumberOfParametersMethodFilter(int number) {
        super(FilterType.METHOD);
        this.number = number;
    }

    @Override
    protected boolean calculate(TLElement element) {
       if (element instanceof TLMethod) {
            TLMethod method = (TLMethod)element;
            return method.getArguments().size() == number;
        } else if (element instanceof TLElement) {
            return calculate(element.getContainingMethod());
        } return false;
    }
}
