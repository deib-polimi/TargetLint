package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a field variable declaration.
 */
public class TLField extends TLDeclaration {
    /**
     * TLDeclaration constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLField(Location location, String source, TLElement parent) {
        super(location, source, parent);
    }
}
