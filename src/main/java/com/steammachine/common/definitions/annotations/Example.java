package com.steammachine.common.definitions.annotations;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotation - marker which tells that the annotate code can be used as example
 * The annotation is seen only in sourcecode.
 * <p>
 *
 * @author Vladimir Bogodukhov
 */

@Target({
        ElementType.LOCAL_VARIABLE,
        ElementType.TYPE,
        ElementType.PARAMETER,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.SOURCE)
@Api(State.MAINTAINED)
public @interface Example {

    /**
     * @return - Additional descriptions.
     */
    String[] value() default {};


}
