package com.darkfoxdev.tesi.targetlint.tlast;

import java.util.List;

/**
 * Represents a type.
 * It's characterized by its qualified name (as a string) and its superclasses (as a list of strings).
 */
public class TLType {

    /**
     * The qualified name of the type.
     */
    String name;

    /**
     * The list of superclasses.
     */
    List<String> superTypes;

    /**
     * Instantiates a new Tl type.
     *
     * @param type       the type
     * @param superTypes the super types
     */
    public TLType(String type, List<String> superTypes) {
        this.name = type;
        this.superTypes = superTypes;
    }

    /**
     * Gets the qualified name of the type.
     *
     * @return the qualified name
     */
    public String getQualifiedName() {
        return name;
    }

    /**
     * Gets the list of superclasses.
     *
     * @return the list of superclasses
     */
    public List<String> getSuperTypes() {
        return superTypes;
    }
}
