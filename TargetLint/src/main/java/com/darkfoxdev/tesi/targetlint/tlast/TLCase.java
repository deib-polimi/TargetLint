package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents the case section of a switch-case construct.
 * It's characterized by a value (a TLElement?) and an instruction list (a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}).
 */
public class TLCase extends TLCondStmt {

    private TLElement value = null;
    private ArrayList<TLElement> instructions = new ArrayList<>();

    /**
     * Instantiates a new TLCase.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLCase(Location location, String source, TLElement parent) {
        super(location, source, parent);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public TLElement getValue() {
        return value;
    }

    /**
     * Gets instructions.
     *
     * @return the instructions
     */
    public ArrayList<TLElement> getInstructions() {
        return instructions;
    }

    @Override
    public void addChild(TLElement child) {
        if (value == null) {
            value = child;
        } else {
            instructions.add(child);
            source = source + "\n" + child.getSource();
        }
    }
}
