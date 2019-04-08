package com.darkfoxdev.tesi.targetlint.uast;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.darkfoxdev.tesi.targetlint.LintDetectorInterface;
import com.darkfoxdev.tesi.targetlint.Match;
import com.darkfoxdev.tesi.targetlint.TLBridge;
import com.darkfoxdev.tesi.targetlint.tlast.TLArgument;
import com.darkfoxdev.tesi.targetlint.tlast.TLAssignment;
import com.darkfoxdev.tesi.targetlint.tlast.TLBlock;
import com.darkfoxdev.tesi.targetlint.tlast.TLCall;
import com.darkfoxdev.tesi.targetlint.tlast.TLCase;
import com.darkfoxdev.tesi.targetlint.tlast.TLCatch;
import com.darkfoxdev.tesi.targetlint.tlast.TLClass;
import com.darkfoxdev.tesi.targetlint.tlast.TLDeclaration;
import com.darkfoxdev.tesi.targetlint.tlast.TLDoWhile;
import com.darkfoxdev.tesi.targetlint.tlast.TLElement;
import com.darkfoxdev.tesi.targetlint.tlast.TLElse;
import com.darkfoxdev.tesi.targetlint.tlast.TLExpression;
import com.darkfoxdev.tesi.targetlint.tlast.TLField;
import com.darkfoxdev.tesi.targetlint.tlast.TLFile;
import com.darkfoxdev.tesi.targetlint.tlast.TLFor;
import com.darkfoxdev.tesi.targetlint.tlast.TLForEach;
import com.darkfoxdev.tesi.targetlint.tlast.TLIf;
import com.darkfoxdev.tesi.targetlint.TLIssue;
import com.darkfoxdev.tesi.targetlint.tlast.TLLambda;
import com.darkfoxdev.tesi.targetlint.tlast.TLMethod;
import com.darkfoxdev.tesi.targetlint.tlast.TLQualified;
import com.darkfoxdev.tesi.targetlint.tlast.TLSwitch;
import com.darkfoxdev.tesi.targetlint.tlast.TLTry;
import com.darkfoxdev.tesi.targetlint.tlast.TLType;
import com.darkfoxdev.tesi.targetlint.tlast.TLVariable;
import com.darkfoxdev.tesi.targetlint.tlast.TLWhile;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiType;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UBinaryExpression;
import org.jetbrains.uast.UBlockExpression;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UCatchClause;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UDeclaration;
import org.jetbrains.uast.UDeclarationsExpression;
import org.jetbrains.uast.UDoWhileExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UExpressionList;
import org.jetbrains.uast.UField;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.UForEachExpression;
import org.jetbrains.uast.UForExpression;
import org.jetbrains.uast.UIfExpression;
import org.jetbrains.uast.ULambdaExpression;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UParameter;
import org.jetbrains.uast.UPolyadicExpression;
import org.jetbrains.uast.UQualifiedReferenceExpression;
import org.jetbrains.uast.USimpleNameReferenceExpression;
import org.jetbrains.uast.USwitchClauseExpression;
import org.jetbrains.uast.USwitchClauseExpressionWithBody;
import org.jetbrains.uast.USwitchExpression;
import org.jetbrains.uast.UTryExpression;
import org.jetbrains.uast.UVariable;
import org.jetbrains.uast.UWhileExpression;
import org.jetbrains.uast.UastEmptyExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * The type Uast detector.
 */
public class UastDetector  extends Detector implements Detector.UastScanner, LintDetectorInterface {

    private Map<UElement,TLElement> parentMap = new HashMap<>();
    private List<UExpression> uElseList = new ArrayList<>();
    private TLBridge tlBridge ;
    private Map<TLIssue,Issue> issues = new HashMap<>();

    /**
     * The constant logger.
     */
    private static Logger logger = Logger.getInstance("LintChecks");

    private Context context;

    /**
     * The constant ISSUE.
     */
    public static final Issue ISSUE =  Issue.create("UASTdetector",
            "UASTDetector",
            "UastDetector",
            Category.PERFORMANCE,

            5,
            Severity.WARNING,
            new Implementation(UastDetector.class,
                    EnumSet.of(Scope.JAVA_FILE)));


    /**
     * Instantiates a new Uast detector.
     */
    public UastDetector() {
        super();
        this.tlBridge = new TLBridge(this);
    }

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Arrays.asList(UFile.class,UClass.class,UMethod.class,UField.class,
                UForExpression.class,UWhileExpression.class,UDoWhileExpression.class,
                UForEachExpression.class,UIfExpression.class,USwitchExpression.class,
                USwitchClauseExpression.class,UParameter.class,UCallExpression.class,
                UBinaryExpression.class,USimpleNameReferenceExpression.class,
                UDeclarationsExpression.class,UVariable.class, UBlockExpression.class,
                UTryExpression.class,UCatchClause.class,ULambdaExpression.class,UAnnotation.class,
                UQualifiedReferenceExpression.class,UElement.class
        );
    }

    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        return new UElementHandler() {

            @Override
            public void visitFile(UFile uFile) {
                Location location = context.getLocation(uFile);
                createTLFile(uFile,location);
            }

            @Override
            public void visitClass(UClass uClass) {
                Location location = context.getLocation((UElement) uClass);
                createTLClass(uClass, location);
            }

            @Override
            public void visitMethod(UMethod uMethod) {
                Location location = context.getLocation(uMethod);
                createTLMethod(uMethod, location);
            }

            @Override
            public void visitParameter(UParameter node) {
                Location location = context.getLocation((UElement) node);
                createTlArgument(node, location);
            }

            @Override
            public void visitField(UField node) {
                Location location = context.getLocation(node);
                createTLField(node, location);
            }

            @Override
            public void visitCallExpression(UCallExpression uCallExpression) {
                Location location = context.getLocation(uCallExpression);
                createTLCall(uCallExpression, location);
            }

            @Override
            public void visitBinaryExpression(UBinaryExpression node) {
                Location location = context.getLocation(node);
                if (node.getOperator().getText().equals("=")) {
                    createTLAssignment(node, location);
                } else {
                    createTLExpression(node,location);
                }
            }

            @Override
            public void visitSimpleNameReferenceExpression(USimpleNameReferenceExpression uSimpleNameReferenceExpression) {
                Location location = context.getLocation(uSimpleNameReferenceExpression);
                createTLVariable(uSimpleNameReferenceExpression, location);

            }

            @Override
            public void visitDeclarationsExpression(UDeclarationsExpression uDeclarationsExpression) {
                for (UDeclaration uDeclaration : uDeclarationsExpression.getDeclarations()) {
                    Location location = context.getLocation((UElement) uDeclaration);
                    createTLDeclaration(uDeclaration, location);
                }
            }

            @Override
            public void visitVariable(UVariable uVariable) {
                Location location = context.getLocation((UElement) uVariable);
                if (visit(UVariable.class,uVariable)) {
                    createTLVariable(uVariable, location);
                }
            }

            @Override
            public void visitForExpression(UForExpression uForExpression) {
                Location location = context.getLocation(uForExpression);
                createTLFor(uForExpression,location);
            }

            @Override
            public void visitForEachExpression(UForEachExpression uForEachExpression) {
                Location location = context.getLocation(uForEachExpression);
                location.setSecondary(context.getLocation((UElement) uForEachExpression.getVariable()));
                createTLForEachExpression(uForEachExpression,location);
            }

            @Override
            public void visitDoWhileExpression(UDoWhileExpression uDoWhileExpression) {
                Location location = context.getLocation(uDoWhileExpression);
                createTLDoWhile(uDoWhileExpression,location);
            }

            @Override
            public void visitBlockExpression(UBlockExpression uBlockExpression) {
                /*
                Location location = context.getLocation(uBlockExpression);
                if (visit(UBlockExpression.class,uBlockExpression)) {
                      createTLBlockExpression(uBlockExpression,location);
                }
                */
            }

            @Override
            public void visitWhileExpression(UWhileExpression uWhileExpression) {
                Location location = context.getLocation(uWhileExpression);
                createTLWhile(uWhileExpression,location);
            }

            @Override
            public void visitIfExpression(UIfExpression uIfExpression) {
                Location location = context.getLocation(uIfExpression);
                if ((uIfExpression.getElseExpression() != null) && !(uIfExpression.getElseExpression() instanceof UastEmptyExpression)) {
                    location.setSecondary(context.getLocation(uIfExpression.getElseExpression()));
                }

                createTLIf(uIfExpression,location);
            }

            @Override
            public void visitSwitchExpression(USwitchExpression uSwitchExpression) {
                Location location = context.getLocation(uSwitchExpression);
                createTLSwitch(uSwitchExpression,location);
            }

            @Override
            public void visitSwitchClauseExpression(USwitchClauseExpression uSwitchClauseExpression) {
                Location location = context.getLocation(uSwitchClauseExpression);
                createTLCase(uSwitchClauseExpression,location);
            }

            @Override
            public void visitTryExpression(UTryExpression uTryExpression) {
                Location location = context.getLocation(uTryExpression);
                if ((uTryExpression.getFinallyClause() != null)
                        && !(uTryExpression.getFinallyClause() instanceof UastEmptyExpression)) {
                    location.setSecondary(context.getLocation(uTryExpression.getFinallyClause()));
                }
                createTLTry(uTryExpression,location);
            }

            @Override
            public void visitCatchClause(UCatchClause uCatchClause) {
                Location location = context.getLocation(uCatchClause);
                createTLCatch(uCatchClause,location);
            }

            @Override
            public void visitLambdaExpression(ULambdaExpression uLambdaExpression) {
                Location location = context.getLocation(uLambdaExpression);
                createTLLambda(uLambdaExpression,location);
            }

            @Override
            public void visitQualifiedReferenceExpression(UQualifiedReferenceExpression uQualifiedReferenceExpression) {
                Location location = context.getLocation(uQualifiedReferenceExpression);
                createTLQualified(uQualifiedReferenceExpression,location);
            }

            @Override
            public void visitAnnotation(UAnnotation uAnnotation) {
                createAnnotation(uAnnotation);
            }

            @Override
            public void visitElement(UElement uElement) {
                Location location = context.getLocation(uElement);
                if (visit(UElement.class,uElement)) {
                    if (uElement instanceof UExpression
                            && !(uElement instanceof UastEmptyExpression)
                            && !(uElement instanceof UBlockExpression)
                            && !(uElement instanceof UExpressionList))  {

                            createTLExpression((UExpression) uElement, location);

                    }
                }
            }
        };
    }

    private boolean visit (Class<? extends UElement> currentVisit, UElement currentElement) {
        return getApplicableUastTypes().stream().filter(x -> (!x.equals(UElement.class))).noneMatch(x -> x.isInstance(currentElement) && !x.equals(currentVisit));
    }

    private void createTLFile (UFile uFile,Location location) {
        TLElement parent = getParent(uFile);
        List<String> imports = uFile.getImports().stream().map(i -> i.asSourceString()).collect(Collectors.toList());
        TLFile tlFile = new TLFile(location,uFile.asSourceString(),parent,imports,uFile.getPackageName());
        parentMap.put(uFile,tlFile);
        tlBridge.possibleMatchFound(tlFile);
    }

    private void createTLClass (UClass uClass,Location location) {
        TLElement parent = getParent(uClass);
        TLClass tlClass = new TLClass(location,uClass.asSourceString(),parent,uClass.getQualifiedName(),
                getSuperClassList(uClass),uClass.isStatic(),uClass.isFinal(),uClass.isInterface(),uClass.isEnum());
        parentMap.put(uClass,tlClass);
        tlBridge.possibleMatchFound(tlClass);
    }

    private void createTLMethod (UMethod uMethod,Location location) {
        TLElement parent = getParent(uMethod);
        TLType type= createTLType(uMethod.getReturnType());
        List<String> throwsList = Arrays.stream(uMethod.getThrowsList().getReferencedTypes())
                .map(r -> r.getCanonicalText()).collect(Collectors.toList());
    ///    throwsList.forEach(t -> log("LOgFILTER " + t));
//TODO throw list



        TLMethod tlMethod = new TLMethod(location,uMethod.asSourceString(),parent,uMethod.getName(),
                type,uMethod.isConstructor(),uMethod.isOverride(),uMethod.isStatic(),uMethod.getVisibility().getText());
        parentMap.put(uMethod,tlMethod);
        tlBridge.possibleMatchFound(tlMethod);
    }

    private void createTLCall(UCallExpression uCallExpression, Location location) {
        TLElement parent = getParent(uCallExpression);
        TLType type = createTLType(uCallExpression.getReturnType());
        String name = uCallExpression.getKind().getName().equals("constructor_call") ?
                uCallExpression.getClassReference().getResolvedName() : uCallExpression.getMethodName();
        TLCall tlCall = new TLCall(location,uCallExpression.asSourceString(),parent,type,name);
        parentMap.put(uCallExpression, tlCall);
        tlBridge.possibleMatchFound(tlCall);
    }

    private void createTLAssignment (UPolyadicExpression node, Location location) {
        TLElement parent = getParent(node);
        TLAssignment tlAssignment = new TLAssignment(location,node.asSourceString(),parent);
        parentMap.put(node,tlAssignment);
        tlBridge.possibleMatchFound(tlAssignment);
    }

    private void createTLVariable (UVariable uVariable, Location location) {
        TLElement parent = getParent(uVariable);
        TLType type = createTLType(uVariable.getType());
        TLVariable tlVariable = new TLVariable(location,uVariable.asSourceString(),parent,type,uVariable.getName());
        tlBridge.possibleMatchFound(tlVariable);
    }

    private void createTLVariable (USimpleNameReferenceExpression uSimpleNameReferenceExpression, Location location) {
        TLElement parent = getParent(uSimpleNameReferenceExpression);
        TLType type = createTLType(uSimpleNameReferenceExpression.getExpressionType());
        TLVariable tlVariable = new TLVariable(location,uSimpleNameReferenceExpression.asSourceString(),parent,type,uSimpleNameReferenceExpression.getResolvedName());
        tlBridge.possibleMatchFound(tlVariable);
    }

    private void createTLDeclaration(UDeclaration uDeclaration,Location location) {
        TLElement parent = getParent(uDeclaration.getUastParent());
        TLDeclaration tlDeclaration = new TLDeclaration(location,uDeclaration.getText(),parent);
        parentMap.put(uDeclaration.getUastParent(),tlDeclaration);
        tlBridge.possibleMatchFound(tlDeclaration);
    }

    private void createTLField(UField uField, Location location) {
        TLElement parent = getParent(uField);
        TLField tlField = new TLField(location,uField.asSourceString(),parent);
        TLVariable tlVariable = new TLVariable(location, uField.asSourceString().split("=")[0],tlField,
                createTLType(uField.getType()),uField.getName());
        tlField.addChild(tlVariable);
        parentMap.put(uField,tlField);
        tlBridge.possibleMatchFound(tlField);
        tlBridge.possibleMatchFound(tlVariable);
    }

    private void createTlArgument (UParameter uParameter,Location location) {
        TLElement parent = getParent(uParameter);
        TLArgument tlArgument = new TLArgument(location,uParameter.asSourceString(),parent,
                createTLType(uParameter.getType()),uParameter.getName());
        tlBridge.possibleMatchFound(tlArgument);
    }

    private void createTLQualified (UQualifiedReferenceExpression uQualifiedReferenceExpression, Location location) {
        TLElement parent = getParent(uQualifiedReferenceExpression);
        String source = uQualifiedReferenceExpression.asSourceString();
        TLType type = createTLType(uQualifiedReferenceExpression.getExpressionType());
        TLQualified tlExpression = new TLQualified(location, source, parent, type);
        parentMap.put(uQualifiedReferenceExpression, tlExpression);
        tlBridge.possibleMatchFound(tlExpression);
    }


    private void createTLExpression (UExpression uExpression,Location location) {
        if (!uElseList.contains(uExpression)) {
            TLElement parent = getParent(uExpression);
            String source = uExpression.asSourceString();
            TLType type = createTLType(uExpression.getExpressionType());
            TLExpression tlExpression = new TLExpression(location, source, parent, type);
            parentMap.put(uExpression, tlExpression);
            tlBridge.possibleMatchFound(tlExpression);
        }
    }

    private void createTLBlockExpression (UBlockExpression uBlockExpression, Location location) {
        TLElement parent = getParent(uBlockExpression);
        TLBlock tlBlock = new TLBlock(location,uBlockExpression.asSourceString(),parent);
        parentMap.put(uBlockExpression,tlBlock);
        tlBridge.possibleMatchFound(tlBlock);
    }

    private void createTLFor (UForExpression uForExpression,Location location) {
        TLElement parent = getParent(uForExpression);
        TLFor tlFor = new TLFor(location,uForExpression.asSourceString(),parent);
        parentMap.put(uForExpression,tlFor);
        tlBridge.possibleMatchFound(tlFor);
    }

    private void createTLForEachExpression (UForEachExpression uForEachExpression,Location location) {
        Location varLocation = location.getSecondary();
        location.setSecondary(null);
        TLElement parent = getParent(uForEachExpression);
        TLForEach tlForEach = new TLForEach(location,uForEachExpression.asSourceString(),parent);
        TLType variableType = createTLType(uForEachExpression.getVariable().getType());
        String variableName = uForEachExpression.getVariable().getName();
        TLVariable tlVariable = new TLVariable(varLocation,uForEachExpression.getVariable().asSourceString(),
                tlForEach,variableType,variableName);
        parentMap.put(uForEachExpression,tlForEach);
        tlBridge.possibleMatchFound(tlForEach);
        tlBridge.possibleMatchFound(tlVariable);
    }

    private void createTLWhile(UWhileExpression uWhileExpression, Location location) {
        TLElement parent = getParent(uWhileExpression);
        TLWhile tlWhile = new TLWhile(location,uWhileExpression.asSourceString(),parent);
        parentMap.put(uWhileExpression,tlWhile);
        tlBridge.possibleMatchFound(tlWhile);
    }

    private void createTLDoWhile(UDoWhileExpression uDoWhileExpression, Location location) {
        TLElement parent = getParent(uDoWhileExpression);
        TLDoWhile tlDoWhile = new TLDoWhile(location,uDoWhileExpression.asSourceString(),parent);
        parentMap.put(uDoWhileExpression,tlDoWhile);
        tlBridge.possibleMatchFound(tlDoWhile);
    }

    private void createTLIf(UIfExpression uIfExpression, Location location) {
        TLElement parent;
        boolean flag = false;
        if (parentMap.containsKey(uIfExpression)) {
            parent = parentMap.get(uIfExpression);
            flag = true;
        } else {
            parent = getParent(uIfExpression);
        }
        TLIf tlIf = new TLIf(location, uIfExpression.asSourceString(), parent,flag,uIfExpression.isTernary());
        parentMap.put(uIfExpression, tlIf);
        if (uIfExpression.getElseExpression() instanceof UIfExpression) {
            parentMap.put(uIfExpression.getElseExpression(), flag ? parent : tlIf);
        } else if (!(uIfExpression.getElseExpression() instanceof UastEmptyExpression)) {
            createTLElse(uIfExpression.getElseExpression(),location.getSecondary());
        }
        tlBridge.possibleMatchFound(tlIf);
    }

    private void createTLElse(UExpression uElseExpression, Location location) {
        uElseList.add(uElseExpression);
        TLElement parent = getParent(uElseExpression);
        TLElse tlElse = new TLElse(location,uElseExpression.asSourceString(),parent);
        parentMap.put(uElseExpression,tlElse);
        tlBridge.possibleMatchFound(tlElse);
    }

    private void createTLSwitch (USwitchExpression uSwitchExpression,Location location) {
        TLElement parent = getParent(uSwitchExpression);
        TLSwitch tlSwitch = new TLSwitch(location,uSwitchExpression.asSourceString(),parent);
        parentMap.put(uSwitchExpression,tlSwitch);
        tlBridge.possibleMatchFound(tlSwitch);
    }

    private void createTLCase (USwitchClauseExpression uSwitchClauseExpression, Location location) {
        TLElement parent = getParent(uSwitchClauseExpression);
        String source;
        if (uSwitchClauseExpression instanceof USwitchClauseExpressionWithBody) {
           source = ((USwitchClauseExpressionWithBody)uSwitchClauseExpression).getBody().asSourceString();
        } else {
            source  = uSwitchClauseExpression.asSourceString();
        }
        TLCase tlCase = new TLCase(location,source,parent);
        parentMap.put(uSwitchClauseExpression,tlCase);

    }

    private void createTLTry (UTryExpression uTryExpression, Location location) {
        TLElement parent = getParent(uTryExpression);
        int finallyLine = location.getSecondary() == null ? location.getEnd().getLine()  : location.getSecondary().getStart().getLine();
        location.setSecondary(null);
        TLTry tlTry = new TLTry(location,uTryExpression.asSourceString(),parent,finallyLine);
        parentMap.put(uTryExpression,tlTry);
        tlBridge.possibleMatchFound(tlTry);
    }

    private void createTLCatch (UCatchClause uCatchClause, Location location) {
        TLElement parent = getParent(uCatchClause);
        List<String> exception =
                uCatchClause.getTypeReferences().stream().map(t -> t.getQualifiedName()).collect(Collectors.toList());
        TLCatch tlCatch = new TLCatch(location,uCatchClause.asSourceString(),parent,exception);
        parentMap.put(uCatchClause,tlCatch);
        tlBridge.possibleMatchFound(tlCatch);
    }

    private void createTLLambda(ULambdaExpression uLambdaExpression,Location location) {
        TLElement parent = getParent(uLambdaExpression);
        TLLambda tlLambda = new TLLambda(location,uLambdaExpression.asSourceString(),parent);
        parentMap.put(uLambdaExpression,tlLambda);
        tlBridge.possibleMatchFound(tlLambda);
    }

    private void createAnnotation (UAnnotation uAnnotation) {
        TLElement parent = parentMap.get(uAnnotation.getUastParent());
        if (parent != null) {
            parent.getAnnotations().add(uAnnotation.asSourceString());
        }
    }

    private TLType createTLType (PsiType type) {
        try {

             List<String> superTypesArray = new ArrayList<>();
                PsiType[] superTypes = type.getSuperTypes();
                while (superTypes.length > 0) {
                    superTypesArray.add(superTypes[0].getCanonicalText());
                    superTypes = superTypes[0].getSuperTypes();
                }
                return new TLType(type.getCanonicalText(), superTypesArray);
        } catch (Exception e) {
            return new TLType("void",new ArrayList<>());
        }
    }

    private ArrayList<String> getSuperClassList (UClass uClass) {
        ArrayList<String> result = new ArrayList<>();
        try {
            if (uClass.getSuperClass().getQualifiedName().equals("java.lang.Object")) {
                return result;
            } else {
                result.addAll(uClass.getUastSuperTypes().stream().map(s -> s.getQualifiedName()).collect(Collectors.toList()));
                result.addAll(getSuperClassList(uClass.getSuperClass()));
            }
        } catch (NullPointerException e){
            return result;
        }
        return result;
    }

    private TLElement getParent (UElement uElement) {
        if  (uElement.getUastParent() == null) {
            return null;
        } else {
            return parentMap.getOrDefault(uElement.getUastParent(), getParent(uElement.getUastParent()));
        }
    }

    private Issue getIssue(TLIssue tlIssue) {
        if (issues.containsKey(tlIssue)) {
            return issues.get(tlIssue);
        } else {
            Issue issue =  Issue.create(tlIssue.getName(),
                    tlIssue.getBriefDescription(),
                    tlIssue.getExplanation(),
                    Category.create(tlIssue.getCategory(),tlIssue.getPriority()),
                    tlIssue.getPriority(),
                    Severity.fromLintOptionSeverity(tlIssue.getSeverity()),
                    new Implementation(UastDetector.class,EnumSet.of(Scope.JAVA_FILE)));
            issues.put(tlIssue,issue);
            return issue;
        }
    }

    @Override
    public void report (TLIssue issue, Match match, String message) {
        if (context != null) {
            context.report(getIssue(issue), match.getElement().getLocation(), message);
        }
    }

    @Override
    public void reportError (String message) {
        if (context != null) {
            logger.warn(message);//TODO
        }
    }


    /**
     * Log.
     *
     * @param message the message
     */
    public static void log(String message) {
        logger.warn(message);
    }

    @Override
    public void beforeCheckProject(Context context) {
        this.context = context;
        tlBridge.beforeCheckProject(context.getProject().getName());
    }

    @Override
    public void beforeCheckFile(Context context) {
        tlBridge.beforeCheckFile();
    }

    @Override
    public void afterCheckProject(Context context) {
        tlBridge.afterCheckProject();
    }

    @Override
    public void afterCheckFile(Context context) {
        parentMap.clear();
        tlBridge.afterCheckFile();
    }

}



