package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class LispFirstElement extends NanoPatternTipper<MethodInvocation> {
  private static final List<UserDefinedTipper<MethodInvocation>> tippers = new ArrayList<UserDefinedTipper<MethodInvocation>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.get(0)", "first($X)", "lisp: first"));
    }
  };

  @Override public boolean canTip(final MethodInvocation ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final MethodInvocation ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String category() {
    return Category.Lisp + "";
  }
}
