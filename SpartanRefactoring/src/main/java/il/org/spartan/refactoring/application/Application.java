package il.org.spartan.refactoring.application;

import il.org.spartan.files.*;
import il.org.spartan.misc.*;
import il.org.spartan.refactoring.handlers.*;
import il.org.spartan.utils.*;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

/**
 * An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from the command line.
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19
 */
@SuppressWarnings("static-method") public class Application implements IApplication {
  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  boolean optDoNotOverwrite = false, optIndividualStatistics = false, optVerbose = false;
  boolean optStatsLines = false, optStatsChanges = false;
  int optRounds = 20;
  String optPath;

  @Override public Object start(final IApplicationContext arg0) {
    if (parseArguments(Arrays.asList((String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS))))
      return IApplication.EXIT_OK;
    final List<FileStats> fileStats = new ArrayList<>();
    try {
      prepareTempIJavaProject();
    } catch (final CoreException e) {
      System.err.println(e.getMessage());
      return IApplication.EXIT_OK;
    }
    int done = 0, failed = 0;
    for (final File f : new FilesGenerator(".java", ".JAVA").from(optPath))
      try (AutoCloseableCompilationUnit acu = openCompilationUnit(f)) {
        final FileStats s = a(f, acu.get());
        fileStats.add(s);
        ++done;
      } catch (final JavaModelException | IOException e) {
        System.err.println(f + ": " + e.getMessage());
        ++failed;
      } catch (final Exception e) {
        System.err.println("An unexpected error has occurred on file " + f + ": " + e.getMessage());
        e.printStackTrace();
        ++failed;
      }
    System.out.println(done + " files processed. " + (failed == 0 ? "" : failed + " failed."));
    if (optStatsChanges)
      printChangeStatistics(fileStats);
    if (optStatsLines)
      printLineStatistics(fileStats);
    return IApplication.EXIT_OK;
  }
  /**
   * @param f
   * @param u
   * @return
   * @throws IOException
   * @throws FileNotFoundException
   * @throws JavaModelException
   */
  private FileStats a(final File f, ICompilationUnit u) throws IOException, FileNotFoundException, JavaModelException {
    final FileStats s = new FileStats(f);
    for (int i = 0; i < optRounds; ++i) {
      final int n = CleanupHandler.countSuggestions(u);
      if (n == 0)
        break;
      s.addRoundStat(n);
      ApplySpartanizationHandler.execute(u);
    }
    FileUtils.writeToFile(determineOutputFilename(f.getAbsolutePath()), u.getSource());
    if (optVerbose)
      System.out.println("Spartanized file " + f.getAbsolutePath());
    s.countLinesAfter();
    return s;
  }
  @Override public void stop() {
    // Unused
  }
  void printHelpPrompt() {
    System.out.println("Spartan Refactoring plugin command line");
    System.out.println("Usage: eclipse -application il.org.spartan.refactoring.application -nosplash [OPTIONS] PATH");
    System.out.println("Executes the Spartan Refactoring Eclipse plug-in from the command line on all the Java source files "
        + "within the given PATH. Files are spartanized in place by default.");
    System.out.println("");
    System.out.println("Options:");
    System.out
        .println("  -N       Do not overwrite existing files (writes the Spartanized output to a new file in the same directory)");
    System.out.println("  -C<num>  Maximum number of Spartanizaion rounds for each file (default: 20)");
    System.out.println("  -E       Display statistics for each file separately");
    System.out.println("  -V       Be verbose");
    System.out.println("");
    System.out.println("Print statistics:");
    System.out.println("  -l       Show the number of lines before and after Spartanization");
    System.out.println("  -r       Show the number of Spartanizaion made in each round");
  }
  void printLineStatistics(final List<FileStats> ss) {
    System.out.println("\nLine differences:");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.fileName());
        System.out.println("    Lines before: " + f.getLinesBefore());
        System.out.println("    Lines after: " + f.getLinesAfter());
      }
    else {
      int totalBefore = 0, totalAfter = 0;
      for (final FileStats f : ss) {
        totalBefore += f.getLinesBefore();
        totalAfter += f.getLinesAfter();
      }
      System.out.println("  Lines before: " + totalBefore);
      System.out.println("  Lines after: " + totalAfter);
    }
  }
  private void printChangeStatistics(final List<FileStats> ss) {
    System.out.println("\nTotal changes made: ");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.fileName());
        for (int i = 0; i < optRounds; ++i)
          System.out.println("    Round #" + (i + 1) + ": " + (i < 9 ? " " : "") + f.getRoundStat(i));
      }
    else
      for (int i = 0; i < optRounds; ++i) {
        int roundSum = 0;
        for (final FileStats f : ss)
          roundSum += f.getRoundStat(i);
        System.out.println("    Round #" + (i + 1) + ": " + (i < 9 ? " " : "") + roundSum);
      }
  }
  String determineOutputFilename(final String path) {
    return !optDoNotOverwrite ? path : path.substring(0, path.lastIndexOf('.')) + "_new.java";
  }
  boolean parseArguments(final List<String> args) {
    if (args == null || args.isEmpty()) {
      printHelpPrompt();
      return true;
    }
    for (final String a : args)
      switch (a) {
        case "-N":
          optDoNotOverwrite = true;
          break;
        case "-E":
          optIndividualStatistics = true;
          break;
        case "-V":
          optVerbose = true;
          break;
        case "-l":
          optStatsLines = true;
          break;
        case "-r":
          optStatsChanges = true;
          break;
        default:
          if (!a.startsWith("-"))
            optPath = a;
          try {
            if (a.startsWith("-C"))
              optRounds = Integer.parseUnsignedInt(a.substring(2));
          } catch (final NumberFormatException e) {
            throw e; // TODO: Do we want to do something else?
          }
      }
    return optPath == null;
  }
  void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp");
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

  static class AutoCloseableCompilationUnit extends Wrapper<ICompilationUnit> implements AutoCloseable {
    /** instantiates this class */
    public AutoCloseableCompilationUnit(ICompilationUnit u) {
      super(u);
    }
    @Override public void close() throws JavaModelException {
      final ICompilationUnit u = get();
      try {
        u.close();
        u.delete(true, null);
      } catch (final JavaModelException | NullPointerException e) {
        e.printStackTrace();
      }
    }
  }

  AutoCloseableCompilationUnit openCompilationUnit(final File f) throws IOException, JavaModelException {
    final String source = FileUtils.read(f);
    setPackage(getPackageNameFromSource(source));
    return new AutoCloseableCompilationUnit(pack.createCompilationUnit(f.getName(), source, false, null));
  }
  String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    final Wrapper<String> $ = new Wrapper<>("");
    p.createAST(null).accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration node) {
        $.set(node.getName().toString());
        return false;
      }
    });
    return $.get();
  }
  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }

  /**
   * Data structure designed to hold and compute information about a single
   * file, in order to produce statistics when completed execution
   */
  private class FileStats {
    final File file;
    final int linesBefore;
    int linesAfter;
    final List<Integer> roundStats = new ArrayList<>();

    public FileStats(final File file) throws IOException {
      linesBefore = countLines(this.file = file);
    }
    public String fileName() {
      return file.getName();
    }
    public void countLinesAfter() throws IOException {
      linesAfter = countLines(determineOutputFilename(file.getAbsolutePath()));
    }
    public void addRoundStat(final int i) {
      roundStats.add(Integer.valueOf(i));
    }
    public int getRoundStat(final int r) {
      try {
        return roundStats.get(r).intValue();
      } catch (final IndexOutOfBoundsException e) {
        return 0;
      }
    }
    public int getLinesBefore() {
      return linesBefore;
    }
    public int getLinesAfter() {
      return linesAfter;
    }
  }

  static int countLines(final File f) throws IOException {
    try (LineNumberReader lr = new LineNumberReader(new FileReader(f))) {
      lr.skip(Long.MAX_VALUE);
      return lr.getLineNumber();
    }
  }
  static int countLines(final String fileName) throws IOException {
    return countLines(new File(fileName));
  }
}
