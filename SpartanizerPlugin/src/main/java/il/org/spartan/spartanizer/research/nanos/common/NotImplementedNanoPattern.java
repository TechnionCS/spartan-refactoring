/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Jan 8, 2017 */
package il.org.spartan.spartanizer.research.nanos.common;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;

public class NotImplementedNanoPattern<N extends ASTNode> extends NanoPatternTipper<N> {
  private static final long serialVersionUID = -7953784585309364300L;

  @Override public String technicalName() {
    return null;
  }

  @Override protected Tip pattern(@SuppressWarnings("unused") final N ¢) {
    return null;
  }

  @Override public boolean check(@SuppressWarnings("unused") final N __) {
    return false;
  }
}
