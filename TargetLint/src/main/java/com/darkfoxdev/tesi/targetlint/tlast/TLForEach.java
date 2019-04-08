package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a for each control flow statement.
 * It's a {@link com.darkfoxdev.tesi.targetlint.tlast.TLLoop} characterized by a variable (as {@link com.darkfoxdev.tesi.targetlint.tlast.TLVariable}) and a collection (represented by a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression}).
 */
public class TLForEach extends TLLoop {

   private TLVariable variable;
   private TLExpression collection;

    /**
     * TLForEach constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLForEach(Location location, String source, TLElement parent) {
        super(location, source, parent);
        this.variable = null;
        this.collection = null;
        super.addChild(new TLExpression(null,null,null,null));
    }

    /**
     * Gets the variable.
     *
     * @return the variable
     */
    public TLVariable getVariable() {
        return variable;
    }

    /**
     * Gets the collection.
     *
     * @return the collection
     */
    public TLElement getCollection() {
        return collection;
    }

    @Override
    public TLElement getCondition() {
        return null;
    }

    @Override
    public void addChild(TLElement child) {
        if (child instanceof TLVariable && variable == null) {
            variable = (TLVariable) child;
        } else if ((child instanceof TLExpression) && collection == null) {
            collection = (TLExpression) child;
        } else {
            super.addChild(child);
        }
    }
}
