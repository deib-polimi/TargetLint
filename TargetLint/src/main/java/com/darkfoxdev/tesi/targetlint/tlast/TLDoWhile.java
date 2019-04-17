package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a Do-While construct.
 */
public class TLDoWhile extends TLLoop {

    /**
     * TLDoWhile constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLDoWhile(Location location, String source, TLElement parent) {
        super(location, source, parent);
    }
}
