package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a code block.
 * It's characterized by a body (a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement}).
 */
public class TLBlock extends TLElement {

    private ArrayList<TLElement> body;

    /**
     * Instantiates a new TLBlock.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLBlock(Location location, String source, TLElement parent) {
        super(location, source, parent);
        this.body = new ArrayList<>();
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    public ArrayList<TLElement> getBody() {
        return body;
    }

    @Override
    public void addChild(TLElement child) {
        if (!body.contains(child)) {
            body.add(child);
        }
    }
}
