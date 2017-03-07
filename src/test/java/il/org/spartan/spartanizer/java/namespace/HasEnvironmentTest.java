/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Jan 1, 2017 */
package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.meta.*;

@RunWith(Parameterized.class)
public class HasEnvironmentTest extends MetaFixture {
  private static final Collection<String> signature = new HashSet<>();

  private static Collection<Object[]> collect(final MetaFixture... fs) {
    signature.clear();
    final Collection<Object[]> $ = new ArrayList<>();
    as.list(fs).forEach(t -> yieldDescendants.ofClass(ASTNode.class).from(t.reflectedCompilationUnit()).stream()
        .filter(λ -> !signature.contains(signature(λ))).forEach(λ -> {
          signature.add(signature(λ));
          $.add(as.array(λ, signature(λ)));
        }));
    return $;
  }

  @Parameters(name = "{index}. {1}: {0} ") public static Collection<Object[]> data() {
    return collect(new NamespaceTest(), new definitionTest());
  }

  private static String signature(final ASTNode ¢) {
    return separate.these(wizard.nodeName(¢), wizard.nodeName(parent(¢)), wizard.nodeName(parent(parent(¢)))).by('/');
  }

  private final ASTNode name;

  public HasEnvironmentTest(final ASTNode name, @SuppressWarnings("unused") final String signature) {
    this.name = name;
  }

  @Test public void notNullNode() {
    assert Environment.of(name) != null : //
    "\n name = " + name + //
        MetaFixture.ancestry(name) + //
        "\n\t environment = " + Environment.of(name)//
    ;
  }
}