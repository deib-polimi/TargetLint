package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a lambda.
 * It's characterized by a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLArgument} and a body represented by a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}
 */
public class TLLambda extends TLElement {

    private List<TLArgument> arguments = new ArrayList<>();
    private List<TLElement> body = new ArrayList<>();

    /**
     * TLLambda constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLLambda(Location location, String source, TLElement parent) {
        super(location, source, parent);
    }

    /**
     * Gets the arguments of the lambda.
     *
     * @return the arguments
     */
    public List<TLArgument> getArguments() {
        return arguments;
    }

    /**
     * Gets the body of the lambda.
     *
     * @return the body
     */
    public List<TLElement> getBody() {
        return body;
    }

    @Override
    public void addChild(TLElement child) {
        if (child instanceof TLArgument) {
            arguments.add((TLArgument) child);
        } else {
            body.add(child);
        }
    }
}
