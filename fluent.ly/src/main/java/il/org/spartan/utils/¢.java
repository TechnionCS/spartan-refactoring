package il.org.spartan.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Short name of a JohnDoe parameter
 *
 * @author Yossi Gil
 * @since 2017-03-13
 */
@Documented @Target(ElementType.PARAMETER) public @interface ¢ {
  /** Add here brief documentation if you like; return __ may change though */
  String value() default "J(ohn)|(ane) Doe";
}
