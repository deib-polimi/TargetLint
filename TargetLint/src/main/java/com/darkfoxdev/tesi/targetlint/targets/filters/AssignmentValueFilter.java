package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLAssignment;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;

/**
 * The type Assignment value filter.
 */
public class AssignmentValueFilter extends TextTargetFilter {

    /**
     * Instantiates a new Assignment value filter.
     *
     * @param query the query
     * @param mode  the mode
     */
    public AssignmentValueFilter(String query, Mode mode) {
        super(FilterType.INSTRUCTION,mode,query);
    }

    /**
     * Instantiates a new Assignment value filter.
     *
     * @param query the query
     */
    public AssignmentValueFilter(String query) {
        super(FilterType.INSTRUCTION,Mode.EQUALS,query);
    }


    @Override
    protected boolean calculate(TLElement element) {
        TLElement value = null;
        if (element instanceof TLAssignment) {
            value = ((TLAssignment) element).getExpression();
        } try {
            return operation.apply(value.getSource());
        } catch (Exception e) {
            return false;
        }
    }
}
