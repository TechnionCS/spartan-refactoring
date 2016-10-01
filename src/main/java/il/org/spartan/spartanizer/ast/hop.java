package il.org.spartan.spartanizer.ast;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface hop {
  /** Provides a {@link Iterable} access to the ancestors of an {@link ASTNode},
   * including the node itself.
   * @param ¢ JD
   * @return an {@link Iterable} that traverses the ancestors of the ASTNode.
   *         Use case: Counting the number of Expressions among a given
   *         ASTNode's ancestors */
  static Iterable<ASTNode> ancestors(final ASTNode ¢) {
    return () -> new Iterator<ASTNode>() {
      ASTNode current = ¢;

      @Override public boolean hasNext() {
        return current != null;
      }

      @Override public ASTNode next() {
        final ASTNode $ = current;
        current = current.getParent();
        return $;
      }
    };
  }

  static CompilationUnit compilationUnit(final ASTNode ¢) {
    return (CompilationUnit) searchAncestors.forType(COMPILATION_UNIT).from(¢);
  }

  /** @param ¢ JD
   * @return ASTNode of the type if one of ¢'s parent ancestors is a container
   *         type and null otherwise */
  static ASTNode containerType(final ASTNode ¢) {
    for (final ASTNode $ : hop.ancestors(¢.getParent()))
      if (iz.is($//
          , ANONYMOUS_CLASS_DECLARATION //
          , ANNOTATION_TYPE_DECLARATION //
          , ENUM_DECLARATION //
          , TYPE_DECLARATION //
          , ENUM_CONSTANT_DECLARATION //
      ))
        return $;
    return null;
  }

  /** @param root the node whose children we return
   * @return A list containing all the nodes in the given root'¢ sub tree */
  static List<ASTNode> descendants(final ASTNode root) {
    if (root == null)
      return null;
    final List<ASTNode> $ = new ArrayList<>();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        $.add(¢);
      }
    });
    $.remove(0);
    return $;
  }

  static VariableDeclarationFragment findDefinition(final VariableDeclarationStatement s, final SimpleName n) {
    return hop.findVariableDeclarationFragment(step.fragments(s), n);
  }

  static VariableDeclarationFragment findVariableDeclarationFragment(final List<VariableDeclarationFragment> fs, final SimpleName ¢) {
    for (final VariableDeclarationFragment $ : fs)
      if (wizard.same(¢, $.getName()))
        return $;
    return null;
  }

  /** @param n the node from which to extract the proper fragment
   * @param x the name by which to look for the fragment
   * @return fragment if such with the given name exists or null otherwise (or
   *         if ¢ or name are null) */
  // TODO this seems a bug
  static VariableDeclarationFragment getDefinition(final ASTNode n, final Expression x) {
    return hasNull(n, x) || n.getNodeType() != VARIABLE_DECLARATION_STATEMENT || x.getNodeType() != SIMPLE_NAME ? null
        : findDefinition((VariableDeclarationStatement) n, (SimpleName) x);
  }

  static SimpleName lastComponent(final Name ¢) {
    return ¢ == null ? null : ¢.isSimpleName() ? (SimpleName) ¢ : ¢.isQualifiedName() ? ((QualifiedName) ¢).getName() : null;
  }

  /** Find the last statement residing under a given {@link Statement}
   * @param ¢ JD
   * @return last statement residing under a given {@link Statement}, or
   *         <code><b>null</b></code> if not such sideEffects exists. */
  static ASTNode lastStatement(final Statement ¢) {
    return last(extract.statements(¢));
  }

  /** Extract the {@link MethodDeclaration} that contains a given node.
   * @param pattern JD
   * @return inner most {@link MethodDeclaration} in which the parameter is
   *         nested, or <code><b>null</b></code>, if no such statement
   *         exists. */
  static MethodDeclaration methodDeclaration(final ASTNode ¢) {
    for (ASTNode $ = ¢; $ != null; $ = $.getParent())
      if (iz.methodDeclaration($))
        return az.methodDeclaration($);
    return null;
  }

  static Name name(final Type ¢) {
    return ¢.isSimpleType() ? ((SimpleType) ¢).getName()
        : ¢.isNameQualifiedType() ? ((NameQualifiedType) ¢).getName() : ¢.isQualifiedType() ? ((QualifiedType) ¢).getName() : null;
  }

  /** Makes a list of all operands of an expression, comprising the left
   * operand, the right operand, followed by extra operands when they exist.
   * @param x JD
   * @return a list of all operands of an expression */
  static List<Expression> operands(final InfixExpression ¢) {
    if (¢ == null)
      return null;
    final List<Expression> $ = new ArrayList<>();
    $.add(left(¢));
    $.add(right(¢));
    if (¢.hasExtendedOperands())
      $.addAll(step.extendedOperands(¢));
    return $;
  }

  /** @param s current {@link Statement}.
   * @return the previous {@link Statement} in the parent {@link Block}. If
   *         parent is not {@link Block} return null, if n is first
   *         {@link Statement} also null. */
  static Statement previousStatementInBody(final Statement s) {
    final Block b = az.block(s.getParent());
    if (b == null)
      return null;
    final List<Statement> statements = step.statements(b);
    return statements.indexOf(s) < 1 ? null : statements.get(statements.indexOf(s) - 1);
  }

  /** @param ¢ JD
   * @return converssion of {@link Statement} , which is previous to the
   *         firstLastStatement in the loop body. */
  static VariableDeclarationFragment prevToFirstLastExpressionFragment(final ForStatement ¢) {
    final ASTNode n = findFirst.statementCanBePushedToForUpdaters(step.body(¢));
    if (n == null)
      return null;
    final Statement current = az.asStatement(n);
    if (current == null)
      return null;
    final Statement previous = previousStatementInBody(current);
    if (previous == null)
      return null;
    final VariableDeclarationStatement vds = az.variableDeclrationStatement(previous);
    return vds == null ? null : findFirst.elementOf(step.fragments(vds));
  }

  /** @param ¢ JD
   * @return conversion of {@link Statement}, which is previous to the
   *         firstLastStatement in the loop body. */
  static VariableDeclarationFragment prevToFirstLastExpressionFragment(final WhileStatement ¢) {
    final ASTNode n = findFirst.statementCanBePushedToForUpdaters(step.body(¢));
    if (n == null)
      return null;
    final Statement current = az.asStatement(n);
    if (current == null)
      return null;
    final Statement previous = previousStatementInBody(current);
    if (previous == null)
      return null;
    final VariableDeclarationStatement vds = az.variableDeclrationStatement(previous);
    return vds == null ? null : findFirst.elementOf(step.fragments(vds));
  }

  static SimpleName simpleName(final Type ¢) {
    return lastComponent(hop.name(¢));
  }
}
