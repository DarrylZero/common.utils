package com.steammachine.common.utils.metodsutils.sub;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov 
 **/
public class ClassWithFieldsAncestor extends ClassWithFields {

    private String privateFieldInherited;
    private final String privateFinalFieldInherited = "";
    private static String privateStaticFieldInherited;
    private static final String privateStaticFinalFieldInherited = "";

    protected String protectedFieldInherited;
    protected final String protectedFinalFieldInherited = "";
    protected static String protectedStaticFieldInherited;
    protected static final String protectedStaticFinalFieldInherited = "";

    String defaultFieldInherited;
    final String defaultFinalFieldInherited = "";
    static String defaultStaticFieldInherited;
    static final String  defaultStaticFinalFieldInherited = "";

    public String publicFieldInherited;
    public final String publicFinalFieldInherited = "";
    public static String publictStaticFieldInherited;
    public static final String PUBLICSTATICFINALFIELDINHERITED = "";

}
