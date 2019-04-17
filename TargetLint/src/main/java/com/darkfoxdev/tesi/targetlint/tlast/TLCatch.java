package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents the catch section of a try-catch construct.
 *  * It's characterized by a list of exceptions (a list of String) and an block (a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}).
 */
public class TLCatch extends TLElement {

    private List<String> exceptionType;
    private List<TLElement> block = new ArrayList<>();

    /**
     * Instantiates a new TLCatch.
     *
     * @param location      the location
     * @param source        the source code
     * @param parent        the parent node
     * @param exceptions    the exception list
     */
    public TLCatch(Location location, String source, TLElement parent,List<String> exceptions) {
        super(location, source, parent);
        this.exceptionType = exceptions;
    }

    /**
     * Gets exception list.
     *
     * @return the exception list
     */
    public List<String> getExceptionType() {
        return exceptionType;
    }

    /**
     * Gets block of instructions.
     *
     * @return the block
     */
    public List<TLElement> getBlock() {
        return block;
    }

    @Override
    public void addChild(TLElement child) {
        block.add(child);
    }
}
