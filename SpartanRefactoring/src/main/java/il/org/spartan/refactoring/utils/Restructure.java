package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

/**
 * An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> functions for restructuring expressions.
 *
 * @author Yossi Gil
 * @since 2015-07-21
 */
public enum Restructure {
  ;
  /**
   * Duplicate all {@link Statement} object found in a given list into another
   * list.
   *
   * @param from
   *          JD
   * @param into
   *          JD
   */
  public static void duplicateInto(final List<Statement> from, final List<Statement> into) {
    for (final Statement s : from)
      duplicateInto(s, into);
  }
  /**
   * Duplicate a {@link Statement} into another list.
   *
   * @param from
   *          JD
   * @param into
   *          JD
   */
  public static void duplicateInto(final Statement from, final List<Statement> into) {
    into.add(duplicate(from));
  }
  /**
   * Determine whether a give {@link ASTNode} includes precisely one
   * {@link Statement}, and return this statement.
   *
   * @param n
   *          JD
   * @return the single statement contained in the parameter, or
   *         <code><b>null</b></code> if not value exists.
   */
  public static Statement singleStatement(final ASTNode n) {
    final List<Statement> $ = statements(n);
    return $.size() != 1 ? null : $.get(0);
  }
  /**
   * Flatten the list of arguments to an {@link InfixExpression}, e.g., convert
   * an expression such as <code>(a + b) + c</code> whose inner form is roughly
   * "+(+(a,b),c)", into <code>a + b + c</code>, whose inner form is (roughly)
   * "+(a,b,c)".
   *
   * @param $
   *          JD
   * @return a duplicate of the argument, with the a flattened list of operands.
   */
  public static InfixExpression flatten(final InfixExpression $) {
    return Subject.operands(flattenInto($.getOperator(), operands($), new ArrayList<Expression>())).to(duplicate($).getOperator());
  }
  private static List<Expression> flattenInto(final Operator o, final List<Expression> es, final List<Expression> $) {
    for (final Expression e : es)
      flattenInto(o, e, $);
    return $;
  }
  private static List<Expression> flattenInto(final Operator o, final Expression e, final List<Expression> $) {
    final Expression core = core(e);
    return !Is.infix(core) || asInfixExpression(core).getOperator() != o ? add(!Is.simple(core) ? e : core, $)
        : flattenInto(o, operands(asInfixExpression(core)), $);
  }
  private static List<Expression> add(final Expression e, final List<Expression> $) {
    $.add(e);
    return $;
  }
  /**
   * Parenthesize an expression (if necessary).
   *
   * @param e
   *          JD
   * @return a {@link Funcs#duplicate(Expression)} of the parameter wrapped in
   *         parenthesis.
   */
  public static Expression parenthesize(final Expression e) {
    if (Is.simple(e))
      return duplicate(e);
    final ParenthesizedExpression $ = e.getAST().newParenthesizedExpression();
    $.setExpression(e.getParent() == null ? e : duplicate(e));
    return $;
  }
  /**
   * Compute the "de Morgan" conjugate of an operator.
   *
   * @param o
   *          must be either {@link Operator#CONDITIONAL_AND} or
   *          {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the parameter is
   *         {@link Operator#CONDITIONAL_OR}, or {@link Operator#CONDITIONAL_OR}
   *         if the parameter is {@link Operator#CONDITIONAL_AND}
   * @see Restructure#conjugate(InfixExpression)
   */
  public static Operator conjugate(final Operator o) {
    assert Is.deMorgan(o);
    return o.equals(CONDITIONAL_AND) ? CONDITIONAL_OR : CONDITIONAL_AND;
  }
  /**
   * Compute the "de Morgan" conjugate of the operator present on an
   * {@link InfixExpression}.
   *
   * @param e
   *          an expression whose operator is either
   *          {@link Operator#CONDITIONAL_AND} or
   *          {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the operator present on the
   *         parameter is {@link Operator#CONDITIONAL_OR}, or
   *         {@link Operator#CONDITIONAL_OR} if this operator is
   *         {@link Operator#CONDITIONAL_AND}
   * @see Restructure#conjugate(Operator)
   */
  public static Operator conjugate(final InfixExpression e) {
    return conjugate(e.getOperator());
  }
}
