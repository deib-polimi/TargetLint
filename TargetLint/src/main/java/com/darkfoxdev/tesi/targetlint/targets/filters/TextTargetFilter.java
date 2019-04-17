package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.TLBridge;

import java.util.function.Function;

/**
 * The type Text target filter.
 */
public abstract class TextTargetFilter extends TargetFilter {

    /**
     * The Query.
     */
    protected String query;
    /**
     * The Operation.
     */
    protected Function<String,Boolean> operation;

    /**
     * The enum Mode.
     */
    public enum Mode {
        /**
         * Contains mode.
         */
        CONTAINS, /**
         * Equals mode.
         */
        EQUALS
    }

    /**
     * Instantiates a new Text target filter.
     *
     * @param type  the type
     * @param mode  the mode
     * @param query the query
     */
    public TextTargetFilter(TargetFilter.FilterType type,Mode mode,String query) {
        super(type);
        this.query = query;
        if (mode == Mode.EQUALS) {
         this.operation = x-> {
                if (x!=null) {
                 return x.equals(query);
                } return false;
            };
        } else if (mode == Mode.CONTAINS) {
            this.operation = x-> {
                if (x!=null) {
                    return x.contains(query);
                } return false;
            };
        }
    }

    /**
     * Instantiates a new Text target filter.
     *
     * @param type  the type
     * @param query the query
     */
    public TextTargetFilter(TargetFilter.FilterType type,String query) {
        this(type,Mode.EQUALS,query);
    }





}
