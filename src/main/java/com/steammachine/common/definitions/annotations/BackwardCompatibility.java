package com.steammachine.common.definitions.annotations;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotation - marker. Used for marking backward capability class members.
 * Usually these are only  stub, or deprecated class members.
 *
 *  The annotation is seen only in sourcecode
 * <p>
 *
 * @author Vladimir Bogodukhov
 */

@Target({
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.SOURCE)
@Api(State.MAINTAINED)
public @interface BackwardCompatibility {

    /**
     *
     * @return - Additional descriptions.
     */
    String[] value() default {};


}
