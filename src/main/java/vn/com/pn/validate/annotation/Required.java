package vn.com.pn.validate.annotation;

import java.lang.annotation.*;
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Required {
    String displayFieldName() default "N/A";
}
