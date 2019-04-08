package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents the argument of a function.
 */
public class TLArgument extends TLVariable {

    /**
     * Instantiates a new TLArgument.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     * @param type     the type
     * @param name     the name
     */
    public TLArgument(Location location, String source, TLElement parent, TLType type, String name) {
        super(location, source, parent, type, name);
    }
}
