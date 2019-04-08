package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents an assignment instruction.
 * It's characterized by a variable (as {@link com.darkfoxdev.tesi.targetlint.tlast.TLVariable}) and the value of the assignment (as {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression}).
 */
public class TLAssignment extends TLExpression {

    private TLVariable variable;
    private TLExpression expression;
    private boolean isDeclaration;

    /**
     * Instantiates a new TLAssignment.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLAssignment(Location location, String source, TLElement parent) {
        super(location, source, parent,null);
        this.variable = null;
        this.expression = null;
        this.isDeclaration = false;
    }

    /**
     * Instantiates a new TLAssignment.
     * <code>isDeclaration</code> flag is <code>true</code> when the TLAssignment represent a declaration.
     *
     * @param location      the location
     * @param source        the source
     * @param parent        the parent
     * @param isDeclaration the is declaration
     */
    public TLAssignment(Location location, String source, TLElement parent,boolean isDeclaration) {
        super(location, source, parent,null);
        this.variable = null;
        this.expression = null;
        this.isDeclaration = isDeclaration;
    }

    /**
     * Returns <code>true</code> when the TLAssignment represent a declaration.
     *
     * @return the boolean
     */
    public boolean isDeclaration() {
        return isDeclaration;
    }

    /**
     * Gets the assignment variable.
     *
     * @return the variable
     */
    public TLVariable getVariable() {
        return variable;
    }

    /**
     * Gets the assignment expression.
     *
     * @return the expression
     */
    public TLElement getExpression() {
        return expression;
    }

    @Override
    public void addChild(TLElement child) {
        if ((child instanceof TLVariable) && variable == null) {
            variable = (TLVariable) child;
        } else if (child instanceof TLExpression)  {
            expression = (TLExpression) child;
        }
    }

    @Override
    public TLType getType() {
        return variable.getType();
    }

    @Override
    public String getTypeAsString() {
        return variable.getTypeAsString();
    }
}
