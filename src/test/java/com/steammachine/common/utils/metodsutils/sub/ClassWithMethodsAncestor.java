package com.steammachine.common.utils.metodsutils.sub;

/**
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 */
public class ClassWithMethodsAncestor {

    private void privateMethodInherited() {
    }

    private void privateMethodWithParamInherited(int param) {
    }

    private String privateMethodThatReturnsInherited() {
        return "";
    }

    void defaultMethodInherited() {
    }

    String defaultMethodThatReturnsInherited() {
        return "";
    }

    void defaultMethodWithParamInherited(int param) {
    }

    protected void protectedMethodInherited() {
    }

    protected String protectedMethodThatReturnsInherited() {
        return "";
    }

    protected void protectedMethodWithParamInherited(int param) {
    }

    public void publicMethodInherited() {
    }

    public String publicMethodThatReturnsInherited() {
        return "";
    }

    public void publicMethodWithParamInherited(int param) {
    }


    private int[] privateMethodReturnsIntArray() {
        return null;
    }

    private static int[] privateStaticMethodReturnsIntArray() {
        return null;
    }

}
