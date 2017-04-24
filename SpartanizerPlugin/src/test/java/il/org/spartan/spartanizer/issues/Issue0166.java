package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link ForParameterRenameToIt}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0166 {
  @Test public void dollar() {
    trimminKof("for(final int $:as)sum+=$;")//
        .stays();
  }

  @Test public void doubleUnderscore() {
    trimminKof("for(final int __:as)sum+=_;")//
        .stays();
  }

  @Test public void innerVariable() {
    trimminKof("for(final int i:as){int sum; f(sum+=i); ++x;}")//
        .stays();
  }

  @Test public void meaningfulName() {
    trimminKof("for(final String fileName: ss) {f(fileName); ++x;}")//
        .stays();
  }

  @Test public void singleUnderscore() {
    trimminKof("for(final int _:as)sum+=_;")//
        .gives("for(final int __:as)sum+=__;")//
        .stays();
  }

  @Test public void statementBlock() {
    trimminKof("for(final Statement s:as){f(s);g(s);sum+=i;}")//
        .gives("for(final Statement ¢:as){f(¢);g(¢);sum+=i;}")//
        .stays();
  }

  @Test public void string() {
    trimminKof("for(String s:as)sum+=s;")//
        .gives("for(String ¢:as)sum+=¢;")//
        .stays();
  }

  @Test public void unused() {
    trimminKof("for(final int i:as)f(sum+=j);")//
        .stays();
  }

  @Test public void vanilla() {
    trimminKof("for(final int i:as)sum+=i;")//
        .gives("for(final int ¢:as)sum+=¢;")//
        .stays();
  }

  @Test public void vanillaBlock() {
    trimminKof("for(final int i:as){++i; sum+=i;}")//
        .gives("for(final int ¢:as){++¢;sum+=¢;}")//
        .stays();
  }
}
