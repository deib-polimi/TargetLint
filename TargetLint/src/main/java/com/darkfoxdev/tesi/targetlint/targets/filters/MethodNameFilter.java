package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.tlast.TLClass;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;

/**
 * The type Method name filter.
 */
public class MethodNameFilter extends TextTargetFilter  {

    /**
     * Instantiates a new Method name filter.
     *
     * @param query the query
     * @param mode  the mode
     */
    public MethodNameFilter(String query,Mode mode) {
        super(FilterType.METHOD,mode,query);
    }


    /**
     * Instantiates a new Method name filter.
     *
     * @param query the query
     */
    public MethodNameFilter(String query) {
        super(FilterType.METHOD,Mode.EQUALS,query);
    }

    @Override
    public boolean calculate(TLElement element) {
        TLMethod method;
        if (element instanceof TLClass) {
            return false;
        } else if (element instanceof TLMethod) {
            method = (TLMethod) element;
        } else {
            method = element.getContainingMethod();
        }
        try {
            return operation.apply(method.getName());

        } catch (NullPointerException e) {
            TLBridge.log(e.toString()); //TODO!!!!!
            TLBridge.log(e.getStackTrace());
            return false;
        }
    }
}
