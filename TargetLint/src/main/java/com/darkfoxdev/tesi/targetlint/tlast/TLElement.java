package com.darkfoxdev.tesi.targetlint.tlast;


import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * TLElement is the abstract class extended by all the other Target Lint AST nodes.
 */
public abstract class TLElement  {

    /**
     * @y.exclude
     */
    public static TLElement TLOBJECT = new TLElement() { //TODO spostare
        @Override
        public void addChild(TLElement child) {

        }
    } ;

    /**
     * The Location.
     */
    protected Location location;
    /**
     * The Source code.
     */
    protected String source;
    /**
     * The Parent node.
     */
    protected TLElement parent;
    private List<String> annotations = new ArrayList<>();


    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets source code.
     *
     * @return the source code
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets parent node.
     *
     * @return the parent node
     */
    public TLElement getParent() {
        return parent;
    }

    /**
     * Gets annotations.
     *
     * @return the annotations
     */
    public List<String> getAnnotations() {
        return annotations;
    }

    /**
     * Gets location as string.
     *
     * @return the location string
     */
    public String getLocationString(boolean withFile) {
        String locationString = " Line: " + location.getStart().getLine() + " Column: " + location.getStart().getColumn();
        if (withFile) {
            return "File: " + location.getFile().getName() + locationString;
        } else {
            return locationString;
        }
    }

    /**
     * Constructor.
     *
     * @param location the location
     * @param source   the source code
     * @param parent   the parent node
     */
    public TLElement(Location location, String source, TLElement parent) {
        this.location = location;
        this.source = source;
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
    }

    /**
     * @y.exclude
     */
    protected TLElement() { }

    /**
     * Checks if the element has an ancestor of specified type in the tree .
     *
     * @param ancestorType the ancestor type
     * @return <code>true</code>: if the ancestor is found; <code>false</code>: otherwise
     */
    public boolean hasAncestorOfType(Class<? extends TLElement> ancestorType) {
        return getAncestorsOfType(ancestorType).size() > 0;
    /*    if (ancestorType.isInstance(parent)) {
            return true;
        } else if (parent == null) {
            return false;
        } else {
           return parent.hasAncestorOfType(ancestorType);
        }
        */
    }

    /**
     * Adds a TLElement as child node.
     *
     * @param child the child node
     */
    abstract public void addChild (TLElement child);

    /**
     * Returns the list of ancestors in the tree with a specified type.
     *
     * @param ancestorType the ancestor type
     * @return the ancestors with the specified type
     */
    public List<TLElement> getAncestorsOfType(Class<? extends TLElement> ancestorType) {
       List<TLElement> result = new ArrayList<>();
       if (parent != null) {
           if (ancestorType.isInstance(parent)) {
               result.add(parent);
           }
           result.addAll(parent.getAncestorsOfType(ancestorType));
       }
       return result;
   }

    /**
     * Gets the containing {@link com.darkfoxdev.tesi.targetlint.tlast.TLFile}.
     *
     * @return the containing file
     */
    public TLFile getContainingFile() {
        if (this instanceof TLFile) {
            return (TLFile) this;
        } else if (parent != null) {
            return parent.getContainingFile();
        } else {
            return null;
        }
    }

    /**
     * Gets the containing {@link com.darkfoxdev.tesi.targetlint.tlast.TLClass}.
     *
     * @return the containing class
     */
    public TLClass getContainingClass(){
        if (this instanceof TLClass){
            return (TLClass) this;
        }
        if (parent == null){
            return null;
        }
        return parent.getContainingClass();
    }

    /**
     * Gets the containing {@link com.darkfoxdev.tesi.targetlint.tlast.TLMethod}.
     *
     * @return the containing method
     */
    public TLMethod getContainingMethod(){
        if (this instanceof TLMethod){
            return (TLMethod) this;
        }
        if (parent == null){
            return null;
        }
        return parent.getContainingMethod();
    }
}
