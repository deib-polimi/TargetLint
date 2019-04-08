package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;

/**
 * The type Method has annotation filter.
 */
public class MethodHasAnnotationFilter extends TextTargetFilter {

    /**
     * Instantiates a new Method has annotation filter.
     *
     * @param query the query
     * @param mode  the mode
     */
    public MethodHasAnnotationFilter(String query,Mode mode) {
        super(FilterType.METHOD,mode,query);
    }

    @Override
    protected boolean calculate(TLElement element) {
        if (element == null) {
            return false;
        } if (element instanceof TLMethod) {
            TLMethod method = (TLMethod)element;
            return  method.getAnnotations().stream().anyMatch(a -> operation.apply(a));
        } else  {
            return calculate(element.getContainingMethod());
        }
    }
}
