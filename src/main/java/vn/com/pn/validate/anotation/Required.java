package vn.com.pn.validate.anotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Required {
    String displayFieldName() default "N/A";
}
