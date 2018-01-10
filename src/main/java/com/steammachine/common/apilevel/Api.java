package com.steammachine.common.apilevel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


/**
 * Annotation for Api level
 */
@Retention(RetentionPolicy.SOURCE)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, ANNOTATION_TYPE})
@Api(State.MAINTAINED)
public @interface Api {

    /**
     * @return api level
     */
    State value();
}
