package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a call.
 * It's characterized by its name and arguments (a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}).
 */
public class TLCall extends TLExpression {

    private ArrayList<TLElement> arguments;
    private String name;

    /**
     * Gets arguments.
     *
     * @return the arguments
     */
    public ArrayList<TLElement> getArguments() {
        return arguments;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * Instantiates a new TLCall.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     * @param type     the type
     * @param name     the name
     */
    public TLCall(Location location, String source, TLElement parent, TLType type, String name) {
        super(location, source, parent,type);
        this.arguments = new ArrayList<>();
        this.name = name;
    }

    @Override
    public void addChild(TLElement child) {
        if (!arguments.contains(child)) {
            arguments.add(child);
        }
    }
}
