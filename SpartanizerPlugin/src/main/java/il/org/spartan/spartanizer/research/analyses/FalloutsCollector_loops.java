package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;

/** Find all loops not matched by a nano pattern
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 11, 2017 */
public class FalloutsCollector_loops extends FolderASTVisitor {
  private static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static final File out = new File(Table.temporariesFolder + system.fileSeparator + "loops" + ".txt");

  public static void main(final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = FalloutsCollector_loops.class;
    blank(out);
    FolderASTVisitor.main(args);
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    try {
      yieldDescendants.untilClass(EnhancedForStatement.class).from(into.cu(spartanalyzer.fixedPoint(¢))).stream().filter(λ -> iz.simpleLoop(λ))
          .forEach(λ -> appendFile(out, λ + ""));
    } catch (@SuppressWarnings("unused") final AssertionError __) {
      System.err.print("X");
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      System.err.print("I");
    }
    return true;
  }

  @Override protected void visit(final String path) {
    appendFile(out, "-------" + path + "-------\n");
    super.visit(path);
  }
}