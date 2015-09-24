package org.spartan.refactoring.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.spartan.hamcrest.CoreMatchers.is;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class JavaTypeNameParserTest {
  @Test public void alphaNumericMid() {
    assertThat(new JavaTypeNameParser("Base64Parser").shortName(), is("bp"));
  }
  @Test public void alphaNumericPost() {
    assertThat(new JavaTypeNameParser("Int32").shortName(), is("i"));
  }
  @Test public void astNode() {
    assertThat(new JavaTypeNameParser("ASTNode").shortName(), is("n"));
  }
  @Test public void httpSecureConnection() {
    assertThat(new JavaTypeNameParser("HTTPSecureConnection").shortName(), is("c"));
  }
  @Test public void infixExpression() {
    assertThat(new JavaTypeNameParser("InfixExpression").shortName(), is("ie"));
  }
  @Test public void jUnit() {
    assertThat(new JavaTypeNameParser("JUnit").shortName(), is("ju"));
  }
  @Test public void onlyLowerCase() {
    assertThat(new JavaTypeNameParser("onlylowercase").shortName(), is("o"));
  }
  @Test public void onlyUpperCase() {
    assertThat(new JavaTypeNameParser("ONLYUPPERCASE").shortName(), is("o"));
  }
  @Test public void singleChar() {
    assertThat(new JavaTypeNameParser("Q").shortName(), is("q"));
  }
}
