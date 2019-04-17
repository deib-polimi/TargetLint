package com.darkfoxdev.tesi.detectors.detectors;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLCall;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.targets.filters.CallNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExtendsFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.InstanceOfCallerFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.MethodNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;

import java.util.ArrayList;
import java.util.List;

public class InflaterDetector extends TLDetector {

    /**
     * The constant ISSUE.
     */
    private static final TLIssue ISSUE = new TLIssue("InflaterLifecycle",
            "Incorrect Inflater lifecycle handling",
            "The root ViewGroup container that will hold your Fragment in your Activity" +
                    " is the ViewGroup parameter given to you in onCreateView() in your Fragment. " +
                    "It's also the ViewGroup you pass into LayoutInflater.inflate(). " +
                    "The FragmentManager will handle attaching your Fragment's View to this ViewGroup, however." +
                    " You do not want to attach it twice. Set attachToRoot to false.",
            "TargetLint",3,5
    );

    /**
     * Instantiates a new Inflater detector.
     *
     * @param tlBridge the tl bridge
     */
    public InflaterDetector(TLBridge tlBridge) {
        super(tlBridge);
    }

    protected TLIssue getIssue() {
        return ISSUE;
    }


    protected void initializer() {

        TargetFilter f1 = new ExtendsFilter("android.support.v4.app.Fragment");
        TargetFilter f2 = new MethodNameFilter("onCreateView");
        TargetFilter f3 = new CallNameFilter("inflate");
        TargetFilter f4 = new InstanceOfCallerFilter("android.view.LayoutInflater");

        Target t = createTarget(TargetSearchLevel.CLASS, TLCall.class, f1, f2, f3, f4);

        CheckOperation co1 = new CheckOperation() {
            @Override
            public List<Match> check(List<Match> matches) {
                List<Match> result = new ArrayList<>();
                for (Match m: matches){
                    if (m.getElement() instanceof TLCall){
                        TLCall call = (TLCall) m.getElement();
                        if (call.getArguments().size()<3){
                            result.add(m);
                        } else if (call.getArguments().get(call.getArguments().size()-1).getSource().equals("true")){
                            result.add(m);
                        }
                    }
                }
                return result;
            }
        };

        createCheck(t, "When inflating and returning a Fragment’s View in onCreateView()," +
                " be sure to pass in false for attachToRoot. " +
                "If you pass in true, you will get an IllegalStateException " +
                "because the specified child already has a parent.", co1);


    }

}
