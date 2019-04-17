package com.darkfoxdev.tesi.detectors.detectors;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.checks.operations.ReferenceMatchCheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLCall;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.tlast.TLQualified;
import com.darkfoxdev.tesi.targetlint.targets.filters.CallNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExtendsFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.InstanceOfCallerFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.MethodNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TextTargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;

import java.util.function.Function;

/**
 * The type Google api client detector.
 */
public class GoogleApiClientDetector extends TLDetector {

    /**
     * The constant ISSUE.
     */
    private static final TLIssue ISSUE = new TLIssue("GoogleApiClientLifecycle",
            "Incorrect GoogleApiClient lifecycle handling",
            "You should always disconnect a GoogleApiClient when you are done with it. "+
                    "For activities and fragments in most cases connection is done during onStart and "+
                    "disconnection during onStop().",
            "TargetLint",3,5);

    /**
     * Instantiates a new Google api client detector.
     *
     * @param tlBridge the tl bridge
     */
    public GoogleApiClientDetector(TLBridge tlBridge) {
        super(tlBridge);
    }

    @Override
    protected  TLIssue getIssue() {
        return ISSUE;
    }

    @Override
    protected void initializer() {

        TargetFilter f1_1 = new ExtendsFilter("android.app.Activity");
        TargetFilter f1_2 = new ExtendsFilter("android.support.v4.app.Fragment");
        TargetFilter f1 = f1_1.or(f1_2);
        TargetFilter f2 = new MethodNameFilter("onConnectionFailed");

        TargetFilter f5 = new MethodNameFilter("onStart");
        TargetFilter f6 = new MethodNameFilter("onStop");

        TargetFilter f3 = new InstanceOfCallerFilter("com.google.android.gms.common.api.GoogleApiClient");


        TargetFilter q1 = new CallNameFilter("connect", TextTargetFilter.Mode.EQUALS);
        TargetFilter q2 = new CallNameFilter("disconnect", TextTargetFilter.Mode.EQUALS);

        Target t1 = createTarget(TargetSearchLevel.FILE,TLCall.class,q1,f1,f2.not(),f3);
        Target t2 = createTarget(TargetSearchLevel.FILE,TLCall.class,q2,f1,f2.not(),f3);

        Target t5 = createTarget(TargetSearchLevel.FILE,TLCall.class,q1,f1.and(f5.not()),(f1),f2.not(),f3);
        Target t6 = createTarget(TargetSearchLevel.FILE,TLCall.class,q2,f1.and(f6.not()),(f1),f2.not(),f3);


        createCheck(t5,"Connection and disconnection should be handled respectively in onStart and onStop methods");
        createCheck(t6,"Connection and disconnection should be handled respectively in onStart and onStop methods");


        Function<Match,String> operation = (Match m) -> {

            if (m.getElement().getParent() instanceof TLQualified) {
              return  ((TLQualified)m.getElement().getParent()).getReceiver().getSource();

            } return "";
        };
        CheckOperation co1 = new ReferenceMatchCheckOperation<>(t2,operation);
        createCheck(t1,"No disconnect",co1.not());

    }
}
