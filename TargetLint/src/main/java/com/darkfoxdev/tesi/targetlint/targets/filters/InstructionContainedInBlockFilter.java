package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;

/**
 * The type Instruction contained in block filter.
 */
public class InstructionContainedInBlockFilter extends TargetFilter  {

    private Class<? extends TLElement> block;

    /**
     * Instantiates a new Instruction contained in block filter.
     *
     * @param block the block
     */
    public InstructionContainedInBlockFilter(Class<? extends TLElement> block) {
        super(FilterType.INSTRUCTION);
        this.block = block;
    }

    @Override
    public boolean calculate(TLElement element) {
        if (element != null) {
            return element.hasAncestorOfType(block);
        } return false;
    }
}
