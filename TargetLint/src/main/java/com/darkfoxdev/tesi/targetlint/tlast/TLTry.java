package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * The type Tl try.
 */
public class TLTry extends TLElement {

    private ArrayList<TLElement> tryBlock = new ArrayList<>();
    private ArrayList<TLCatch> catches = new ArrayList<>();
    private ArrayList<TLElement> finallyBlock = new ArrayList<>();

    private int finallyLine;

    /**
     * Instantiates a new Tl try.
     *
     * @param location    the location
     * @param source      the source
     * @param parent      the parent
     * @param finallyLine the finally line
     */
    public TLTry(Location location, String source, TLElement parent,int finallyLine) {
        super(location, source, parent);
        this.finallyLine = finallyLine;
    }

    /**
     * Gets try block.
     *
     * @return the try block
     */
    public ArrayList<TLElement> getTryBlock() {
        return tryBlock;
    }

    /**
     * Gets finally block.
     *
     * @return the finally block
     */
    public ArrayList<TLElement> getFinallyBlock() {
        return finallyBlock;
    }

    /**
     * Gets catches.
     *
     * @return the catches
     */
    public ArrayList<TLCatch> getCatches() {
        return catches;
    }

    @Override
    public void addChild(TLElement child) {
        if (child instanceof TLCatch) {
            catches.add((TLCatch)child);
        } else if (finallyLine > child.getLocation().getStart().getLine()) {
            tryBlock.add(child);
        } else {
            finallyBlock.add(child);
        }

    }
}
