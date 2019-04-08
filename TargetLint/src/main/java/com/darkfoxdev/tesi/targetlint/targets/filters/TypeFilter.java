package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLExpression;

/**
 * The type Type filter.
 */
public class TypeFilter extends TargetFilter {

    private String type;

    /**
     * Instantiates a new Type filter.
     *
     * @param type the type
     */
    public TypeFilter(String type) {
        super(FilterType.INSTRUCTION);
        this.type = type;
    }

    @Override
    public boolean calculate(TLElement element) {
            if (element instanceof TLExpression) {
                return (((TLExpression)element).getTypeAsString().equals(type)
                        || ((TLExpression)element).getType().getSuperTypes().contains(type));
            }
            return false;

    }
}
