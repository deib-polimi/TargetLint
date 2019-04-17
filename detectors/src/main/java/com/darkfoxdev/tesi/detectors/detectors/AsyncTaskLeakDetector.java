package com.darkfoxdev.tesi.detectors.detectors;
import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.checks.operations.ReferenceMatchCheckOperation;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;
import com.darkfoxdev.tesi.targetlint.targets.filters.ExtendsFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.InstructionContainedInBlockFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.filters.TypeFilter;
import com.darkfoxdev.tesi.targetlint.tlast.TLAssignment;
import com.darkfoxdev.tesi.targetlint.tlast.TLClass;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLField;
import com.darkfoxdev.tesi.targetlint.tlast.TLVariable;
import java.util.function.Function;

public class AsyncTaskLeakDetector extends TLDetector {

    public AsyncTaskLeakDetector(TLBridge tlBridge) {
        super(tlBridge);
    }

    @Override
    protected TLIssue getIssue() {
        return new TLIssue("AsyncTaskLeakDetector","Incorrect AsyncTask declaration",
                "Non static inner classes always keep a reference to the outer class." +
                        "You should make the class static in order to avoid memory leaks," +
                        "if you need to update Views use WeakReference class or declare the whole class in a seprate file" +
                        " (https://developer.android.com/reference/java/lang/ref/WeakReference","TargetLint",3,5);
    }

    @Override
    protected void initializer() {
        TargetFilter tf1 = new InstructionContainedInBlockFilter(TLClass.class);
        TargetFilter tf2 = new ExtendsFilter("android.os.AsyncTask");/*new TargetFilter(TargetFilter.FilterType.CLASS) {
            @Override
            protected boolean calculate(TLElement element) {
               if ((element instanceof TLClass)) {
                  return ((TLClass)element).getSuperClass().contains("android.os.AsyncTask");
               } return false;
            }
        };*/
        Target t2 = createTarget(TargetSearchLevel.FILE,TLClass.class,tf1,tf2);
        TargetFilter tf3 = new TypeFilter("android.view.View");
        Target t1 = createTarget(TargetSearchLevel.FILE, TLVariable.class,tf3);
        Function<Match,TLClass> operation = new Function<Match, TLClass>() {
            @Override
            public TLClass apply(Match match) {
                TLElement element = match.getElement();
                if (element instanceof TLClass) {
                    return (TLClass) element;
                } else {
                    return element.getContainingClass();
                }
            }
        };
        Target t3 = createTarget(TargetSearchLevel.FILE, TLField.class,tf3, tf2.not());
        Function<Match,String> operation2 = (Match m) -> {
            if (m.getElement() instanceof TLAssignment) {
                TLAssignment tlAssignment = (TLAssignment)m.getElement();
                return  tlAssignment.getVariable().getName();
            } else if (m.getElement() instanceof TLVariable) {
                TLVariable tlVariable = (TLVariable) m.getElement();
                return tlVariable.getName();
            } return null;
        };
        CheckOperation co1 = new ReferenceMatchCheckOperation<TLClass>(t2,operation);
        CheckOperation co2 = new ReferenceMatchCheckOperation<String>(t3,operation2);
        String message =
                "Consider making the class static and using WeakReference if you need to update the view " ;
        createCheck(t1,message,co1,co2);

    }
}
