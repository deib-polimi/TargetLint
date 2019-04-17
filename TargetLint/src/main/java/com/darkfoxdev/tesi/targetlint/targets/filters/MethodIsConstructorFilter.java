package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;

/**
 * The type Method is constructor filter.
 */
public class MethodIsConstructorFilter extends TargetFilter {

    /**
     * Instantiates a new Method is constructor filter.
     */
    public MethodIsConstructorFilter() {
        super(FilterType.METHOD);
    }

    @Override
    protected boolean calculate(TLElement element) {
        if (element == null) {
            return false;
        } if (element instanceof TLMethod) {
            return ((TLMethod)element).isConstructor();
        } else  {
            calculate(element.getContainingMethod());
        } return false;
    }
}
