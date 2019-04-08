package com.darkfoxdev.tesi.detectors.detectors;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.checks.operations.ReferenceMatchCheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLCall;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.tlast.TLQualified;
import com.darkfoxdev.tesi.targetlint.targets.filters.CallNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExtendsFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.MethodNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;

import java.util.function.Function;

public class ActivityLeakDetector extends TLDetector {

    private static final TLIssue ISSUE = new TLIssue("ActivityLeakDetector",
            "Possible memory leak due to retained activity",
            "Holding a reference to an activity could result in memory leaks. " +
                    "For example, once there is a static reference, the Activity will not be garbage collected. " +
                    "This problem could also happen with listeners and handlers. " +
                    "Don't forget to remove reference during onDestroy",
            "TargetLint",3,5
    );

    /**
     * Instantiates a new Tl detector.
     *
     * @param tlBridge the tl bridge
     */
    public ActivityLeakDetector(TLBridge tlBridge) {
        super(tlBridge);
    }

    @Override
    protected TLIssue getIssue() {
        return ISSUE;
    }

    @Override
    protected void initializer() {

        TargetFilter f1 = new ExtendsFilter("android.app.Activity");
        TargetFilter f2 = new CallNameFilter("addListener");

        TargetFilter f3 = new MethodNameFilter("onDestroy");
        TargetFilter f4 = new CallNameFilter("removeListener");

        TargetFilter f5 = new TargetFilter(TargetFilter.FilterType.INSTRUCTION) {
            @Override
            protected boolean calculate(TLElement element) {
                if (element instanceof TLCall){
                    TLCall call = (TLCall) element;
                    return call.getArguments().stream().anyMatch(e -> e.getSource().equals("this"));
                }
                return false;
            }
        };

        Target t1 = createTarget(TargetSearchLevel.FILE, TLCall.class, f1, f2, f5);

        Target t2 = createTarget(TargetSearchLevel.FILE, TLCall.class, f1, f3, f4, f5);

        Function<Match,String> operation = (Match m) -> {

            if (m.getElement().getParent() instanceof TLQualified) {
                return  ((TLQualified)m.getElement().getParent()).getReceiver().getSource();

            } return "";
        };

        CheckOperation co1 = new ReferenceMatchCheckOperation<>(t2, operation);
        createCheck(t1, "Methods named addListener usually save a reference of the Activity. " +
                "In order to prevent memory leaks, it's advised to call removeListener in onDestroy method.", co1.not());

    }
}
