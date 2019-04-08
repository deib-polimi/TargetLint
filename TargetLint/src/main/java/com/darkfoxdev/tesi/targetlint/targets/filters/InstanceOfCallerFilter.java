package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLQualified;

/**
 * The type Instance of caller filter.
 */
public class InstanceOfCallerFilter extends TargetFilter   {

    private String type;

    /**
     * Instantiates a new Instance of caller filter.
     *
     * @param callerType the caller type
     */
    public InstanceOfCallerFilter(String callerType) {
        super(FilterType.INSTRUCTION);
        this.type = callerType;
    }

    @Override
    public boolean calculate(TLElement tlElement) {

        if (tlElement.getParent() instanceof TLQualified) {
           TLQualified element = ((TLQualified)tlElement.getParent());
            return (element.getReceiver().getTypeAsString().equals(type)
                    || element.getType().getSuperTypes().contains(type));
        }
        return false;

        }


}
