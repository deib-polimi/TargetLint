package com.darkfoxdev.tesi.targetlint.targets.filters;


import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.checks.operations.TargetFilterAsCheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;

/**
 * The type Target filter.
 */
public abstract class TargetFilter  {

    /**
     * The enum Filter type.
     */
    public enum FilterType {
        /**
         * Class filter type.
         */
        CLASS, /**
         * Method filter type.
         */
        METHOD, /**
         * Instruction filter type.
         */
        INSTRUCTION
    }

    private TargetFilterFunction operation = this::calculate;
    private FilterType filterType;

    /**
     * Gets filter type.
     *
     * @return the filter type
     */
    public FilterType getFilterType() {
        return filterType;
    }

    /**
     * Instantiates a new Target filter.
     *
     * @param filterType the filter type
     */
    public TargetFilter(FilterType filterType) {
        this.filterType = filterType;
    }

    /**
     * Convert to check operation check operation.
     *
     * @return the check operation
     */
    public CheckOperation convertToCheckOperation () {
        return  new TargetFilterAsCheckOperation(this);
    }

    /**
     * Evaluate boolean.
     *
     * @param element the element
     * @return the boolean
     */
    public final boolean evaluate(TLElement element) {
        try {
            return operation.evaluate(element);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * And target filter.
     *
     * @param f the f
     * @return the target filter
     */
    public TargetFilter and (TargetFilter f) {
        TargetFilterFunction op = (e) -> operation.evaluate(e) && f.evaluate(e);
        return concatenate(f.filterType,op);
    }

    /**
     * Or target filter.
     *
     * @param f the f
     * @return the target filter
     */
    public TargetFilter or (TargetFilter f) {
        TargetFilterFunction op = (e) -> operation.evaluate(e) || f.evaluate(e);
        return concatenate(f.filterType,op);
    }

    /**
     * Not target filter.
     *
     * @return the target filter
     */
    public TargetFilter not () {
        TargetFilterFunction op = (e) -> !operation.evaluate(e);
        return concatenate(this.filterType,op);
    }

    /**
     * Calculate boolean.
     *
     * @param element the element
     * @return the boolean
     */
    protected abstract boolean calculate (TLElement element);

    private TargetFilter concatenate (FilterType t,TargetFilterFunction operation) {

        FilterType resultType = this.filterType;
        if (this.filterType != t) {
            resultType = FilterType.INSTRUCTION;
        }

        return new TargetFilter(resultType) {
            @Override
            protected boolean calculate(TLElement element) {
                return operation.evaluate(element);
            }
        } ;
    }

    /**
     * The interface Target filter function.
     */
    protected interface TargetFilterFunction {
        /**
         * Evaluate boolean.
         *
         * @param element the element
         * @return the boolean
         */
        boolean evaluate (TLElement element);
    }


}
