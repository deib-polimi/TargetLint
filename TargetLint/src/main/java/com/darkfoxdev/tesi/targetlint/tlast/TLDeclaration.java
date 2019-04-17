package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a declaration.
 */
public class TLDeclaration extends TLAssignment {

    private boolean isField;

    /**
     * TLDeclaration constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLDeclaration(Location location, String source, TLElement parent) {
        super(location, source, parent,true);
        this.isField = false;
    }

    /**
     * TLDeclaration constructor for a field variable.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     * @param isField  <code>true</code> if it's the declaration of a field variable
     */
    public TLDeclaration(Location location, String source, TLElement parent,boolean isField) {
        super(location, source, parent,true);
        this.isField = isField;
    }

    /**
     * Returns <code>true</code> if it's the declaration of a field variable.
     *
     * @return <code>true</code> if it's the declaration of a field variable.
     */
    public boolean isField() {
        return isField;
    }
}
