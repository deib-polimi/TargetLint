package com.darkfoxdev.tesi.targetlint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.darkfoxdev.tesi.targetlint.uast.UastDetector;

import java.util.Arrays;
import java.util.List;

/**
 * The Android Lint custom registry.
 */
public class CustomRegistry extends IssueRegistry {

    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(UastDetector.ISSUE);
    }
}
