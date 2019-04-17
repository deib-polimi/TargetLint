package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.tlast.TLClass;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;

/**
 * The type Class name filter.
 */
public class ClassNameFilter extends TextTargetFilter  {

    /**
     * Instantiates a new Class name filter.
     *
     * @param query the query
     * @param mode  the mode
     */
    public ClassNameFilter(String query,Mode mode) {
        super(FilterType.CLASS,mode,query);
    }

    /**
     * Instantiates a new Class name filter.
     *
     * @param query the query
     */
    public ClassNameFilter(String query) {
        super(FilterType.CLASS,Mode.EQUALS,query);
    }

    @Override
    public boolean calculate(TLElement element) {
        TLClass classElement;
        if (element instanceof TLClass) {
            classElement = (TLClass) element;
        } else {
            classElement = element.getContainingClass();
        } try {
            if (operation.apply(classElement.getQualifiedName())) {
                return true;
            }
        } catch (NullPointerException ex) {
            TLBridge.log(ex.getStackTrace()); //TODO !!!
            return false;
        } return false;
    }
}
