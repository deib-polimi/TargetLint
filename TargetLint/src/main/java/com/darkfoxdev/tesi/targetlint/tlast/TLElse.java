package com.darkfoxdev.tesi.targetlint.tlast;


import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;
import java.util.List;

public class TLElse extends TLCondStmt {

   private List<TLElement> body;


    public TLElse(Location location, String source, TLElement parent) {
        super(location, source, parent);
        this.body = new ArrayList<>();
    }

    public List<TLElement> getBody() {
        return body;
    }

    @Override
    public void addChild(TLElement child) {
        if (!body.contains(child)) {
            body.add(child);
        }
    }
}
