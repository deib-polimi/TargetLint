package com.darkfoxdev.tesi.targetlint;

import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.targets.Target;

/**
 * Match represents an element found in the code that is interesting for the target.
 * It's defined by the generating target, the element found and a context element.
 * The context element is the containing {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} corresponding to
 * the {@link com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel TargetSearchLevel}
 * (e.g. for {@link com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel#METHOD TargetSearchLevel.METHOD},
 *  it's the {@link com.darkfoxdev.tesi.targetlint.tlast.TLMethod TLMethod} containing the found element).
 *
 */
public final class Match {

    private Target target;
    private TLElement element;
    private TLElement context;

    /**
     * Match constructor.
     *
     * @param target    the target
     * @param element   the element
     * @param context   the context
     */
    public Match(Target target, TLElement element, TLElement context) {
        this.target = target;
        this.element = element;
        this.context = context;
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    public TLElement getContext() {
        return context;
    }

    /**
     * Gets the target.
     *
     * @return the target
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Gets the element.
     *
     * @return the element
     */
    public TLElement getElement() {
        return element;
    }



}
