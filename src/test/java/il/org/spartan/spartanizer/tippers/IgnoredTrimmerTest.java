package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public final class IgnoredTrimmerTest {
  public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimmingOf(" @SuppressWarnings() int $ = (Class<T>) findClass(className);\n")//
        .stays();
  }

  @Test public void forwardDeclaration1() {
    trimmingOf("/* * This is a comment */ int i = 6; int j = 2; int k = i+2; S.x.f(i-j+k); ")
        .gives(" /* * This is a comment */ int j = 2; int i = 6; int k = i+2; S.x.f(i-j+k); ");
  }

  @Test public void forwardDeclaration2() {
    trimmingOf("/* * This is a comment */ int i = 6, h = 7; int j = 2; int k = i+2; S.x.f(i-j+k); ")
        .gives(" /* * This is a comment */ int h = 7; int j = 2; int i = 6; int k = i+2; S.x.f(i-j+k); ");
  }

  @Test public void forwardDeclaration3() {
    trimmingOf("/* * This is a comment */ int i = 6; int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); y(i); y(i+m); ")
        .gives(" /* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); int i = 6; y(i); y(i+m); ");
  }

  @Test public void forwardDeclaration4() {
    trimmingOf(
        " /* * This is a comment */ int i = 6; int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); final C bc = new C(i); y(i+m+bc.j); private static class C { public C(int i) { j = 2*i; public final int j; ")
            .gives(
                " /* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); int i = 6; final C bc = new C(i); y(i+m+bc.j); private static class C { public C(int i) { j = 2*i; public final int j; ");
  }

  @Test public void forwardDeclaration5() {
    trimmingOf("/* * This is a comment */ int i = y(0); int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + i); y(i+m); ")
        .gives(" /* * This is a comment */ int j = 3; int k = j+2; int i = y(0); int m = k + j -19; y(m*2 - k/m + i); y(i+m); ");
  }

  @Test public void forwardDeclaration6() {
    trimmingOf(" /* * This is a comment */ int i = y(0); int h = 8; int j = 3; int k = j+2 + y(i); int m = k + j -19; y(m*2 - k/m + i); y(i+m); ")
        .gives(" /* * This is a comment */ int h = 8; int i = y(0); int j = 3; int k = j+2 + y(i); int m = k + j -19; y(m*2 - k/m + i); y(i+m); ");
  }

  @Test public void forwardDeclaration7() {
    trimmingOf(
        " j = 2*i; } public final int j; private C yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f(res.j); return res; ")
            .gives(
                " j = 2*i; } public final int j; private C yada6() { final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; final C res = new C(6); S.x.f(res.j); return res; ");
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement() {
    trimmingOf("if(a) { int i = 3; }")//
        .stays();
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement2() {
    trimmingOf("if(a) { Object o; }")//
        .stays();
  }

  @Test public void inlineSingleUse01() {
    trimmingOf("/* * This is a comment */ int i = y(0); int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + i); ")
        .gives(" /* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + (y(0))); ");
  }

  @Test public void inlineSingleUse02() {
    trimmingOf("/* * This is a comment */ int i = 5,j=3; int k = j+2; int m = k + j -19 +i; y(k); ")
        .gives(" /* * This is a comment */ int j=3; int k = j+2; int m = k + j -19 +(5); y(k); ");
  }

  @Test public void inlineSingleUse03() {
    trimmingOf("/* * This is a comment */ int i = 5; int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + i); ")
        .gives(" /* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + (5)); ");
  }

  @Test public void inlineSingleUse04() {
    trimmingOf("int x = 6; final C b = new C(x); int y = 2+b.j; y(y-b.j); y(y*2); ")
        .gives(" final C b = new C((6)); int y = 2+b.j; y(y-b.j); y(y*2); ");
  }

  @Test public void inlineSingleUse05() {
    trimmingOf("int x = 6; final C b = new C(x); int y = 2+b.j; y(y+x); y(y*x); ")//
        .gives(" int x = 6; int y = 2+(new C(x)).j; y(y+x); y(y*x); ");
  }

  @Test public void inlineSingleUse09() {
    trimmingOf(
        " final A a = new D().new A(V){\nABRA\n{\nCADABRA\n{V;); wizard.assertEquals(5, a.new Context().lineCount()); final PureIterable&lt;Mutant&gt; ms = a.generateMutants(); wizard.assertEquals(2, count(ms)); final PureIterator&lt;Mutant&gt; i = ms.iterator(); assert (i.hasNext()); wizard.assertEquals(V;{\nABRA\nABRA\n{\nCADABRA\n{\nV;, i.next().text); assert (i.hasNext()); wizard.assertEquals(V;{\nABRA\n{\nCADABRA\nCADABRA\n{\nV;, i.next().text); assert !(i.hasNext()); ")
            .stays();
  }

  @Test public void inlineSingleUse10() {
    trimmingOf(
        " final A a = new A(\"{\nABRA\n{\nCADABRA\n{\"); wizard.assertEquals(5, a.new Context().lineCount()); final PureIterable<Mutant> ms = a.mutantsGenerator(); wizard.assertEquals(2, count(ms)); final PureIterator<Mutant> i = ms.iterator(); assert (i.hasNext()); wizard.assertEquals(\"{\nABRA\nABRA\n{\nCADABRA\n{\n\", i.next().text); assert (i.hasNext()); wizard.assertEquals(\"{\nABRA\n{\nCADABRA\nCADABRA\n{\n\", i.next().text); assert !(i.hasNext());")
            .stays();
  }

  @Test public void issue120_1() {
    trimmingOf("\"a\"+\"b\"")//
        .gives("\"ab\"");
  }

  @Test public void issue120_2() {
    trimmingOf("\"abc\"+\"de\"+\"fgh\"")//
        .gives("\"abcdefgh\"");
  }

  @Test public void issue120_3() {
    trimmingOf("\"abc\"+a.toString()+\"de\"+\"fgh\"")//
        .gives("\"abc\"+a.toString()+\"defgh\"");
  }

  @Test public void issue120_4() {
    trimmingOf("c.toString()+\"abc\"+a.toString()+\"de\"+\"fgh\"")//
        .gives("c.toString()+\"abc\"+a.toString()+\"defgh\"");
  }

  @Test public void issue54ForPlainUseInCondition() {
    trimmingOf("int a = f(); for (int i = 0; a <100; ++i) b[i] = 3;")//
        .stays();
  }

  @Test public void issue54ForPlainUseInUpdaters() {
    trimmingOf("int a = f(); for (int i = 0; i <100; i *= a) b[i] = 3;")//
        .stays();
  }

  public void issue62b() {
    trimmingOf("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;i+=1) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;i++) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;++i) if(false) break; return i; }")//
        .gives("int f(int i) { for(;i<100;++i){} return i; }")//
        .stays();
  }

  @Test public void reanmeReturnVariableToDollar01() {
    trimmingOf(" public C(int i) { j = 2*i; public final int j; public C yada6() { final C res = new C(6); S.x.f(res.j); return res; ")
        .gives(" public C(int i) { j = 2*i; public final int j; public C yada6() { final C $ = new C(6); S.x.f($.j); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar02() {
    trimmingOf(
        " int res = blah.length(); if (blah.contains(0xDEAD)) return res * 2; if (res % 2 ==0) return ++res; if (blah.startsWith(\"y\")) { return y(res); int x = res + 6; if (x>1) return res + x; res -= 1; return res; ")
            .gives(
                " int $ = blah.length(); if (blah.contains(0xDEAD)) return $ * 2; if ($ % 2 ==0) return ++$; if (blah.startsWith(\"y\")) { return y($); int x = $ + 6; if (x>1) return $ + x; $ -= 1; return $; ");
  }

  @Test public void reanmeReturnVariableToDollar03() {
    trimmingOf(
        " public C(int i) { j = 2*i; public final int j; public int yada7(final String blah) { final C res = new C(blah.length()); if (blah.contains(0xDEAD)) return res.j; int x = blah.length()/2; if (x==3) return x; x = y(res.j - x); return x; ")
            .gives(
                " public C(int i) { j = 2*i; public final int j; public int yada7(final String blah) { final C res = new C(blah.length()); if (blah.contains(0xDEAD)) return res.j; int $ = blah.length()/2; if ($==3) return $; $ = y(res.j - $); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar06() {
    trimmingOf(
        " j = 2*i; } public final int j; public void yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { final C res2 = new C(res.j); S.x.f(res2.j); doStuff(res2); private int doStuff(final C r) { final C res = new C(r.j); return res.j + 1; S.x.f(res.j); ")
            .gives(
                " j = 2*i; } public final int j; public void yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { final C res2 = new C(res.j); S.x.f(res2.j); doStuff(res2); private int doStuff(final C r) { final C $ = new C(r.j); return $.j + 1; S.x.f(res.j); ");
  }

  @Test public void reanmeReturnVariableToDollar07() {
    trimmingOf(
        " j = 2*i; } public final int j; public C yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f(res.j); return res; ")
            .gives(
                " j = 2*i; } public final int j; public C yada6() { final C $ = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f($.j); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar08() {
    trimmingOf(
        " public C(int i) { j = 2*i; public final int j; public C yada6() { final C res = new C(6); if (res.j == 0) return null; S.x.f(res.j); return res; ")
            .gives(
                " public C(int i) { j = 2*i; public final int j; public C yada6() { final C $ = new C(6); if ($.j == 0) return null; S.x.f($.j); return $; ");
  }

  @Test public void reanmeReturnVariableToDollar09() {
    trimmingOf(
        " public C(int i){j = 2*i;public final int j;public C yada6() { final C res = new C(6); if (res.j == 0) return null; S.x.f(res.j); return null;")
            .stays();
  }

  @Test public void reanmeReturnVariableToDollar10() {
    trimmingOf(
        "@Override public IMarkerResolution[] getResolutions(final IMarker m) { try { final Laconization s = All.get((String) m.getAttribute(Builder.Laconization_TYPE_KEY)); ")
            .gives(
                "@Override public IMarkerResolution[] getResolutions(final IMarker m) { try { final Laconization $ = All.get((String) m.getAttribute(Builder.Laconization_TYPE_KEY)); ");
  }

  @Test public void renameVariableUnderscore2() {
    trimmingOf("class A {int __; int f(int __) {return __;}}")//
        .gives("class A {int ____; int f(int ____) {return ____;}}");
  }

  @Test public void replaceClassInstanceCreationWithFactoryClassInstanceCreation() {
    trimmingOf("Character x = new Character(new Character(f()));")//
        .gives("Character x = Character.valueOf(Character.valueOf(f()));");
  }

  @Test public void stringFromBuilderAddParenthesis() {
    trimmingOf("new StringBuilder(f()).append(1+1).toString()")//
        .gives("\"\" + f() + (1+1)");
  }

  @Test public void stringFromBuilderGeneral() {
    trimmingOf("new StringBuilder(myName).append(\"\'s grade is \").append(100).toString()")//
        .gives("myName + \"\'s grade is \" + 100");
  }

  @Test public void stringFromBuilderNoStringComponents() {
    trimmingOf("new StringBuilder(0).append(1).toString()")//
        .gives("\"\" + 0 + 1");
  }
}