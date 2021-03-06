package il.org.spartan.spartanizer.research.nanos.deprecated;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.WhileStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** Exhaust an iterator but do nothing with elements
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
public class Exhaust extends NanoPatternTipper<WhileStatement> {
  private static final long serialVersionUID = -0x3D1EEDEB45F193DBL;
  private static final Collection<UserDefinedTipper<WhileStatement>> tippers = as.list( //
      patternTipper("while ($X) {}", "exhaust(()->$X);", "Exhaust pattern: conevrt to fluent API")) //
  ;

  @Override public boolean canTip(final WhileStatement ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final WhileStatement ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public Category category() {
    return Category.Iterative;
  }
  @Override public String description() {
    return "Move an Iterable to its end using getNext() != null";
  }
}
