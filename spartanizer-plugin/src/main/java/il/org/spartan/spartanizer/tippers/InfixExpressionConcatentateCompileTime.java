package il.org.spartan.spartanizer.tippers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.StringLiteral;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Concat some strings to one string {@code
 * "abcde"
 * } to {@code
 * "abcde"
 * }
 * @author Dor Ma'ayan
 * @author Nov Shalmon
 * @since 2016-09-04 */
public final class InfixExpressionConcatentateCompileTime extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Collapse {
  private static final long serialVersionUID = -0x349BFB6422E9DC98L;

  @Override public String description() {
    return "Concat the strings to a one string";
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Concat the string literals to a single string";
  }
  @Override public ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != op.PLUS2)
      return null;
    final List<Expression> $ = extract.allOperands(x);
    assert $.size() >= 2;
    boolean clean = true;
    for (int i = 0; i < $.size() - 1;)
      if ($.get(i).getNodeType() != ASTNode.STRING_LITERAL || $.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)
        ++i;
      else {
        clean = false;
        final StringLiteral l = x.getAST().newStringLiteral();
        l.setLiteralValue(((StringLiteral) $.get(i)).getLiteralValue() + ((StringLiteral) $.get(i + 1)).getLiteralValue());
        $.remove(i);
        $.remove(i);
        $.add(i, l);
      }
    if (clean)
      return null;
    assert !$.isEmpty();
    return $.size() <= 1 ? the.firstOf($) : subject.operands($).to(op.PLUS2);
  }
}
