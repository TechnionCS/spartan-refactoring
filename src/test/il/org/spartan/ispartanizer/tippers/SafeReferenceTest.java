package il.org.spartan.ispartanizer.tippers;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.PsiTestCase;
import il.org.spartan.ispartanizer.plugin.tippers.DefaultsTo;
import il.org.spartan.ispartanizer.plugin.tippers.SafeReference;
import il.org.spartan.ispartanizer.tippers.TipperTest;

/**
 * Created by amirsagiv on 12/24/16.
 */
public class SafeReferenceTest extends TipperTest {

    public void testCanTipFirstScenarioLegal() {
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y")));
    }

    public void testCanTipSecondScenarioLegal() {
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("null == x ? null : x.y")));
    }

    public void testCanTipThirdScenarioLegal() {
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y : null")));
    }

    public void testCanTipFourthScenarioLegal() {
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("null != x ? x.y : null")));
    }

    public void testCanTipIllegal1() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x == null ? null : null")));
    }

    public void testCanTipIllegal2() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x == null ? x.y : null")));

    }

    public void testCanTipIllegal3() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x != null ? null : x.y")));
    }

    public void testCanTipIllegal4() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x != null ? null : null")));
    }

    public void testCanTipIllegal5() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("y == null ? null : x.y")));
    }

    public void testCanTipIllegal6() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("y != null ? x.y: null")));
    }

    public void testCanTipIllegal7() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x >= null ? x.y: null")));
    }

    public void testCanTipIllegal8() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("null < x ? x.y: null")));
    }

    public void testCanTipIllegal9() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x == null ? null: a.y")));
    }

    public void testCanTipIllegal10() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x != null ? a.y: null")));
    }

    public void testCanTipIllegal11() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("null == x ? null: a.y")));
    }

    public void testCanTipIllegal12() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("null != x ? a.y: null")));
    }

    public void testFirstScenarioWithLegalMethod(){
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y()")));
    }

    public void testSecondScenarioWithLegalMethod(){
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("null == x ? null : x.y()")));
    }

    public void testThirdScenarioWithLegalMethod(){
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y() : null")));
    }

    public void testFourthScenarioWithLegalMethod(){
        assertTrue(new SafeReference().canTip(createTestExpressionFromString("null != x ? x.y() : null")));
    }

    //Notice!! these next tests only pass because I limited the pattern to work with methods with zero params. delete
    // if you make it work with any method!
    public void testCanTipIllegal13() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y(p1)")));
    }

    public void testCanTipIllegal14() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x == null ? null : x.y(p1,p2)")));
    }

    public void testCanTipIllegal15() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y(p1) : null")));
    }

    public void testCanTipIllegal16() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y(p1 ,p2) : null")));
    }

    public void testCanTipIllegal17() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x == null ? x.y() : x.y(p1)")));
    }

    public void testCanTipIllegal18() {
        assertFalse(new SafeReference().canTip(createTestExpressionFromString("x != null ? x.y(p1 ,p2) : x.y")));
    }




}
