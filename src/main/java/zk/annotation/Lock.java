package zk.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Lock {
    long expire();

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    boolean sync() default false;

    String key() default "getLock";
}
