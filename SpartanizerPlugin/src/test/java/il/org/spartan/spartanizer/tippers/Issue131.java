package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** {@link $BodyDeclarationModifiersPrune}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public final class Issue131 {
  @Test public void A$010() {
    trimmingOf("for(int i=4; i<s.length() ; ++i){i+=9;return x;}return x;")
        .gives("for(int ¢=4; ¢<s.length() ; ++¢){¢+=9;break;}return x;").stays();
  }

  @Test public void A$020() {
    trimmingOf("while(i>9)if(i==5)return x;return x;").gives("while(i>9)if(i==5)break;return x;").stays();
  }

  @Test public void A$030() {
    trimmingOf("for(int ¢=4 ; ¢<s.length() ; ++¢)return x;return x;")
        .gives("for(int ¢=4 ; ¢<s.length() ; ++¢)break;return x;").stays();
  }

  @Test public void A$040() {
    trimmingOf("for(int ¢=4 ; ¢<s.length() ; ++¢)if(tipper=4)return x;return x;")
        .gives("for(int ¢=4 ; ¢<s.length() ; ++¢)if(tipper=4)break;return x;").stays();
  }

  @Test public void A$050() {
    trimmingOf("while(i>5){i+=9;i++;return x;}return x;").gives("while(i>5){i+=9;++i;break;}return x;").stays();
  }

  @Test public void A$060() {
    trimmingOf("while(i>5){i+=9;return x;}return x;").gives("while(i>5){i+=9;break;}return x;").stays();
  }

  @Test public void A$070() {
    trimmingOf("while(i>5)return x;return x;").gives("while(i>5)break;return x;").stays();
  }

  @Test public void A$080() {
    trimmingOf("while(i>5)if(tipper=4)return x;return x;").gives("while(i>5)if(tipper=4)break;return x;").stays();
  }

  @Test public void A$090() {
    trimmingOf("for(int i=4 ; i<s.length() ; ++i)if(i==5)return x;return x;")
        .gives("for(int ¢=4 ; ¢<s.length() ; ++¢)if(¢==5)break;return x;").stays();
  }

  @Test public void A$100() {
    trimmingOf("for(int i=4;i<s.length();++i){i+=9;i++;return x;}return x;")
        .gives("for(int ¢=4;¢<s.length();++¢){¢+=9;¢++;break;}return x;")
        .gives("for(int ¢=4;¢<s.length();++¢){¢+=9;++¢;break;}return x;").stays();
  }

  @Test public void A$110() {
    trimmingOf("int tipper=5;int i=2;for(int i=4;i<s.length();++i){if(i==5){tipper+=9;return x;}y+=15;return x;}return x;")
        .gives("int tipper=5;int i=2;for(int ¢=4;¢<s.length();++¢){if(¢==5){tipper+=9;return x;}y+=15;break;}return x;")
        .gives("int tipper=5;int i=2;for(int ¢=4;¢<s.length();++¢){if(¢==5){tipper+=9; break;}y+=15;break;}return x;").stays();
  }

  @Test public void A$120() {
    trimmingOf("boolean b=false;for(int i=4;i<s.length();++i){if(i==5){tipper+=9;return x;}else return tr;y+=15;return x;}return x;")
        .gives("boolean b=false;for(int ¢=4;¢<s.length();++¢){if(¢==5){tipper+=9;return x;}else return tr;y+=15;break;}return x;")
        .gives("boolean b=false;for(int ¢=4;¢<s.length();++¢){if(¢!=5)return tr;tipper+=9;return x;y+=15;break;}return x;")
        .gives("boolean b=false;for(int ¢=4;¢<s.length();++¢){if(¢!=5)return tr;tipper+=9;break;y+=15;break;}return x;").stays();
  }

  @Test public void A$130() {
    trimmingOf("int i=1;while(i<7){if(i==5){tipper+=9;return x;}y+=15;return x;}return x;")
        .gives("int i=1;while(i<7){if(i==5){tipper+=9;return x;}y+=15;break;}return x;")
        .gives("int i=1;while(i<7){if(i==5){tipper+=9;break;}y+=15;break;}return x;").stays();
  }

  @Test public void A$140() {
    trimmingOf("public static void main(){while(i<7){if(i==5){tipper+=9;return x;}else return tr;y+=15;return x;}return x;}")
        .gives("public static void main(){while(i<7){if(i!=5)return tr;tipper+=9;return x;y+=15;return x;}return x;}")
        .gives("public static void main(){while(i<7){if(i!=5)return tr;tipper+=9;break;y+=15;return x;}return x;}")
        .gives("public static void main(){while(i<7){if(i!=5)return tr;tipper+=9;break;y+=15;break;}return x;}").stays();
  }

  @Test public void A$150() {
    trimmingOf("  private int free(int max) {" + "int $ = 0;" + "for (; $ < max; ++$) {" + "Entry<T> entry = queue.poll();" + "if (entry == null)"
        + "return $;" + "freeEntry(entry);" + "}" + "return $;" + "}")
            .gives("  private int free(int max) {" + "int $ = 0;" + "for (; $ < max; ++$) {" + "Entry<T> entry = queue.poll();" + "if (entry == null)"
                + "break;" + "freeEntry(entry);" + "}" + "return $;" + "}");
  }
}
