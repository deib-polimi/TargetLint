package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a switch-case conditional statement.
 * It's a {@link com.darkfoxdev.tesi.targetlint.tlast.TLCondStmt} characterized by a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression}, representing the variable, and a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLCase}.
 */
public class TLSwitch extends TLCondStmt {

    private TLExpression variable = null;
    private ArrayList<TLCase> caseExpressions = new ArrayList<>();

    /**
     * TLSwitch constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLSwitch(Location location, String source, TLElement parent) {
        super(location, source, parent);
    }

    /**
     * Gets the variable.
     *
     * @return the variable
     */
    public TLElement getVariable() {
        return variable;
    }

    /**
     * Gets the list of case blocks.
     *
     * @return the case blocks
     */
    public ArrayList<TLCase> getCaseExpressions() {
        return caseExpressions;
    }

    @Override
    public void addChild(TLElement child) {
        if (variable == null && (child instanceof TLExpression)) {
            variable = (TLExpression) child;
        } else if (child instanceof TLCase) {
            caseExpressions.add((TLCase) child);
        }
    }
}
