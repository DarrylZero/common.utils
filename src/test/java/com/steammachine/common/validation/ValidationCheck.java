package com.steammachine.common.validation;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * @author Vladimir Bogodukhov
 */
public class ValidationCheck {


/* ------------------------------------------------ name integrity ------------------------------------------------- */

    @Test
    public void nameIntegrity10() {
        Assert.assertEquals("com.steammachine.common.validation.Validation", Validation.class.getName());
    }

/* ------------------------------------------------ notEmpty string ------------------------------------------------ */

    @Test
    public void notEmptyString10() {
        Validation.notEmpty("Hola mundo !");
    }

    @Test(expected = ValidationFailed.class)
    public void notEmptyString20() {
        Validation.notEmpty((String) null);
    }

    @Test(expected = ValidationFailed.class)
    public void notEmptyString30() {
        Validation.notEmpty("");
    }

    @Test
    public void notEmptyString40() {
        Validation.notEmpty("        ");
    }

/* ------------------------------------------------ notEmpty Collection --------------------------------------------- */

    @Test
    public void notEmptyCollection10() {
        Validation.notEmpty(Arrays.asList("1"));
    }

    @Test
    public void notEmptyCollection20() {
        Validation.notEmpty(Arrays.asList("1", "2"));
    }

    @Test(expected = ValidationFailed.class)
    public void notEmptyCollection30() {
        Validation.notEmpty((Collection<Object>) null);
    }

    @Test(expected = ValidationFailed.class)
    public void notEmptyCollection40() {
        Validation.notEmpty(Collections.emptyList());
    }

/* ------------------------------------------------ notEmpty Map --------------------------------------------- */

    @Test
    public void notEmptyMap10() {
        Validation.notEmpty(new HashMap<String, Integer>() {
            {
                put("1", 2);
                put("2", 2);
            }
        });
    }

    @Test(expected = ValidationFailed.class)
    public void notEmptyMap20() {
        Validation.notEmpty((Map<Object, Object>) null);
    }

    @Test(expected = ValidationFailed.class)
    public void notEmptyMap30() {
        Validation.notEmpty(new HashMap<String, Integer>());
    }

}