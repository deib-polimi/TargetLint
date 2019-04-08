package com.darkfoxdev.tesi.targetlint.targets.filters;


import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.tlast.TLCall;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;

/**
 * The type Call name filter.
 */
public class CallNameFilter extends TextTargetFilter {

    /**
     * Instantiates a new Call name filter.
     *
     * @param query the query
     * @param mode  the mode
     */
    public CallNameFilter(String query, Mode mode ) {
        super(FilterType.INSTRUCTION,mode,query);
    }

    /**
     * Instantiates a new Call name filter.
     *
     * @param query the query
     */
    public CallNameFilter(String query ) {
        super(FilterType.INSTRUCTION,Mode.EQUALS,query);
    }

    @Override
    protected boolean calculate(TLElement element) {
        if (element instanceof TLCall) {
            try {
               TLCall expr = (TLCall) element;
                return operation.apply(expr.getName());
            } catch (Exception e) {
                TLBridge.log(e.getStackTrace()); //TODO !!!
                return false;
            }
        } return false;
    }
}
