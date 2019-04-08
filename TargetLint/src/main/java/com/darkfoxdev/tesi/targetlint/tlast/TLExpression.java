package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents an expression.
 * It's characterized by a type (as {@link com.darkfoxdev.tesi.targetlint.tlast.TLType}) and it has multiple children (as {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}).
 */
public class TLExpression extends TLElement {

    private TLType type;

    private ArrayList<TLElement> children = new ArrayList<>();

    /**
     * TLExpression constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     * @param type     the type
     */
    public TLExpression(Location location, String source, TLElement parent, TLType type) {
        super(location, source, parent);
        this.type = type;
    }

    /**
     * Gets the type as string.
     *
     * @return the type
     */
    public String getTypeAsString() {
        return type.getQualifiedName();
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public TLType getType() {return type;}

    /**
     * Gets children.
     *
     * @return the children
     */
    public ArrayList<TLElement> getChildren() {
        return children;
    }

    @Override
    public void addChild(TLElement child) {
        children.add(child);
    }
}
