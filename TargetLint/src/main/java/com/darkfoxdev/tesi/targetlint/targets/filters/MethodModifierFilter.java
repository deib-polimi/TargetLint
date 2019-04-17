package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;

public class MethodModifierFilter extends TargetFilter {

    private String modifierString;

    /**
     * Instantiates a new Target filter.
     *
     * @param modifierString method modifier
     */
    public MethodModifierFilter(String modifierString) {
        super(FilterType.METHOD);
        this.modifierString = modifierString;

    }

    @Override
    protected boolean calculate(TLElement element) {
        if(element instanceof TLMethod) {
            TLMethod method = (TLMethod)element;
            return method.getAccessModifier().equals(modifierString);
        } else {
            return false;
        }
    }
}
