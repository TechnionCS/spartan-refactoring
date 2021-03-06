package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.specificity;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** reorder comparisons so that the specific value is placed on the right.
 * Specific value means a literal, or any of the two keywords <code>
 * <b>this</b>
 * </code> or <code>
 * <b>null</b>
 * </code> .
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixComparisonSpecific extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = -0x2958F3B5F7F3C3D1L;
  private static final Comparator<Expression> specifity = new specificity();

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Exchange left and right operands of comparison";
  }
  @Override public boolean prerequisite(final InfixExpression ¢) {
    return specifity.compare(left(¢), right(¢)) < 0 && !¢.hasExtendedOperands() && iz.comparison(¢)
        && (specificity.defined(left(¢)) || specificity.defined(right(¢)));
  }
  @Override public Expression replacement(final InfixExpression ¢) {
    return cons.conjugate(¢);
  }
}
