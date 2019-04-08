package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a conditional statement.
 */
public abstract class TLCondStmt extends TLElement {

    /**
     * TLCondStmt constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLCondStmt(Location location, String source, TLElement parent) {
        super(location, source, parent);
    }

    /**
     * @y.exclude
     */
    public TLCondStmt() {

    }


}
