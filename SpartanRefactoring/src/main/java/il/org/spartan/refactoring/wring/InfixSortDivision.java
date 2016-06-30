package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} that sorts the arguments of a {@link Operator#DIVIDE}
 * expression.
 *
 * @author Yossi Gil
 * @since 2015-09-05
 */
public final class InfixSortDivision extends Wring.InfixSortingOfCDR implements Kind.ReorganizeExpression {
  @Override boolean sort(final List<Expression> es) {
    return ExpressionComparator.MULTIPLICATION.sort(es);
  }
  @Override boolean scopeIncludes(final InfixExpression e) {
    return in(e.getOperator(), DIVIDE);
  }
}