package rip.diavlo.base.events.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
  byte value() default 2;
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\events\api\EventTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */