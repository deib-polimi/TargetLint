package com.darkfoxdev.tesi.targetlint.targets.filters;

import com.darkfoxdev.tesi.targetlint.tlast.TLCatch;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLTry;

import java.util.List;
import java.util.stream.Collectors;

public class ExceptionHandledFilter extends TargetFilter {

    private String exception;

    /**
     * Instantiates a new Target filter.
     *
     * @param exception the exception to check
     */
    public ExceptionHandledFilter(String exception) {
        super(FilterType.INSTRUCTION);
        this.exception = exception;
    }

    protected boolean calculate(TLElement element) {
        List<TLElement> tryBlocks =  element.getAncestorsOfType(TLTry.class);
        if (tryBlocks.size()>0) {
            for (TLCatch catchClause : ((TLTry)tryBlocks.get(0)).getCatches()) {
                List<String> catches = catchClause.getExceptionType().stream().filter(x -> x.contains(exception)
                        || (x.startsWith("java.lang.Exception") || x.startsWith("java.lang.Throwable"))).collect(Collectors.toList());
                if (catches.size() == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}


