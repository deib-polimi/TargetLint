package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a While construct.
 */
public class TLWhile extends TLLoop {
    /**
     * Instantiates a new Tl while.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLWhile(Location location, String source, TLElement parent) {
        super(location, source, parent);
    }
}
