package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a class.
 * It's characterized by:
 * <ul>
 *     <li>a qualified name as a string</li>
 *     <li>a list of superclass as a list of strings</li>
 *     <li>a list of class variables as a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLDeclaration}</li>
 *     <li>a list of methods as a list of {@link com.darkfoxdev.tesi.targetlint.tlast.TLMethod}</li>
 *     <li>flags for modifiers (<code>static</code> and <code>final</code>)</li>
 *     <li>flags for defining interfaces and enumerations</li>
 * </ul>
 */
public class TLClass extends TLElement {

    private String qualifiedName;
    private ArrayList<String> superClass;

    private ArrayList<TLDeclaration> classVariables;
    private ArrayList<TLMethod>  methods;

    private boolean isStatic;
    private boolean isFinal;
    private boolean isInterface;
    private boolean isEnum;

    /**
     * TLClass constructor.
     *
     * @param location    the location
     * @param source      the source code
     * @param parent      the parent node
     * @param name        the qualified qualifiedName
     * @param superClass  the list of superclasses from parent class to Object
     * @param isStatic    <code>true</code> if the class is declared <code>static</code>
     * @param isFinal     <code>true</code> if the class is declared <code>final</code>
     * @param isInterface <code>true</code> if it's an <code>interface</code>
     * @param isEnum      <code>true</code> if it's an <code>enum</code>
     */
    public TLClass(Location location, String source, TLElement parent, String name,
                   ArrayList<String>  superClass,boolean isStatic, boolean isFinal, boolean isInterface, boolean isEnum) {

        super(location, source, parent);
        this.qualifiedName = name;
        this.superClass = superClass;
        this.classVariables = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.isEnum = isEnum;
        this.isFinal = isFinal;
        this.isInterface = isInterface;
        this.isStatic = isStatic;
    }

    /**
     * Gets the name of the class without qualifiers.
     *
     * @return the name of the class
     */
    public String getName() {
        String [] splitName = qualifiedName.split("\\.");
        if (splitName.length == 0) {
            return qualifiedName;
        }
        return splitName[splitName.length -1];
    }

    /**
     * Gets the fully qualified name of the class.
     *
     * @return the fully qualified name of the class
     */
    public String getQualifiedName() {
        if (this.qualifiedName != null) {
            return qualifiedName;
        } else {
            return "";
        }
    }

    /**
     * Returns if the class is <code>static</code> or not
     *
     * @return <code>true</code> if the class is declared <code>static</code>
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Returns if the class is declared <code>final</code> or not
     *
     * @return <code>true</code> if the class is declared <code>final</code>
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Returns <code>true</code> if it's an <code>interface</code>
     *
     * @return <code>true</code> if it's an <code>interface</code>
     */
    public boolean isInterface() {
        return isInterface;
    }

    /**
     * Returns <code>true</code> if it's an <code>enum</code>
     *
     * @return <code>true</code> if it's an <code>enum</code>
     */
    public boolean isEnum() {
        return isEnum;
    }

    /**
     * Gets the list of superclasses from parent class to Object.
     *
     * @return the super class
     */
    public ArrayList<String>  getSuperClass() {
        return superClass;
    }


    /**
     * Gets the list of class variables.
     *
     * @return the class variables
     */
    public ArrayList<TLDeclaration> getClassVariables() {
        return classVariables;
    }

    /**
     * Gets the list of methods.
     *
     * @return the methods
     */
    public ArrayList<TLMethod> getMethods() {
        return methods;
    }

    @Override
    public void addChild(TLElement child) {
        if (child instanceof TLMethod) {
            methods.add((TLMethod) child);
        } else if ((child instanceof TLDeclaration) ) {
            classVariables.add((TLDeclaration)child);
        }
    }
}
