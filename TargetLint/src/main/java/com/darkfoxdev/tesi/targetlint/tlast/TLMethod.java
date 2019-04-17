package com.darkfoxdev.tesi.targetlint.tlast;

import com.android.tools.lint.detector.api.Location;

import java.util.ArrayList;

/**
 * {@link com.darkfoxdev.tesi.targetlint.tlast.TLElement} that represents a method.
 * It's characterized by:
 * <ul>
 *     <li>a name as a String</li>
 *     <li>a access level modifier as a String</li>
 *     <li>a return type as {@link com.darkfoxdev.tesi.targetlint.tlast.TLType}</li>
 *     <li>a flag that specifies if the method overrides a method of the superclass</li>
 *     <li>a flag that specifies if the method is a constructor</li>
 *     <li>a flag that specifies if the method is <code>static</code></li>
 * </ul>
 */
public class TLMethod extends TLElement {

    private String name;
    private TLType returnType;
    private String accessModifier;

    private boolean isOverride;
    private boolean isConstructor;
    private boolean isStatic;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the return type as String.
     *
     * @return the return type
     */
    public String getReturnTypeAsString() {
        return returnType.getQualifiedName();
    }

    /**
     * Gets the return type.
     *
     * @return the return type
     */
    public TLType getReturnType() {return returnType;}

    /**
     * Gets the access level modifier of the method as a String.
     *
     * @return method accessModifier
     */
    public String getAccessModifier() {
        return accessModifier;
    }

    /**
     * Gets the list of arguments of the method.
     *
     * @return the arguments
     */
    public ArrayList<TLArgument> getArguments() {
        return arguments;
    }

    /**
     * Gets the list of instructionsof the method.
     *
     * @return the instructions
     */
    public ArrayList<TLElement> getInstructions() {
        return instructions;
    }

    private ArrayList<TLArgument> arguments;
    private ArrayList<TLElement> instructions;

    /**
     * TLMethod constructor.
     *
     * @param location      the location
     * @param source        the source code
     * @param parent        the parent node
     * @param name          the name
     * @param returnType    the return type
     * @param isConstructor <code>true</code> if the method is a constructor
     * @param isOverride    <code>true</code> if the method overrides a method of the superclass
     * @param isStatic      <code>true</code> if the method is <code>static</code>
     */
    public TLMethod(Location location, String source, TLElement parent, String name,
                    TLType returnType,boolean isConstructor,boolean isOverride, boolean isStatic, String modifier) {
        super(location, source, parent);
        this.name = name;
        this.returnType = returnType;
        this.arguments = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.isConstructor = isConstructor;
        this.isOverride = isOverride;
        this.isStatic = isStatic;
        this.accessModifier = modifier;
    }

    /**
     * Returns the value of the flag that specifies if the method overrides a method of the superclass.
     *
     * @return <code>true</code> if the method overrides a method of the superclass
     */
    public boolean isOverride() {
        return isOverride;
    }

    /**
     * Returns the value of the flag that specifies if the method is a constructor.
     *
     * @return <code>true</code> if the method is a constructor
     */
    public boolean isConstructor() {
        return isConstructor;
    }

    /**
     * Returns the value of the flag that specifies if the method is <code>static</code>.
     *
     * @return <code>true</code> if the method is <code>static</code>
     */
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public void addChild(TLElement child) {
        if (child instanceof TLArgument) {
            arguments.add((TLArgument)child);
        }else if (!instructions.contains(child)) {
            instructions.add(child);
        }
    }
}
