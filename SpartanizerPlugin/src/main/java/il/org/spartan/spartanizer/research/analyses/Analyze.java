package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.classifier.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.patterns.characteristics.*;
import il.org.spartan.spartanizer.research.patterns.methods.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Analyze {
  {
    set("outputDir", "/tmp");
  }
  private static InteractiveSpartanizer spartanizer;
  @SuppressWarnings("rawtypes") private static Map<String, Analyzer> analyses = new HashMap<String, Analyzer>() {
    static final long serialVersionUID = 1L;
    {
      put("AvgIndicatorMetrical", new AvgIndicatorMetricalAnalyzer());
      put("understandability", new UnderstandabilityAnalyzer());
      put("understandability2", new Understandability2Analyzer());
      put("statementsToAverageU", new SameStatementsAverageUAnalyzer());
      put("magic numbers", new MagicNumbersAnalysis());
    }
  };

  public static void main(final String args[]) {
    AnalyzerOptions.parseArguments(args);
    initializeSpartanizer();
    createOutputDirIfNeeded();
    final long startTime = System.currentTimeMillis();
    switch (getProperty("analysis")) {
      case "methods":
        methodsAnalyze();
        break;
      case "classify":
        classify();
        break;
      case "sort":
        spartanizeMethodsAndSort();
        break;
      case "hindex":
        hIndex.analyze();
        break;
      default:
        analyze();
    }
    System.out.println("Took " + new DecimalFormat("#0.00").format((System.currentTimeMillis() - startTime) / 1000.0) + "s");
  }

  /** THE analysis */
  private static void spartanizeMethodsAndSort() {
    final List<MethodDeclaration> methods = new ArrayList<>();
    for (final File f : inputFiles()) {
      final CompilationUnit cu = az.compilationUnit(compilationUnit(f));
      Logger.logCompilationUnit(cu);
      types(cu).stream().filter(haz::methods).forEach(t -> {
        Logger.logType(t);
        for (final MethodDeclaration ¢ : methods(t).stream().filter(m -> !m.isConstructor() && body(m) != null).collect(Collectors.toList()))
          try {
            // System.out.println(¢);
            Count.before(¢);
            final MethodDeclaration after = findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
            Count.after(after);
            methods.add(after);
          } catch (@SuppressWarnings("unused") final AssertionError __) {
            //
          }
        Logger.finishedType();
      });
    }
    methods.sort((x, y) -> count.statements(x) < count.statements(y) ? -1 : count.statements(x) > count.statements(y) ? 1 : 0);
    writeFile(new File(outputDir() + "/after.java"), methods.stream().map(x -> format.code(x + "")).reduce("", (x, y) -> x + y));
    writeFile(new File(outputDir() + "/notTagged.java"),
        methods.stream().filter(m -> !(javadoc(m) + "").contains("[[")).map(x -> format.code(x + "")).reduce("", (x, y) -> x + y));
    Logger.summarizeSortedMethodStatistics(outputDir());
    Logger.summarizeNPStatistics(outputDir());
    Count.print();
  }

  private static void initializeSpartanizer() {
    spartanizer = addNanoPatterns(new InteractiveSpartanizer());
  }

  public static Toolbox toolboxWithNanoPatterns() {
    return addNanoPatterns(new InteractiveSpartanizer()).toolbox;
  }

  /** run an interactive classifier to classify nanos! */
  private static void classify() {
    String code = "";
    for (final File ¢ : inputFiles())
      code += spartanize(compilationUnit(¢));
    new Classifier().analyze(getCompilationUnit(code));
  }

  /** analyze nano patterns in code. */
  private static void analyze() {
    AnalyzerOptions.setVerbose();
    deleteOutputFile();
    for (final File ¢ : inputFiles()) {
      System.out.println("\nnow: " + ¢.getPath());
      final ASTNode cu = compilationUnit(¢);
      Logger.logCompilationUnit(az.compilationUnit(cu));
      Logger.logFile(¢.getName());
      try {
        appendFile(new File(outputDir() + "/after.java"), spartanize(cu));
      } catch (@SuppressWarnings("unused") final AssertionError __) {
        //
      }
      /** @author matteo append also before */
      // appendFile(new File(getProperty("outputDir") + "/before.java"), (cu +
      // ""));
      // appendFile(new File(getProperty("outputDir") + "/after.java"),
      // spartanize(cu));
    }
    Logger.summarize(outputDir());
  }

  private static String spartanize(final ASTNode cu) {
    return spartanizer.fixedPoint(cu + "");
  }

  @SuppressWarnings("rawtypes") private static void methodsAnalyze() {
    for (final File f : inputFiles())
      //
      types(az.compilationUnit(compilationUnit(f))).stream().filter(haz::methods).forEach(t -> {
        for (final MethodDeclaration ¢ : methods(t).stream().filter(m -> !m.isConstructor()).collect(Collectors.toList()))
          try {
            for (final Analyzer a : analyses.values())
              a.logMethod(¢, findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + ""))))));
          } catch (@SuppressWarnings("unused") final AssertionError __) {
            //
          }
      });
    for (final String a : analyses.keySet()) {
      System.out.println("++++++++" + a + "++++++++");
      analyses.get(a).printComparison();
      analyses.get(a).printAccumulated();
    }
  }

  /** Add our wonderful patterns (which are actually just special tippers) to
   * the gUIBatchLaconizer.
   * @param ¢ our gUIBatchLaconizer
   * @return */
  private static InteractiveSpartanizer addNanoPatterns(final InteractiveSpartanizer ¢) {
    if ("false".equals(getProperty("nmethods")))
      addCharacteristicMethodPatterns(¢);
    return addMethodPatterns(¢)
        .add(ConditionalExpression.class, //
            new DefaultsTo(), //
            new Unless(), //
            new SafeReference(), //
            null) //
        .add(Assignment.class, //
            new AssignmentLazyEvaluation(), //
            null) //
        .add(Block.class, //
            new CreateFrom(), //
            new FindFirstBlock(), //
            new ReturnOld(), //
            new ReturnAllMatches(), //
            new ReturnAnyMatches(), //
            null) //
        .add(EnhancedForStatement.class, //
            new Aggregate(), //
            new ContainsEnhancedFor(), //
            new ForEach(), //
            // new ReduceEnhancedFor(), //
            null) //
        // .add(ForStatement.class, //
        // new Contains(), //
        // new CopyArray(), //
        // new FindFirst(), //
        // new ForEachEnhanced(), //
        // new InitArray(), //
        // new MaxEnhanced(), //
        // new Min(), //
        // new Reduce(), //
        // null) //
        .add(IfStatement.class, //
            new IfNullThrow(), //
            new IfNullReturn(), //
            new IfNullReturnNull(), //
            new ExecuteWhen(), //
            new PutIfAbsent(), //
            new IfThrow(), //
            null) //
        .add(InfixExpression.class, //
            new Between(), //
            new LispLastIndex(), //
            null)//
        .add(MethodInvocation.class, //
            new LispFirstElement(), //
            new LispLastElement(), //
            null) //
        .add(TryStatement.class, //
            new IfThrowsReturnNull(), //
            null);
  }

  private static InteractiveSpartanizer addMethodPatterns(final InteractiveSpartanizer ¢) {
    return ¢.add(MethodDeclaration.class, //
        new ConstantReturner(), //
        new FactoryMethod(), //
        new DefaultParametersAdder(), //
        new Delegator(), //
        new DoNothingReturnParam(), //
        new DoNothingReturnThis(), //
        new DownCaster(), //
        new Examiner(), //
        new FluentSetter(), ///
        new Getter(), //
        new ForEachApplier(), //
        new Setter(), //
        new SuperDelegator(), //
        new Thrower(), //
        new ToStringMethod(), //
        new TypeChecker(), //
        new UpCaster(), //
        null);
  }

  private static InteractiveSpartanizer addCharacteristicMethodPatterns(final InteractiveSpartanizer ¢) {
    return ¢.add(MethodDeclaration.class, //
        new Fluenter(), //
        new Independent(), //
        new JDPattern(), //
        new MethodEmpty(), //
        new UseParameterAndReturnIt(), //
        null);
  }
}