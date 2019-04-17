package com.darkfoxdev.tesi.detectors.detectors;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.checks.operations.ReferenceMatchCheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLAssignment;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLField;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.targets.filters.AssignmentValueFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExtendsFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.MethodNameFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TypeFilter;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;

import java.util.List;
import java.util.function.Function;

public class RetainedViewDetector extends TLDetector {

    private static final TLIssue ISSUE =  new TLIssue("RetainedViewDetector",
            "Incorrect retained View variable handling",
            "Saving Views in Fragment can speed up access, but it can introduce memory leakage.\n" +
                    "\n" +
                    "A View keeps a reference to the Activity context, so when a View is retained it also retain a reference to that context.\n" +
                    "\n" +
                    "Activity recreation will create a new context and old contexts can't be garbage collected since the Fragment still has a reference to the old one.",
            "TargetLint",3,5);

    /**
     * Instantiates a new Tl detector.
     *
     * @param tlBridge the tl bridge
     */
    public RetainedViewDetector(TLBridge tlBridge) {
        super(tlBridge);
    }

    @Override
    protected TLIssue getIssue() {
        return ISSUE;
    }

    @Override
    protected void initializer() {
        TargetFilter f1_1 = new ExtendsFilter("android.support.v4.app.Fragment") ;
        TargetFilter f1_2 = new ExtendsFilter("androidx.fragment.app.Fragment") ;
        TargetFilter f1 = f1_1.or(f1_2);
        TargetFilter f2_1 = new TypeFilter("android.view.View");
        TargetFilter f2_2 = new TypeFilter("android.graphics.drawable.Drawable");
        TargetFilter f2_3 = new TypeFilter("android.widget.Adapter");
        TargetFilter f2 =  f2_1.or(f2_2).or(f2_3);
        TargetFilter f3 = new MethodNameFilter("onDestroy");
        TargetFilter q1 = new AssignmentValueFilter("null");

        TargetFilter f4 = new TargetFilter(TargetFilter.FilterType.INSTRUCTION) {
            @Override
            protected boolean calculate(TLElement element) {
                return element.getAnnotations().stream().anyMatch(a -> a.toLowerCase().contains("@bindview"));
            }
        };


        Target t1 = createTarget(TargetSearchLevel.FILE,TLField.class,f2,f1);
        Target t2 = createTarget(TargetSearchLevel.FILE,TLAssignment.class,q1,f1,f2,f3);

        Function<Match,String> operation = (Match m) -> {
            if (m.getElement() instanceof TLAssignment) {
                TLAssignment tlAssignment = (TLAssignment)m.getElement();
                return  tlAssignment.getVariable().getName();
            } return null;
        };

        CheckOperation co1 = new ReferenceMatchCheckOperation<>(t2,operation);

        String message = "View retaining can introduce memory leakage: " +
                "consider setting to null in onDestroy() method.";
      //  createCheck(t1,message,f4.not().convertToCheckOperation(),co1.not());

    }
}