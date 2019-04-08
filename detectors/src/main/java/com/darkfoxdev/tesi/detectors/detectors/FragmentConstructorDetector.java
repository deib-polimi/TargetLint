package com.darkfoxdev.tesi.detectors.detectors;

import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExtendsFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.MethodIsConstructorFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.MethodModifierFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.NumberOfParametersMethodFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;

/**
 * The type Fragment constructor detector.
 */
public class FragmentConstructorDetector extends TLDetector {

    /**
     * Instantiates a new Fragment constructor detector.
     *
     * @param tlBridge the tl bridge
     */
    public FragmentConstructorDetector(TLBridge tlBridge) {
        super(tlBridge);
    }


    /**
     * The constant ISSUE.
     */
    private static final TLIssue ISSUE =  new TLIssue("FragmentConstructorDetector",
            "Incorrect Fragment constructor declaration",
            "You shouldn't really be overriding the Fragment constructor." + "/n" +
                    "You can override newInstance() static method and pass any parameters via arguments" + "/n" +
                    "CheckOperation https://stackoverflow.com/a/10450535 for an explanation.",
            "TargetLint",3,5
            );

    @Override
    protected TLIssue getIssue() {
        return ISSUE;
    }

    @Override
    protected void initializer() {
        TargetFilter f1 = new ExtendsFilter("android.app.Fragment");
        TargetFilter f2 = new NumberOfParametersMethodFilter(0);
        TargetFilter f3 = new MethodModifierFilter("public");
        TargetFilter q1 = new MethodIsConstructorFilter();
        Target t1 = createTarget(TargetSearchLevel.METHOD,TLMethod.class,q1,f1,f2.not());
        Target t2 = createTarget(TargetSearchLevel.METHOD,TLMethod.class,q1,f1,f2,f3.not());
        createCheck(t1,"Avoid non-default constructors in fragments: "
                + "use a default constructor plus "
                + "`Fragment#setArguments(Bundle)` instead");
        createCheck(t2,"The Default Constructor must be public");
    }





}
