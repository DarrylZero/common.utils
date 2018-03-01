package com.steammachine.common.utils.metodsutils;

import com.steammachine.common.utils.metodsutils.sub.*;
import org.junit.Assert;
import org.junit.Test;
import com.steammachine.common.utils.metodsutils.sub.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * @author Vladimir Bogodukhov
 */
public class MethodUtilsCheck {


    @Test
    public void levelGetLevel() throws NoSuchMethodException {
        Class<?> cls = ClassWithMethods.class;
        Assert.assertEquals(
                MethodUtils.Level.PRIVATE,
                MethodUtils.getLevel(cls.getDeclaredMethod("privateMethod").getModifiers())
        );

        Assert.assertEquals(
                MethodUtils.Level.PROTECTED,
                MethodUtils.getLevel(cls.getDeclaredMethod("protectedMethod").getModifiers())
        );

        Assert.assertEquals(
                MethodUtils.Level.DEFAULT,
                MethodUtils.getLevel(cls.getDeclaredMethod("defaultMethod").getModifiers())
        );

        Assert.assertEquals(
                MethodUtils.Level.PUBLIC,
                MethodUtils.getLevel(cls.getDeclaredMethod("publicMethod").getModifiers())
        );
    }

    @Test
    public void callInspectClassCheck() {
        MethodUtils.Level level;
        level = MethodUtils.Level.PRIVATE;
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParamInherited", null, Integer.TYPE));

        level = MethodUtils.Level.PROTECTED;
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethod", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodInherited", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParamInherited", null, Integer.TYPE));

        level = MethodUtils.Level.DEFAULT;
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethod", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturns", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethod", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturns", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodInherited", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturnsInherited", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodInherited", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturnsInherited", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParamInherited", null, Integer.TYPE));

        level = MethodUtils.Level.PUBLIC;
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethod", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturns", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethod", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturns", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethod", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturns", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParam", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethod", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturns", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodInherited", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "privateMethodThatReturnsInherited", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodInherited", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodThatReturnsInherited", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "protectedMethodWithParam", null, Integer.TYPE));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodInherited", null));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodThatReturnsInherited", String.class));
        Assert.assertNull(MethodUtils.findMethod(level, ClassWithMethods.class, "defaultMethodWithParamInherited", null, Integer.TYPE));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodInherited", null));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodThatReturnsInherited", String.class));
        Assert.assertNotNull(MethodUtils.findMethod(level, ClassWithMethods.class, "publicMethodWithParamInherited", null, Integer.TYPE));

    }


    @Test
    public void callInspectClassCheck100() {
        Assert.assertEquals(Collections.emptyList(), MethodUtils.findMethods(ClassWithMethods.class, method -> false));
    }

    @Test
    public void callInspectClassCheck110() {
        List<String> list = Arrays.asList("privateMethod", "protectedMethod", "defaultMethod",
                "publicMethod", "privateMethodWithParam",
                "privateMethodThatReturns", "protectedMethodThatReturns",
                "protectedMethodWithParam", "defaultMethodThatReturns",
                "defaultMethodWithParam", "publicMethodThatReturns",
                "publicMethodWithParam", "privateMethodInherited",
                "privateMethodWithParamInherited", "privateMethodThatReturnsInherited",
                "protectedMethodInherited", "protectedMethodThatReturnsInherited",
                "defaultMethodInherited", "defaultMethodThatReturnsInherited",
                "defaultMethodWithParamInherited", "publicMethodInherited",
                "publicMethodThatReturnsInherited", "publicMethodWithParamInherited",
                "privateMethodReturnsIntArray", "privateStaticMethodReturnsIntArray",
                "protectedMethodWithParamInherited");

        Assert.assertTrue(MethodUtils.findMethods(ClassWithMethods.class, method -> method.getName().contains("Method")).
                stream().map(Method::getName).allMatch(list::contains));
    }


    @Test
    public void callInspectClassCheck120() {
        List<Method> list =
                MethodUtils.findMethods(ClassWithMethods.class, method -> method.getName().contains("private"));
        Assert.assertTrue(list.stream().map(Method::getName).allMatch(i -> i.contains("private")));
    }


    @Test
    public void arrrayReturned1() {
        Assert.assertNotNull(MethodUtils.findMethod(MethodUtils.Level.PRIVATE, ClassWithMethods.class,
                "privateMethodReturnsIntArray", int[].class));
    }

    @Test
    public void privateStaticMethodReturnsIntArray1() {
        Assert.assertNotNull(MethodUtils.findMethod(MethodUtils.Level.PRIVATE, ClassWithMethods.class,
                "privateStaticMethodReturnsIntArray", int[].class));
    }


    /* ----------------------------------------------- findConstructor --------------------------------------------- */

    @Test
    public void findConstructor1() {
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructorsAncestor.class, Long.TYPE));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructorsAncestor.class, String.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructorsAncestor.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructorsAncestor.class));

        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructorsAncestor.class, Long.TYPE));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructorsAncestor.class, String.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructorsAncestor.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructorsAncestor.class));

        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructorsAncestor.class, Long.TYPE));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructorsAncestor.class, String.class));
        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructorsAncestor.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructorsAncestor.class));

        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructorsAncestor.class, Long.TYPE));
        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructorsAncestor.class, String.class));
        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructorsAncestor.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructorsAncestor.class));
    }

    @Test
    public void findConstructor2() {
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructors.class, Long.TYPE));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructors.class, String.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructors.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PRIVATE, ClassWithConstructors.class));

        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructors.class, Long.TYPE));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructors.class, String.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructors.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PROTECTED, ClassWithConstructors.class));

        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructors.class, Long.TYPE));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructors.class, String.class));
        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructors.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.DEFAULT, ClassWithConstructors.class));

        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructors.class, Long.TYPE));
        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructors.class, String.class));
        Assert.assertNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructors.class, List.class));
        Assert.assertNotNull(MethodUtils.findConstructor(MethodUtils.Level.PUBLIC, ClassWithConstructors.class));
    }

    @Test
    public void findConstructors10() {
        Assert.assertNotNull(MethodUtils.findConstructors(ClassWithConstructors.class, clazz -> true));
        Assert.assertNotNull(MethodUtils.findConstructors(ClassWithConstructors.class, clazz -> false));
    }

/* ------------------------------------------------------- findFields ---------------------------------------------- */

    @Test
    public void findFields10() {
        Assert.assertEquals(
                new HashSet<Object>(Arrays.asList("privateField", "privateFinalField", "privateStaticField",
                        "privateStaticFinalField", "protectedField", "protectedFinalField", "protectedStaticField",
                        "protectedStaticFinalField", "defaultField", "defaultFinalField", "defaultStaticField",
                        "defaultStaticFinalField", "publicField", "publicFinalField", "publictStaticField",
                        "PUBLICSTATICFINALFIELD"))
                ,
                new HashSet<Object>(Arrays.asList(MethodUtils.findFields(ClassWithFields.class, field -> true).stream().
                        map(Field::getName).toArray()))
        );
    }

    @Test
    public void findFields20() {
        Assert.assertEquals(
                new HashSet<Object>(Arrays.asList("privateFieldInherited", "protectedFieldInherited", "defaultFinalField",
                        "defaultStaticFinalFieldInherited", "publictStaticField", "defaultFinalFieldInherited",
                        "privateStaticField", "PUBLICSTATICFINALFIELD", "publicFieldInherited", "defaultStaticField",
                        "privateFinalField", "protectedStaticFinalField", "defaultField", "defaultStaticFinalField",
                        "publicFinalField", "protectedFinalFieldInherited", "privateStaticFinalFieldInherited",
                        "protectedStaticFinalFieldInherited", "protectedField", "publicField",
                        "privateStaticFieldInherited", "protectedStaticField", "defaultStaticFieldInherited",
                        "protectedFinalField", "publictStaticFieldInherited", "privateField",
                        "protectedStaticFieldInherited", "PUBLICSTATICFINALFIELDINHERITED", "publicFinalFieldInherited",
                        "privateStaticFinalField", "privateFinalFieldInherited", "defaultFieldInherited")),

                new HashSet<Object>(Arrays.asList(MethodUtils.findFields(ClassWithFieldsAncestor.class, field -> true).
                        stream().map(Field::getName).toArray()))
        );
    }

    @Test
    public void findFieldsPrivates() {
        Assert.assertEquals(
                new HashSet<Object>(Arrays.asList("privateField", "privateStaticField", "privateStaticFinalField",
                        "privateFinalField")),

                new HashSet<Object>(Arrays.asList(MethodUtils.findFields(ClassWithFields.class,
                        field -> Modifier.isPrivate(field.getModifiers())).stream().map(Field::getName).toArray()))
        );
    }

    @Test
    public void findFieldsPublics() {
        Assert.assertEquals(
                new HashSet<Object>(Arrays.asList("publicFinalField", "PUBLICSTATICFINALFIELD", "publicField",
                        "publictStaticField")),

                new HashSet<Object>(Arrays.asList(MethodUtils.findFields(ClassWithFields.class,
                        field -> Modifier.isPublic(field.getModifiers())).stream().map(Field::getName).toArray()))
        );
    }

    @Test
    public void findFieldsProtected() {
        Assert.assertEquals(
                new HashSet<Object>(Arrays.asList("publicFinalField", "PUBLICSTATICFINALFIELD", "publicField",
                        "publictStaticField")),
                new HashSet<Object>(Arrays.asList(MethodUtils.findFields(ClassWithFields.class,
                        field -> Modifier.isPublic(field.getModifiers())).stream().map(Field::getName).toArray()))
        );
    }

    @Test
    public void findFieldsFinals() {
        Assert.assertEquals(
                new HashSet<Object>(Arrays.asList("protectedStaticFinalField", "defaultStaticFinalField",
                        "publicFinalField", "PUBLICSTATICFINALFIELD", "privateStaticFinalField", "defaultFinalField",
                        "privateFinalField", "protectedFinalField")),

                new HashSet<Object>(Arrays.asList(MethodUtils.findFields(ClassWithFields.class,
                        field -> Modifier.isFinal(field.getModifiers())).stream().map(Field::getName).toArray()))
        );
    }

    @Test
    public void findFieldsStatics() {
        Assert.assertEquals(
                new HashSet<Object>(Arrays.asList("protectedStaticFinalField", "defaultStaticFinalField",
                        "privateStaticField", "PUBLICSTATICFINALFIELD", "privateStaticFinalField", "defaultStaticField",
                        "publictStaticField", "protectedStaticField")),

                new HashSet<Object>(Arrays.asList(MethodUtils.findFields(ClassWithFields.class,
                        field -> Modifier.isStatic(field.getModifiers())).stream().map(Field::getName).toArray()))
        );
    }


}






