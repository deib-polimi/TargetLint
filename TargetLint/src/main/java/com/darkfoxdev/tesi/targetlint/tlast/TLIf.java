package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a If conditional statement.
 * It's a {@link com.darkfoxdev.tesi.targetlint.tlast.TLCondStmt} characterized by:
 * <ul>
 *     <li>a {@link com.darkfoxdev.tesi.targetlint.tlast.TLExpression} representing a condition</li>
 *     <li>a list of elseif blocks as a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLIf}</li>
 *     <li>a block as a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}</li>
 *     <li>an else block as a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}</li>
 *     <li>a flag to keep track if it's an <code>else if</code> statement</li>
 *     <li>a flag to keep track if it's written using the ternary operator</li>
 * </ul>
 */
public class TLIf extends TLCondStmt {

    private TLExpression condition;
    private ArrayList<TLIf> elseIfs;
    private ArrayList<TLElement> block;
    private TLElse elseBlock;

    private boolean isElseIf;
    private boolean isTernary;


    /**
     * TLIf Constructor
     *
     * @param location  the location
     * @param source    the source code
     * @param parent    the parent node
     * @param isElseIf  the <code>else if</code> flag
     * @param isTernary <code>true</code> if it's written using the ternary operator
     */
    public TLIf(Location location, String source, TLElement parent, boolean isElseIf, boolean isTernary) {
        this.location = location;
        this.source = source;
        this.parent = parent;
        this.condition = null;
        this.elseIfs = new ArrayList<>();
        this.block = new ArrayList<>();
        this.elseBlock =  null;
        this.isElseIf = isElseIf;
        this.isTernary = isTernary;
        if (parent != null) {
            parent.addChild(this);
        }
    }

    /**
     * Gets the condition.
     *
     * @return the condition
     */
    public TLElement getCondition() {
        return condition;
    }

    /**
     * Gets the list of <code>else if</code> TLIf.
     *
     * @return the list of <code>else if</code>s
     */
    public ArrayList<TLIf> getElseIfs() {
        return elseIfs;
    }

    /**
     * Gets the list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents the list of instructions in the block of the if.
     *
     * @return the list of instructions in the block
     */
    public ArrayList<TLElement> getBlock() {
        return block;
    }

    /**
     * Gets the list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents the list of instructions in the else block of the if.
     *
     * @return the list of instructions in the block
     */
    public TLElse getElseBlock() {
        return elseBlock;
    }

    /**
     * Returns the flag used to keep track if it's an <code>else if</code> statement
     *
     * @return <code>true</code> if it's an <code>else if</code> statement
     */
    public boolean isElseIf() {
        return isElseIf;
    }

    /**
     * Returns the flag used to keep track if it's written using the ternary operator
     *
     * @return <code>true</code> if it's written using the ternary operator
     */
    public boolean isTernary() {
        return isTernary;
    }

    @Override
    public void addChild(TLElement child) {
        if (condition == null && (child instanceof TLExpression) || child instanceof TLVariable) {//TODO : controllare
            condition = (TLExpression) child;
        } else  if (isTernary()) {
            addChildToTernaryIf(child);
        } else {
            addChildToNormalIf(child);
        }
    }

    private void addChildToNormalIf (TLElement child) {
        if ((child instanceof TLIf) && ((TLIf) child).isElseIf()) {
            elseIfs.add((TLIf) child);
        } else if (child instanceof TLElse && elseBlock == null) {
            elseBlock = (TLElse) child;
        } else {
            block.add(child);
        }
    }

    private void addChildToTernaryIf (TLElement child) {
        if (block.size() == 0) {
            block.add(child);
        } else {

            TLElse elseVar = new TLElse(child.location,child.source,null);
            elseVar.addChild(child);
            child.parent = elseVar;
            elseVar.parent = this;
            elseBlock =  elseVar; //!!!!!!!! TODO controllare

        }
    }


}
