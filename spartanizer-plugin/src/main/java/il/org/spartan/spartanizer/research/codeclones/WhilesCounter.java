package il.org.spartan.spartanizer.research.codeclones;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.cmdline.GrandVisitor;

/** TODO oran1248: document class
 * @author oran1248
 * @since 2017-04-18 */
public class WhilesCounter {
  static int whiles;

  public static void main(final String[] args) {
    new GrandVisitor(args) {/**/}.visitAll(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final WhileStatement node) {
        ++whiles;
        return true;
      }
    });
    System.out.println(whiles);
  }
}
