package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLClass;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;

/**
 * The type Extends filter.
 */
public class ExtendsFilter extends TargetFilter {

    private String superclass;

    /**
     * Instantiates a new Extends filter.
     *
     * @param superclass the superclass
     */
    public ExtendsFilter(String superclass) {
        super(FilterType.CLASS);
        this.superclass = superclass;
    }

    @Override
    public boolean calculate(TLElement element) {
        TLClass classElement;
        if (element instanceof TLClass) {
            classElement = (TLClass) element;
        } else {
            classElement = element.getContainingClass();
        }

        return classElement.getSuperClass().contains(superclass);

    }
}
