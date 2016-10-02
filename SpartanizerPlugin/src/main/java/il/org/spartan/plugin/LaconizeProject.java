package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.spartanizer.dispatch.*;

/** A handler for {@link Tips}. This handler executes all safe Tips on all Java
 * files in the current project.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01 */
public final class LaconizeProject extends BaseHandler {
  static final int MAX_PASSES = 20;

  /** Returns the number of spartanization tips for a compilation unit
   * @param u JD
   * @return number of tips available for the compilation unit */
  public int countTips(final ICompilationUnit u) {
    final AtomicInteger $ = new AtomicInteger(0);
    try {
      PlatformUI.getWorkbench().getProgressService().run(true, true, pm -> {
        pm.beginTask("Looking for tips in " + u.getResource().getProject().getName(), IProgressMonitor.UNKNOWN);
        final GUI$Applicator ¢ = new Trimmer();
        ¢.setMarker(null);
        ¢.setICompilationUnit(u);
        $.addAndGet(¢.countTips());
        pm.done();
      });
    } catch (InvocationTargetException x) {
      LoggingManner.logEvaluationError(this, x);
    } catch (InterruptedException x) {
      LoggingManner.logCancellationRequest(this, x);
    }
    return $.get();
  }

  public LaconizeProject() {
    this(new Trimmer());
  }

  public LaconizeProject(final GUI$Applicator inner) {
    super(inner);
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) throws ExecutionException {
    final StringBuilder message = new StringBuilder();
    final ICompilationUnit currentCompilationUnit = eclipse.currentCompilationUnit();
    final IJavaProject javaProject = currentCompilationUnit.getJavaProject();
    message.append("starting at " + currentCompilationUnit.getElementName() + "\n");
    final List<ICompilationUnit> us = eclipse.facade.compilationUnits(currentCompilationUnit);
    message.append("found " + us.size() + " compilation units \n");
    final IWorkbench wb = PlatformUI.getWorkbench();
    final int initialCount = countTips(currentCompilationUnit);
    message.append("with " + initialCount + " tips");
    if (initialCount == 0)
      return eclipse.announce("No tips for '" + javaProject.getElementName() + "' project\n" + message);
    eclipse.announce(message);
    final GUI$Applicator a = new Trimmer();
    int i;
    for (i = 0; i < MAX_PASSES; ++i) {
      final IProgressService ps = wb.getProgressService();
      final AtomicInteger passNum = new AtomicInteger(i + 1);
      final AtomicBoolean cancelled = new AtomicBoolean(false);
      try {
        ps.run(true, true, pm -> {
          // a.setProgressMonitor(pm);
          pm.beginTask(
              "Spartanizing project '" + javaProject.getElementName() + "' - " + "Pass " + passNum.get() + " out of maximum of " + MAX_PASSES,
              us.size());
          int n = 0;
          final List<ICompilationUnit> dead = new ArrayList<>();
          for (final ICompilationUnit ¢ : us) {
            if (pm.isCanceled()) {
              cancelled.set(true);
              break;
            }
            pm.worked(1);
            pm.subTask("Compilation unit #" + ++n + "/" + us.size() + " (" + ¢.getElementName() + ")");
            if (!a.apply(¢))
              dead.add(¢);
          }
          us.removeAll(dead);
          pm.done();
        });
      } catch (final InvocationTargetException x) {
        LoggingManner.logEvaluationError(this, x);
      } catch (final InterruptedException x) {
        LoggingManner.logEvaluationError(this, x);
      }
      if (cancelled.get() || us.isEmpty())
        break;
    }
    if (i == MAX_PASSES)
      throw new ExecutionException("Too many iterations");
    final int finalCount = countTips(currentCompilationUnit);
    return eclipse
        .announce("Spartanizing '" + javaProject.getElementName() + "' project \n" + "Completed in " + (1 + i) + " passes. \n" + "Total changes: "
            + (initialCount - finalCount) + "\n" + "Tips before: " + initialCount + "\n" + "Tips after: " + finalCount + "\n" + message);
  }
}
