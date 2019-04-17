package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a qualified expression.
 * It's a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression} characterized by a receiver and a selector; both represented by a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression}.
 * The receiver is the part to the left of the dot.
 * Vice versa, the selector is the part to the right.
 */
public class TLQualified extends TLExpression {

    private TLExpression receiver;
    private TLExpression selector;


    /**
     * TLQualified constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     * @param type     the type
     */
    public TLQualified(Location location, String source, TLElement parent, TLType type) {
        super(location, source, parent, type);
    }


    /**
     * Gets the receiver.
     *
     * @return the receiver
     */
    public TLExpression getReceiver() {
        return receiver;
    }

    /**
     * Gets the selector.
     *
     * @return the selector
     */
    public TLExpression getSelector() {
        return selector;
    }

    @Override
    public void addChild(TLElement child) {
        if (child instanceof TLExpression && receiver == null) {
            receiver = (TLExpression) child;
        } else if (child instanceof TLExpression && selector == null) {
            selector = (TLExpression)child;
        }
    }
}