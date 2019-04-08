package com.darkfoxdev.tesi.targetlint.targets;

import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.TLDetector;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Target.
 * This type defines the search target on which the detector will work.
 * A target is defined by a {@link com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel search level}, an element type (see <a href="{@docRoot}/com/darkfoxdev/tesi/targetlint/tlast/package-summary.html">tlast package</a>) and a list of <a href="{@docRoot}/com/darkfoxdev/tesi/targetlint/targets/filters/package-summary.html">filters</a>.
 * Those allows to get only relevant elements as {@link com.darkfoxdev.tesi.targetlint.Match matches}.
 */
public class Target {

    private Class<? extends TLElement> targetType;

    private List<TargetFilter> filters = new ArrayList<>();

    private List<TLElement> possibleMatches = new ArrayList<>();

    private Map<TLElement, List<Match>> matches = new HashMap<>();

    private TargetSearchLevel level;

    /**
     * Instantiates a new Target and adds it to the target list of the {@link com.darkfoxdev.tesi.targetlint.TLDetector detector}.
     *
     * @param tlDetector the detector
     * @param targetType the TLElement subclass targeted by the target (see <a href="{@docRoot}/com/darkfoxdev/tesi/targetlint/tlast/package-summary.html">tlast package</a>)
     * @param level      the search level (see {@link com.darkfoxdev.tesi.targetlint.targets.TargetSearchLevel})
     * @param filters    the <a href="{@docRoot}/com/darkfoxdev/tesi/targetlint/targets/filters/package-summary.html">filters</a> that define the target
     */
    public Target (TLDetector tlDetector, Class<? extends TLElement> targetType, TargetSearchLevel level, TargetFilter... filters) {
        this.level = level;
        this.targetType = targetType;
        this.filters.addAll(Arrays.asList(filters));
        tlDetector.getTargets().add(this);
    }

    /**
     * Gets filters.
     *
     * @return the filters
     */
    public List<TargetFilter> getFilters() {
        return filters;
    }

    /**
     * Gets matches for a specific reference (eg. all matches of a specific method, class, ecc.).
     *
     * @param forReference the element to which the matches refer
     * @return the matches
     */
    public List<Match> getMatches(TLElement forReference) {
        return matches.getOrDefault(forReference, new ArrayList<>());
    }

    /**
     * Gets all the matches of the target.
     *
     * @return the matches
     */
    public Map<TLElement, List<Match>> getMatches() {
        return matches;
    }

    /**
     * Gets the search level.
     *
     * @return the search level
     */
    public TargetSearchLevel getLevel() {
        return level;
    }

    /**
     * Gets the targeted type.
     *
     * @return the targeted type
     */
    public Class<? extends TLElement> getTargetType() {
        return targetType;
    }

    /**
     * Gets filters of type FilterType.CLASS.
     *
     * @return the all the CLASS filters
     *
     * @see com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter.FilterType FilterType
     */
    public List<TargetFilter> getClassFilters() {
        return filters.stream().filter(x -> x.getFilterType() == TargetFilter.FilterType.CLASS).collect(Collectors.toList());
    }

    /**
     * Gets filters of type FilterType.METHOD.
     *
     * @return the all the METHOD filters
     *
     * @see com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter.FilterType FilterType
     */
    public List<TargetFilter> getMethodFilters() {
        return filters.stream().filter(x -> x.getFilterType() == TargetFilter.FilterType.METHOD).collect(Collectors.toList());
    }

    /**
     * Gets filters of type FilterType.INSTRUCTION.
     *
     * @return the all the INSTRUCTION filters
     *
     * @see com.darkfoxdev.tesi.targetlint.targets.filters.TargetFilter.FilterType FilterType
     */
    public List<TargetFilter> getInstructionFilters() {
        return filters.stream().filter(x -> x.getFilterType() == TargetFilter.FilterType.INSTRUCTION).collect(Collectors.toList());
    }

    /**
     * Checks, against filters, if the element m is a match.
     *
     * @param m the element to check
     */
    public void isMatch(TLElement m) {
        try {
            if (getClassFilters().stream().allMatch(tf -> tf.evaluate(m.getContainingClass()))) {
                if (getMethodFilters().stream().allMatch(tf -> tf.evaluate(m.getContainingMethod()))) {
                    if (getInstructionFilters().stream().allMatch(tf -> tf.evaluate(m))) {
                        addMatch(m);
                    }
                }
            }
        } catch (Exception e) {
            TLBridge.log(e.toString()); //TODO!!!
            TLBridge.log(e.getStackTrace());

        }
    }

    /**
     * Add a possible match.
     * A possible match is an element that is yet to be checked against filters.
     *
     * @param element the possible match element
     */
    public void addPossibleMatch(TLElement element) {
        if (!possibleMatches.contains(element)) {
            possibleMatches.add(element);
        }
    }

    /**
     * Process all possible matches and then clear the possible matches list.
     */
    public void createRelevantMatches() {
        for (TLElement m : possibleMatches) {
            isMatch(m);
        }
        possibleMatches.clear();
    }

    private void addMatch(TLElement m) {
        TLElement reference;
        if (level.equals(TargetSearchLevel.FILE)) {
            reference = m.getContainingFile();
        } else if (level.equals(TargetSearchLevel.CLASS)) {
            reference = m.getContainingClass();
        } else if (level.equals(TargetSearchLevel.METHOD)) {
            reference = m.getContainingMethod();
        } else {
            reference = TLElement.TLOBJECT; //TODO controllare se funziona
        }

        Match match = new Match(this, m, reference);

        if (!matches.containsKey(reference)) {
            matches.put(reference, new ArrayList<>(Arrays.asList(match)));
        } else {
            matches.get(reference).add(match);
        }


    }


}
