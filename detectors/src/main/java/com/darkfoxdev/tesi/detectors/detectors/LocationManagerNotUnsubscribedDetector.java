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
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;

import java.util.function.Function;

public class LocationManagerNotUnsubscribedDetector extends TLDetector {


    private static final TLIssue ISSUE =  new TLIssue("LocationManagerNotUnsubscribedDetector",
            "Listener not unsubscribed",
            "Not unsubscribed listeners can introduce memory leakage.\n",
            "TargetLint",3,5);

    /**
     * Instantiates a new Tl detector.
     *
     * @param tlBridge the tl bridge
     */
    public LocationManagerNotUnsubscribedDetector(TLBridge tlBridge) {
        super(tlBridge);
    }


    @Override
    protected TLIssue getIssue() {
        return ISSUE;
    }

    @Override
    protected void initializer() {
        TargetFilter f1_1 = new ExtendsFilter("android.app.Activity");
        TargetFilter f1_2 = new ExtendsFilter("android.support.v4.app.Fragment");
        TargetFilter f1 = f1_1.or(f1_2);
        TargetFilter f2 = new InstanceOfCallerFilter("android.location.LocationManager");
        TargetFilter f3 = new MethodNameFilter("onDestroy");
        TargetFilter q1 = new CallNameFilter("requestLocationUpdates");
        TargetFilter q2 = new CallNameFilter("removeUpdates");

        Target t1 =  createTarget(TargetSearchLevel.FILE,TLCall.class, q1,f1,f2);
        Target t2 = createTarget(TargetSearchLevel.FILE, TLCall.class,q2,f1,f2,f3);

        Function<Match,String> operation = (Match m) -> {
            if (m.getElement().getParent() instanceof TLQualified) {
                return  ((TLQualified)m.getElement().getParent()).getReceiver().getSource();
            } return "";
        };

        CheckOperation co1 = new ReferenceMatchCheckOperation<>(t2,operation);
        createCheck(t1,"Not unsubscribed listeners can introduce memory leakage: consider adding removeUpdate() in onDestroy() method.",co1.not());
    }



}
