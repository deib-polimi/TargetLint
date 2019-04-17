package com.darkfoxdev.tesi.detectors.detectors;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.checks.operations.ReferenceMatchCheckOperation;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExceptionHandledFilter;
import com.darkfoxdev.tesi.targetlint.tlast.TLCall;
import com.darkfoxdev.tesi.targetlint.tlast.TLCatch;
import com.darkfoxdev.tesi.targetlint.tlast.TLTry;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.checks.operations.GroupAndExecuteCheckOperation;
import com.darkfoxdev.tesi.targetlint.checks.operations.MatchNumberCheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.tlast.TLLoop;
import com.darkfoxdev.tesi.targetlint.targets.filters.CallNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExtendsFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.InstanceOfCallerFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.InstructionContainedInBlockFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.MethodNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TextTargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.TLBridge;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by Pietro on 23/03/18.
 */
public final class BroadcastReceiverDetector extends TLDetector {

    /**
     * The constant ISSUE.
     */
    private static final TLIssue ISSUE = new TLIssue("BroadcastReceiverLifecycle",
            "Incorrect BroadcastReceiver lifecycle handling",
            "Calls to register and unregister of BroadcastReceiver components should be done carefully, "+
                    "i.e. you should avoid unregistering twice (otherwise you'll receive an exception), always "+
                    "unregister it to avoid leaks, etc.",
            "TargetLint",3,5
           );

    /**
     * Instantiates a new Broadcast receiver detector.
     *
     * @param tlBridge the tl bridge
     */
    public BroadcastReceiverDetector(TLBridge tlBridge) {
        super(tlBridge);
    }

    protected TLIssue getIssue() {
        return ISSUE;
    }

    protected void initializer() {
        TargetFilter f1 = new ExtendsFilter("android.app.Activity");
        TargetFilter f2 = new MethodNameFilter("onSaveInstanceState");
        TargetFilter f3 = new InstanceOfCallerFilter("android.support.v4.content.LocalBroadcastManager");
        TargetFilter f4 = new InstructionContainedInBlockFilter(TLLoop.class);
        TargetFilter f5 = new MethodNameFilter("onResume");
        TargetFilter f6 = new MethodNameFilter("onPause");
        TargetFilter f7 = new MethodNameFilter("onStart");
        TargetFilter f8 = new MethodNameFilter("onStop");
        TargetFilter f9 = new ExceptionHandledFilter("java.lang.IllegalArgumentException").not();
        TargetFilter q1 = new CallNameFilter("unregisterReceiver", TextTargetFilter.Mode.EQUALS);
        TargetFilter q2 = new CallNameFilter("registerReceiver", TextTargetFilter.Mode.EQUALS);
        TargetFilter q3 = q1.or(q2);

        Target t1 = createTarget(TargetSearchLevel.FILE,TLCall.class,q1,f1.or(f3));
        Target t2 = createTarget(TargetSearchLevel.FILE,TLCall.class,q2,f1.or(f3));
        Target t3 = createTarget(TargetSearchLevel.FILE,TLCall.class,q3,f1.or(f3),f4,f9);

        createCheck(t1,"No in onSaveInstanceState",f2.convertToCheckOperation());
        createCheck(t3,"Not safe in a loop");

        Function<Match,String> operation = (Match match) -> {
                if (match.getElement() instanceof TLCall) {
                    return ((TLCall)match.getElement()).getArguments().get(0).getSource();
                }
                return null;
        };

        CheckOperation co1 = new ReferenceMatchCheckOperation<>(t1,operation);
        createCheck(t2,"No unRegister",co1.not());

        Target t4 = createTarget(TargetSearchLevel.FILE,TLCall.class,q1,f1.or(f3),f6);
        CheckOperation co2 = new ReferenceMatchCheckOperation<>(t4,operation);
        createCheck(t2,"Registrations in onResume should be unregistered in onPause",co2.not(),f5.convertToCheckOperation());

        Target t5 =createTarget(TargetSearchLevel.FILE,TLCall.class,q1,f1.or(f3),f8);
        CheckOperation co3 = new ReferenceMatchCheckOperation<>(t5,operation);
        createCheck(t2,"Registrations in onStart should be unregistered in onStop",co3.not(),f7.convertToCheckOperation());

        CheckOperation co4 = new GroupAndExecuteCheckOperation<>(operation,MatchNumberCheckOperation.createGreaterThanCheckOperation(1));
        createCheck(t1,"Multiple unRegister should be in Try Catch block",co4,f9.convertToCheckOperation());
    }
}