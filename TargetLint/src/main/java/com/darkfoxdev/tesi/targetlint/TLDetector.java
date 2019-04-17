package com.darkfoxdev.tesi.targetlint;

import com.darkfoxdev.tesi.targetlint.checks.Check;
import com.darkfoxdev.tesi.targetlint.checks.operations.CheckOperation;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;
import com.darkfoxdev.tesi.targetlint.targets.Target;
import com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Tl detector.
 */
public abstract class TLDetector {

    private TLBridge tlBridge;

    private ArrayList<Target> targets = new ArrayList<>();
    private List<Check> checks = new ArrayList<>();

    /**
     * Instantiates a new Tl detector.
     *
     * @param tlBridge the tl bridge
     */
    public TLDetector(TLBridge tlBridge){
        this.tlBridge = tlBridge;
        initializer();
    }

    /**
     * Gets issue.
     *
     * @return the issue
     */
    protected abstract TLIssue getIssue ();

    /**
     * Initializer.
     */
    protected abstract void initializer();

    private List<Target> getClassTarget(){return targets.stream().filter(t -> t.getLevel().equals(TargetSearchLevel.CLASS)).collect(Collectors.toList());}
    private List<Target> getMethodTarget(){return targets.stream().filter(t -> t.getLevel().equals(TargetSearchLevel.METHOD)).collect(Collectors.toList());}
    private List<Target> getFileTarget(){return targets.stream().filter(t -> t.getLevel().equals(TargetSearchLevel.FILE)).collect(Collectors.toList());}
    private List<Target> getProjectTarget(){return targets.stream().filter(t -> t.getLevel().equals(TargetSearchLevel.PROJECT)).collect(Collectors.toList());}

    private void processChecks (List<Check> checks) {
        for (Check c : checks) {
            try {
                c.process().forEach(m -> report(m,c.getMessage()));
            } catch (Exception e) {
                TLBridge.log(e.getStackTrace());
            }
        }
    }

    /**
     * Is match.
     *
     * @param element the element
     */
    public void isPossibleMatch(TLElement element) {
        targets.stream().filter(t -> t.getTargetType().isInstance(element)).forEach(t -> t.addPossibleMatch(element));
    }

    private void report (Match m, String message) {
        tlBridge.report(m,getIssue(),message);
    }

    /**
     * Before check project.
     */
    public void beforeCheckProject() {

    }

    /**
     * After check project.
     */
    public void afterCheckProject() {
        processChecks(checks);
        targets.forEach(t -> t.getMatches().clear());
    }

    /**
     * Before check file.
     */
    public void beforeCheckFile() {

    }

    /**
     * After check file.
     */
    public void afterCheckFile() {
        targets.forEach(t -> t.createRelevantMatches());
    }

    /**
     * Create target target.
     *
     * @param level   the level
     * @param type    the type
     * @param filters the filters
     * @return the target
     */
    public Target createTarget(TargetSearchLevel level,Class<? extends TLElement> type,TargetFilter... filters) {
        return  new Target(this,type,level,filters);
    }

    /**
     * Create target target.
     *
     * @param level   the level
     * @param filters the filters
     * @return the target
     */
    public Target createTarget(TargetSearchLevel level,TargetFilter... filters) {
        return  new Target(this,TLElement.class,level,filters);
    }

    /**
     * Create check check.
     *
     * @param target     the target
     * @param message    the message
     * @param operations the operations
     * @return the check
     */
    public Check createCheck(Target target, String message, CheckOperation... operations) {
      return Check.createCheck(this,target,message,operations);
    }

    /**
     * Gets checks.
     *
     * @return the checks
     */
    public List<Check> getChecks() {
        return checks;
    }

    /**
     * Gets targets.
     *
     * @return the targets
     */
    public List<Target> getTargets() {
        return targets;
    }
}
