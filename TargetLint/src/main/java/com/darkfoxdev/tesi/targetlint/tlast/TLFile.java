package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents the file.
 * It's characterized by:
 * <ul>
 *     <li>the package name as a String</li>
 *     <li>the list of contained class as a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLClass}</li>
 *     <li>the list of imports as a list of strings</li>
 * </ul>
 */
public class TLFile extends TLElement {

    private List<TLClass> classes = new ArrayList<>();
    private List<String> imports;
    private String packageName;

    /**
     * TLFile constructor.
     *
     * @param location    the location
     * @param source      the source code
     * @param parent      the parent node
     * @param imports     the list of <code>import</code>
     * @param packageName the package name
     */
    public TLFile(Location location, String source, TLElement parent, List<String> imports, String packageName) {
        super(location, source, parent);
        this.imports = imports;
        this.packageName = packageName;

    }

    @Override
    public void addChild(TLElement child) {

        if (child instanceof TLClass) {
            classes.add((TLClass)child);
        }
    }
}
