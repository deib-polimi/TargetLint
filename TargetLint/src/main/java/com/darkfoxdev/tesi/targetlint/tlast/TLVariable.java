package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a variable.
 * It's a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression} characterized by its name (as a string).
 */
public class TLVariable extends TLExpression {

    private String name;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * TLVariable constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     * @param type     the type
     * @param name     the name
     */
    public TLVariable(Location location, String source, TLElement parent, TLType type, String name) {
        super(location, source, parent,type);

        this.name = name;
    }

    @Override
    public void addChild(TLElement child) {

    }
}
