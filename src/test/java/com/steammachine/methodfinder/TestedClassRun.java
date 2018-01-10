package com.steammachine.methodfinder;

import org.junit.Test;
import com.steammachine.common.utils.commonutils.CommonUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 **/
class TestedClassRun {

    public static final int MAX_METHODS = 1000;


    /**
     *
     *
     *
     */
    @Test
    void generate() throws IOException {
        String path = CommonUtils.getAbsoluteResourcePath(TestedClassRun.class, "TestedClass3.java");
        Path dest = Paths.get(path);
        Files.deleteIfExists(dest);
        Files.createFile(dest);
        try (PrintStream stream = new PrintStream(new FileOutputStream(path, true))) {
            stream.println("package " + TestedClassRun.class.getPackage().getName() + ";");
            stream.println();
            stream.println();
            stream.println("import java.lang.annotation.ElementType;");
            stream.println("import java.lang.annotation.Retention;");
            stream.println("import java.lang.annotation.RetentionPolicy;");
            stream.println("import java.lang.annotation.Target;");
            stream.println("import java.util.function.Consumer;");
            stream.println("/**");
            stream.println("* @author Vladimir Bogodukhov");
            stream.println("**/");
            stream.println("public class TestedClass3 {");
            stream.println();
            stream.println("    @Target({ElementType.METHOD})");
            stream.println("    @Retention(RetentionPolicy.RUNTIME)");
            stream.println("    public @interface Mark {");
            stream.println("    }");
            stream.println();
            stream.println("    private final Consumer<Object> consumer;");
            stream.println();
            stream.println("    public TestedClass3(Consumer<Object> consumer) {");
            stream.println("        this.consumer = consumer;");
            stream.println("    }");
            stream.println();

            stream.println("    @Mark");
            stream.println("    private void test() {");
            stream.println("        consumer.accept(null);");
            stream.println("    }");
            outputMethods(stream);
            stream.println("}");
            stream.flush();

//            System.out.println("" + path + ":0:");
        }
    }


    private static void outputMethods(PrintStream stream) {
        stream.println();
        stream.println("    private void test0() {");
        stream.println("        test();");
        stream.println("    }");

        for (int i = 0; i < MAX_METHODS; i++) {
            stream.println();
            outputMethodData(stream, i);
            stream.println();
        }
        stream.println();
        stream.println();
        outputExecMethod(stream, MAX_METHODS);
        stream.println();
        stream.println();
    }

    private static void outputExecMethod(PrintStream stream, int number) {
        stream.println("      public void execute() {");
        stream.println("        test" + number + "();          ");
        stream.println("      }");
    }

    private static void outputMethodData(PrintStream stream, int num) {
        stream.println("    private void test" + (num + 1) + "() {");
        stream.println("        test" + num + "();");
        stream.println("    }");
    }

}
