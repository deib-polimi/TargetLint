package com.darkfoxdev.tesi.targetlint.tlast;


import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a loop.
 * It's defined by a condition and a body, represented respectively by a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression} and a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}.
 */
public class TLLoop extends TLElement {

    private TLExpression condition;
    /**
     * The body of the loop.
     */
    protected ArrayList<TLElement> body;

    /**
     * Instantiates a new TlLoop.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLLoop(Location location, String source, TLElement parent) {
        super(location, source, parent);
        this.body = new ArrayList<>();
        this.condition = null;
    }

    /**
     * Gets the condition of the loop.
     *
     * @return the condition of the loop
     */
    public TLElement getCondition() {
        return condition;
    }

    /**
     * Gets body of the loop.
     *
     * @return the body of the loop
     */
    public ArrayList<TLElement> getBody() {
        return body;
    }

    @Override
    public void addChild(TLElement child) {
        if (child instanceof TLExpression && condition == null) {
            condition = (TLExpression) child;
        } else  {
            body.add(child);
        }
    }
}
