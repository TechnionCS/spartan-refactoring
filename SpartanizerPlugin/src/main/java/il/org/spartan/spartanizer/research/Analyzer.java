package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** TODO: nothing implemented yet, will be working on this..
 * @author Ori Marcovitch
 * @since 2016 */
public class Analyzer {
  public static void main(String args[]) {
    analyze(args[0]);
  }

  public static void analyze(String folderName) {
    for (File ¢ : getFiles(folderName))
      analyze(¢);
  }

  /** @param folderName
   * @return */
  private static List<File> getFiles(String folderName) {
    // TODO get all files recursively
    return null;
  }

  /** @param f */
  private static void analyze(File f) {
    markAllNP(spartanize());
    report();
  }

  /**
   * 
   */
  private static void report() {
    // TODO output statistics
  }

  /**
   * 
   */
  private static void markAllNP(ASTNode n) {
    // TODO create visitor which adds comments each time it find an NP, No need
    // for iterative, just one traversal.
    // collect statistics of course
  }

  /**
   * 
   */
  private static ASTNode spartanize() {
    return null;
    // TODO call some spartanizer
  }
}
