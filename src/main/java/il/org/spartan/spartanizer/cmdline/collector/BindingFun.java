package il.org.spartan.spartanizer.cmdline.collector;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from the command line.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19 */
public final class BindingFun implements IApplication {
  /** Count the number of lines in a {@link File} f
   * @param ¢ File
   * @return
   * @throws IOException */
  static int countLines(final File ¢) throws IOException {
    try (LineNumberReader $ = new LineNumberReader(new FileReader(¢))) {
      $.skip(Long.MAX_VALUE);
      return $.getLineNumber();
    }
  }

  /** Count the number of lines in File named filename
   * @param fileName
   * @return
   * @throws IOException */
  static int countLines(final String fileName) throws IOException {
    return countLines(new File(fileName));
  }

  static MethodInvocation getMethodInvocation(final CompilationUnit u, final int lineNumber, final MethodInvocation i) {
    final Wrapper<MethodInvocation> $ = new Wrapper<>();
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodInvocation ¢) {
        if (u.getLineNumber(¢.getStartPosition()) == lineNumber)
          $.set(¢);
        return super.visit(¢);
      }
    });
    return $.get() == null ? i : $.get();
  }

  static String getPackageNameFromSource(final String source) {
    final ASTParser $ = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    $.setResolveBindings(true);
    $.setSource(source.toCharArray());
    return getPackageNameFromSource(new Wrapper<>(""), $.createAST(null));
  }

  private static String getPackageNameFromSource(final Wrapper<String> $, final ASTNode n) {
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration ¢) {
        $.set(¢.getName() + "");
        return false;
      }
    });
    return $.get();
  }

  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  boolean optDoNotOverwrite, optIndividualStatistics, optVerbose;
  boolean optStatsLines, optStatsChanges, printLog;
  int optRounds = 20;

  @Override public Object start(final IApplicationContext arg0) {
    ___.unused(arg0);
    try {
      prepareTempIJavaProject();
    } catch (final CoreException ¢) {
      System.err.println(¢.getMessage());
      return IApplication.EXIT_OK;
    }
    for (final File f : new FilesGenerator(".java", ".JAVA").from("C:\\Users\\sorimar\\git\\test")) {
      ICompilationUnit u = null;
      try {
        u = openCompilationUnit(f);
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setResolveBindings(true);
        parser.setSource(u);
        System.out.println(((CompilationUnit) parser.createAST(null)).getAST().hasResolvedBindings());
      } catch (JavaModelException | IOException x) {
        x.printStackTrace();
      }
    }
    return IApplication.EXIT_OK;
  }

  @Override public void stop() {
    ___.nothing();
  }

  String determineOutputFilename(final String path) {
    return !optDoNotOverwrite ? path : path.substring(0, path.lastIndexOf('.')) + "__new.java";
  }

  /** Discard compilation unit u
   * @param u */
  void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final NullPointerException | JavaModelException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
  }

  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException ¢) {
      ¢.printStackTrace();
    }
  }

  ICompilationUnit openCompilationUnit(final File ¢) throws IOException, JavaModelException {
    final String $ = FileUtils.read(¢);
    setPackage(getPackageNameFromSource($));
    return pack.createCompilationUnit(¢.getName(), $, false, null);
  }

  void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("tempP");
    if (p.exists())
      p.delete(true, null);
    p.create(null);
    p.open(null);
    final IProjectDescription d = p.getDescription();
    d.setNatureIds(new String[] { JavaCore.NATURE_ID });
    p.setDescription(d, null);
    javaProject = JavaCore.create(p);
    final IFolder binFolder = p.getFolder("bin");
    final IFolder sourceFolder = p.getFolder("src");
    srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
    binFolder.create(false, true, null);
    sourceFolder.create(false, true, null);
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    final IClasspathEntry[] buildPath = new IClasspathEntry[1];
    buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
    javaProject.setRawClasspath(buildPath, null);
  }

  void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
}
