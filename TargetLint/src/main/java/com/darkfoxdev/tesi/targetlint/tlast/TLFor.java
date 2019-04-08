package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a For control flow statement.
 * It's a {@link com.darkfoxdev.tesi.targetlint.tlast.TLLoop} characterized by an assignment (as {@link com.darkfoxdev.tesi.targetlint.tlast.TLAssignment}) and a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression} representing the step expression.
 */
public class  TLFor extends TLLoop {

    private TLAssignment assignment;
    private TLExpression stepExpression;

    /**
     * TLFor construct.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLFor(Location location, String source, TLElement parent) {
        super(location, source, parent);
        this.assignment = null;
        this.stepExpression = null;
    }

    /**
     * Gets the assignment.
     *
     * @return the assignment
     */
    public TLAssignment getAssignment() {
        return assignment;
    }

    /**
     * Gets the variable of the assignment.
     *
     * @return the variable
     */
    public TLVariable getVariable() {
        return assignment.getVariable();
    }

    /**
     * Gets the step expression.
     *
     * @return the step expression
     */
    public TLExpression getStepExpression() {
        return stepExpression;
    }


    @Override
    public void addChild(TLElement child) {
        if (assignment == null && child instanceof TLAssignment) {
            assignment = (TLAssignment) child;
        } else if (getCondition() == null && child instanceof TLExpression) {
           super.addChild(child);
        } else if (stepExpression == null && child instanceof TLExpression) {
            stepExpression = (TLExpression) child;
        } else {
            super.addChild(child);
        }

    }
}
