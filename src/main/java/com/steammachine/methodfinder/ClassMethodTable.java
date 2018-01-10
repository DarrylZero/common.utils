package com.steammachine.methodfinder;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 *         <p>
 *         <p>
 */
public class ClassMethodTable {

    private static final String ILLEGAL_METHOD_DESCRIPTOR = "illegal method descriptor";

    /**
     * Вспомогательный интерфейс обозначающий данные
     */
    public interface SourceCodePosition {

        /**
         * @return минимальная линия кода в исходнике.
         */
        int minLine();

        /**
         * @return максимальная линия кода в исходнике.
         */
        int maxLine();

        /**
         * @return Порядок следования метода.
         */
        int order();

        /**
         * @return Признак того, что метод имеет координаты в исходном коде.
         */
        boolean hasPosition();


        boolean inlinerange(int line);

    }

    public static class DefaultSourceCodePosition implements SourceCodePosition {
        private final int minLine;
        private final int maxLine;
        private final int order;
        private final boolean hasPosition;

        public DefaultSourceCodePosition(int minLine, int maxLine, int order, boolean hasPosition) {
            this.minLine = minLine;
            this.maxLine = maxLine;
            this.order = order;
            this.hasPosition = hasPosition;
        }

        public DefaultSourceCodePosition(int order) {
            this.minLine = -1;
            this.maxLine = -1;
            this.order = order;
            this.hasPosition = false;
        }

        public DefaultSourceCodePosition(int minLine, int maxLine, int order) {
            this(minLine, maxLine, order, true);
        }

        @Override
        public int minLine() {
            return minLine;
        }

        @Override
        public int maxLine() {
            return maxLine;
        }

        @Override
        public int order() {
            return order;
        }

        @Override
        public boolean hasPosition() {
            return hasPosition;
        }


        public boolean inlinerange(int line) {
            return hasPosition() && line >= minLine() && line <= maxLine();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DefaultSourceCodePosition)) return false;

            DefaultSourceCodePosition that = (DefaultSourceCodePosition) o;

            if (minLine != that.minLine) return false;
            if (maxLine != that.maxLine) return false;
            if (order != that.order) return false;
            return hasPosition == that.hasPosition;
        }

        @Override
        public int hashCode() {
            int result = minLine;
            result = 31 * result + maxLine;
            result = 31 * result + order;
            result = 31 * result + (hasPosition ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "DefaultSourceCodePosition{" +
                    "minLine=" + minLine +
                    ", maxLine=" + maxLine +
                    ", order=" + order +
                    ", hasPosition=" + hasPosition +
                    '}';
        }
    }


    /**
     * Константы классовых элементов
     */
    @SuppressWarnings("WeakerAccess")
    private static class CCIC {
        public static final int CLASS_MAGIC_NUMBER = 0xCAFEBABE;
        public static final byte CONSTANT_UTF8 = 1;
        public static final byte CONSTANT_INTEGER = 3;
        public static final byte CONSTANT_FLOAT = 4;
        public static final byte CONSTANT_LONG = 5;
        public static final byte CONSTANT_DOUBLE = 6;
        public static final byte CONSTANT_CLASS = 7;
        public static final byte CONSTANT_STRING = 8;
        public static final byte CONSTANT_FIELDREF = 9;
        public static final byte CONSTANT_METHODREF = 10;
        public static final byte CONSTANT_INTERFACEMETHODREF = 11;
        public static final byte CONSTANT_NAMEANDTYPE = 12;
        public static final byte CONSTANT_METHODHANDLE = 15;
        public static final byte CONSTANT_METHODTYPE = 16;
        public static final byte CONSTANT_INVOKEDYNAMIC = 18;
    }


    private static class Const {
        private final byte tag;

        private Const(byte tag) {
            this.tag = tag;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class CONSTANT_METHODREF_Info extends Const {
        private final int class_index;
        private final int name_and_type_index;

        private CONSTANT_METHODREF_Info(byte tag, int class_index, int name_and_type_index) {
            super(tag);
            this.class_index = class_index;
            this.name_and_type_index = name_and_type_index;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class CONSTANT_INTERFACEMETHODREF_Info extends Const {
        private final int class_index;
        private final int name_and_type_index;

        private CONSTANT_INTERFACEMETHODREF_Info(byte tag, int class_index, int name_and_type_index) {
            super(tag);
            this.class_index = class_index;
            this.name_and_type_index = name_and_type_index;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class Constant_String_Info extends Const {
        private final int string_index;


        private Constant_String_Info(byte tag, int string_index) {
            super(tag);
            this.string_index = string_index;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class CONSTANT_NAMEANDTYPE_Info extends Const {
        private final int name_index;
        private final int descriptor_index;

        private CONSTANT_NAMEANDTYPE_Info(byte tag, int name_index, int descriptor_index) {
            super(tag);
            this.name_index = name_index;
            this.descriptor_index = descriptor_index;
        }
    }

    private static class Constant_Utf8_Info extends Const {
        private final String data;

        private Constant_Utf8_Info(byte tag, String data) {
            super(tag);
            this.data = data;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class CONSTANT_METHODHANDLE_Info extends Const {
        private final int reference_kind;
        private final int reference_index;

        private CONSTANT_METHODHANDLE_Info(byte tag, int reference_kind, int reference_index) {
            super(tag);
            this.reference_kind = reference_kind;
            this.reference_index = reference_index;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class CONSTANT_METHODTYPE_Info extends Const {
        private final int descriptor_index;

        private CONSTANT_METHODTYPE_Info(byte tag, int descriptor_index) {
            super(tag);
            this.descriptor_index = descriptor_index;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class Constant_InvokeDynamic extends Const {
        private final int bootstrap_method_attr_index;
        private final int name_and_type_index;

        private Constant_InvokeDynamic(byte tag, int bootstrap_method_attr_index, int name_and_type_index) {
            super(tag);
            this.bootstrap_method_attr_index = bootstrap_method_attr_index;
            this.name_and_type_index = name_and_type_index;
        }
    }


    /**
     * Используется для тех случаев, когда в коде метода нет данных о том где он расположен в исходнике.
     */
    private static final SourceCodePosition NO_POSITION = new DefaultSourceCodePosition(-1);


    /**
     * прочитать данные о расположении методов из класса
     *
     * @param data поток с данными класса
     * @return данные о расположении методов из класса
     * @throws IOException
     */
    public static Map<String, SourceCodePosition> readClassFromInputStream(InputStream data) throws IOException {
        return readClassFromDataInputStream(new DataInputStream(data));
    }

    /**
     * представить информацию о методе в виде
     *
     * @param method
     * @return
     */
    public static String methodSignature(Method method) {
        Objects.requireNonNull(method);


        /* Возможно этот метод тут быть не должен method.getParameters() */
        StringBuilder builder = new StringBuilder();

        builder.append(method.getReturnType().getTypeName());
        builder.append(" ");
        builder.append(method.getName());
        builder.append("(");

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {

            builder.append(parameterTypes[i].getTypeName());
            if (i < parameterTypes.length - 1) {
                builder.append(",");
            }
        }
        builder.append(")");
        return builder.toString();
    }


/* ----------------------------------------------- protected ------------------------------------------------------ */

    /**
     * @param data - datastream to read from
     * @return - parsed class structure
     * @throws IOException
     */
    protected static Map<String, SourceCodePosition> readClassFromDataInputStream(DataInputStream data) throws IOException {
        if (data.readInt() != CCIC.CLASS_MAGIC_NUMBER) {
            throw new IllegalStateException(" is not a Java .class file");
        }

        Map<String, SourceCodePosition> result = new HashMap<String, SourceCodePosition>();

        data.readUnsignedShort(); /* minor */
        data.readUnsignedShort(); /* major */

        /*Read constant pool entries*/
        int constCount = data.readUnsignedShort();
        Const[] c = new Const[constCount];

        for (int i = 1; i < constCount; i++) {
            byte tag = data.readByte();  /*Read tag byte*/

            switch (tag) {
                case CCIC.CONSTANT_CLASS: {
                    /*  c[i] = new Constant_Class_Info(tag, data.readUnsignedShort()); */
                    data.readUnsignedShort();
                    break;
                }

                case CCIC.CONSTANT_FIELDREF: {
/*
                     c[i] = new Constant_FieldRef_Info(tag, data.readUnsignedShort(), data.readUnsignedShort());
                     u2 class_index; u2 name_and_type_index;
*/
                    skipBytes(data, 4);
                    break;
                }

                case CCIC.CONSTANT_METHODREF: {
                    c[i] = new CONSTANT_METHODREF_Info(tag, data.readUnsignedShort(), data.readUnsignedShort());
                    // u2 class_index; u2 name_and_type_index;
                    break;
                }

                case CCIC.CONSTANT_INTERFACEMETHODREF: {
                    c[i] = new CONSTANT_INTERFACEMETHODREF_Info(tag, data.readUnsignedShort(),
                            data.readUnsignedShort());
                    // u2 class_index; u2 name_and_type_index;
                    break;
                }

                case CCIC.CONSTANT_STRING: {
                    c[i] = new Constant_String_Info(tag, data.readUnsignedShort());
                    // u2 string_index;
                    break;
                }

                case CCIC.CONSTANT_INTEGER: {
                    // c[i] = new Constant_Integer_Info(tag, data.readInt());
                    // u4 bytes;
                    skipBytes(data, 4);
                    break;
                }

                case CCIC.CONSTANT_FLOAT: {
//                    c[i] = new Constant_Float_Info(tag, data.readFloat());
                    // u4 bytes;
                    skipBytes(data, 4);
                    break;
                }

                case CCIC.CONSTANT_LONG: {
//                    c[i] = new Constant_Long_Info(tag, data.readLong());
//                    c[i + 1] = c[i];
                    i++;
                    // u4 high_bytes;
                    // u4 low_bytes;

                    // All 8-byte constants take up two entries in the Constant_pool table of the class file.
                    // If a Constant_Long_info or Constant_Double_info structure is the item in the Constant_pool
                    // table at index n, then the next usable item in the pool is located at index n+2.
                    // The Constant_pool index n+1 must be valid but is considered unusable.

                    skipBytes(data, 8);

                    break;
                }

                case CCIC.CONSTANT_DOUBLE: {
//                    c[i] = new Constant_Double_Info(tag, data.readDouble());
//                    c[i + 1] = c[i];
                    i++;

                    // u4 high_bytes;
                    // u4 low_bytes;

                    // All 8-byte constants take up two entries in the Constant_pool table of the class file.
                    // If a Constant_Long_info or Constant_Double_info structure is the item in the Constant_pool
                    // table at index n, then the next usable item in the pool is located at index n+2.
                    // The Constant_pool index n+1 must be valid but is considered unusable.

                    skipBytes(data, 8);
                    break;
                }

                case CCIC.CONSTANT_NAMEANDTYPE: {
                    c[i] = new CONSTANT_NAMEANDTYPE_Info(tag, data.readUnsignedShort(), data.readUnsignedShort());
                    // u2 name_index , u2 descriptor_index;
                    break;
                }

                case CCIC.CONSTANT_UTF8: {
                    c[i] = new Constant_Utf8_Info(tag, data.readUTF());
                    // u2 length; u1 bytes[length];
                    break;
                }


                case CCIC.CONSTANT_METHODHANDLE: {
                    c[i] = new CONSTANT_METHODHANDLE_Info(tag, data.readUnsignedByte(),
                            data.readUnsignedShort());
                    // u1 reference_kind; u2 reference_index;
                    break;
                }

                case CCIC.CONSTANT_METHODTYPE: {
                    c[i] = new CONSTANT_METHODTYPE_Info(tag, data.readUnsignedShort());
                    // u2 descriptor_index;
                    break;
                }

                case CCIC.CONSTANT_INVOKEDYNAMIC: {
//                    c[i] = new Constant_InvokeDynamic(tag, data.readUnsignedShort(), data.readUnsignedShort());
                    // u2 bootstrap_method_attr_index u2 name_and_type_index;
                    skipBytes(data, 4);
                    break;
                }

                default: {
                    throw new IllegalStateException("Invalid byte tag in constant pool: " + tag);
                }
            }
        }

        skipBytes(data, 2); /* access_flags  */
        data.readUnsignedShort(); /* this_class  */
        data.readUnsignedShort(); /* super_class */

        int interfaceCount = data.readUnsignedShort(); //  interface count
        for (int i = 0; i < interfaceCount; i++) {
            data.readUnsignedShort();// Read interface index
        }
        int fieldCount = data.readUnsignedShort();       // fieldCount
        for (int i = 0; i < fieldCount; i++) {
            data.readUnsignedShort(); // access_flags;
            data.readUnsignedShort(); // u2  name_index;
            data.readUnsignedShort(); // u2  descriptor_index;

            int attributesCount = data.readUnsignedShort(); // u2  attributes_count;
            for (int j = 0; j < attributesCount; j++) {
                data.readUnsignedShort();// u2 attribute_name_index;
                skipAttribute(data);
            }
        }

        int methodCount = data.readUnsignedShort();//u2  methods_count;
        for (int i = 0; i < methodCount; i++) {
            data.readUnsignedShort();// u2 access_flags

            String methodName = stringDataInfo(data.readUnsignedShort(), c); /* u2             name_index;        */
            String dataInfo = stringDataInfo(data.readUnsignedShort(), c);   /* u2             descriptor_index;  */
            String methodSignature = parseMethodDescriptor(methodName, dataInfo);

            SourceCodePosition position = NO_POSITION;
            int attributesCount = data.readUnsignedShort(); /*u2             attributes_count; */
            for (int j = 0; j < attributesCount; j++) {
                String attributeName = stringDataInfo(data.readUnsignedShort(), c);
                if ("Code".equals(attributeName)) {
                    position = readCodeAttribute(data, i, c);
                } else {
                    skipAttribute(data);
                }
            }
            result.put(methodSignature, position);
        }


        int attributesCount = data.readUnsignedShort(); /*u2 attributes_count;*/
        for (int j = 0; j < attributesCount; j++) {
            stringDataInfo(data.readUnsignedShort(), c);/*attributeName*/
            skipAttribute(data);
        }

        return result;
    }

    /* ----------------------------------------------- privates --------------------------------------------------------- */

    private static void skipBytes(DataInputStream data, int attribute_length) throws IOException {
        while (attribute_length > 0) {
            int skippedBytes = data.skipBytes(attribute_length);
            attribute_length = attribute_length - skippedBytes;
        }
    }


    private static String stringDataInfo(int constantIndex, Const[] consts) {
        if (constantIndex < 0 || constantIndex >= consts.length) {
            throw new IllegalStateException("illegal interface index : " + constantIndex);
        }
        if (consts[constantIndex] == null) {
            throw new IllegalStateException("consts[" + constantIndex + "] == null");
        }
        if (consts[constantIndex].tag != CCIC.CONSTANT_UTF8) {
            throw new IllegalStateException("");
        }
        Constant_Utf8_Info utf8_info = (Constant_Utf8_Info) consts[constantIndex];
        return utf8_info.data;
    }

    private static SourceCodePosition readCodeAttribute(
            DataInputStream data, final int order, Const[] c) throws IOException {
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        skipBytes(data, 4); // u4 attribute_length;
        skipBytes(data, 2); // u2 max_stack;
        skipBytes(data, 2); // u2 max_locals;
        int code_length = data.readInt();//* u4 code_length;
        byte[] codeBytes = new byte[code_length];
        data.readFully(codeBytes);//* u1 code[code_length];
        int exception_table_length = data.readUnsignedShort();//* u2 exception_table_length;
        for (int i = 0; i < exception_table_length; i++) {
            skipBytes(data, 2); // u2 start_pc;
            skipBytes(data, 2); // u2 end_pc;
            skipBytes(data, 2); // u2 handler_pc;
            skipBytes(data, 2); // u2 catch_type;
        }
        SourceCodePosition result = NO_POSITION;
        int attributes_count = data.readUnsignedShort(); //        * u2 attributes_count;
        for (int j = 0; j < attributes_count; j++) {
            String attributeName = stringDataInfo(data.readUnsignedShort(), c); // u2 attribute_name_index;
            if ("LineNumberTable".equals(attributeName)) {
                result = readCodeOrder(data, order);
            } else {
                skipAttribute(data);
            }
        }
        return result;
    }

    private static SourceCodePosition readCodeOrder(DataInputStream data, final int order) throws IOException {
        skipBytes(data, 4); /*u4 attribute_length;*/
        int line_number_table_length = data.readUnsignedShort(); /*u2 line_number_table_length;*/


        /* перебираем все элементы и получаем минимальное значение строки для данного метода */


        Integer minLine = null;
        Integer maxLine = null;
        for (int k = 0; k < line_number_table_length; k++) {
            data.readUnsignedShort(); /*u2 start_pc;*/
            final int lineNumber = data.readUnsignedShort(); /*u2 line_number;*/
            skipBytes(data, 0);

            {
                if (minLine == null) {
                    minLine = lineNumber;
                }
                if (maxLine == null) {
                    maxLine = lineNumber;
                }

                if (lineNumber < minLine) {
                    minLine = lineNumber;
                }
                if (lineNumber > maxLine) {
                    maxLine = lineNumber;
                }
            }
        }

        final SourceCodePosition info;
        if (line_number_table_length > 0) {
            info = new DefaultSourceCodePosition(Objects.requireNonNull(minLine), Objects.requireNonNull(maxLine), order);
        } else {
            info = new DefaultSourceCodePosition(order);
        }

        return info;
    }

    private static void skipAttribute(DataInputStream data) throws IOException {
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        int attribute_length = data.readInt();// u4 attribute_length;
        while (attribute_length > 0) {
            int skippedBytes = data.skipBytes(attribute_length);
            attribute_length = attribute_length - skippedBytes;
        }
    }

    /**
     * Получает строковое представление типа по его представлению (в файле класса).
     * <p>
     * <p>
     * <p>
     * BaseType   Character	   Type    Interpretation
     * B          byte           signed byte
     * C          char           Unicode character code point in the Basic Multilingual Plane, encoded with UTF-16
     * D          double         double-precision floating-point VALUE
     * F          float          single-precision floating-point VALUE
     * I          int            integer
     * J          long           long integer
     * L          ClassName ;    reference	an instance of class ClassName
     * S          short          signed short
     * Z          boolean	       true or false
     * [          reference      one array dimension
     *
     * @param typeName - строковое закодированное представление типа
     * @return -
     */
    private static String parseTypeName(String typeName, boolean returnType) {
        if (typeName == null) {
            throw new NullPointerException("typeName is null");
        }

        int dimensions = 0;
        for (int i = 0; i < typeName.length(); i++) {
            if (typeName.charAt(i) == '[') {
                dimensions++;
            } else {
                break;
            }
        }
        if (dimensions >= typeName.length()) {
            throw new IllegalStateException("illegal state string");
        }

        final String type;
        switch (typeName.charAt(dimensions)) {
            /* byte           signed byte */
            case 'B': {
                type = "byte";
                break;
            }

            /*char           Unicode character code point in the Basic Multilingual Plane, encoded with UTF-16*/
            case 'C': {
                type = "char";
                break;
            }

            /* double         double-precision floating-point VALUE */
            case 'D': {
                type = "double";
                break;
            }

            /* float          single-precision floating-point VALUE */
            case 'F': {
                type = "float";
                break;
            }

            /*   int            integer */
            case 'I': {
                type = "int";
                break;
            }


            /* long           long integer  */
            case 'J': {
                type = "long";
                break;
            }

            /* ClassName ;    reference	an instance of class ClassName */
            case 'L': {
                int lastIndexOf = typeName.lastIndexOf(';');
                if (lastIndexOf != typeName.length() - 1) {
                    throw new IllegalStateException("wrong type format " + typeName);
                }

                type = typeName.substring(dimensions + 1, lastIndexOf).replace('/', '.');
                break;
            }

            /* short          signed short */
            case 'S': {
                type = "short";
                break;
            }

            /* boolean	       true or false */
            case 'Z': {
                type = "boolean";
                break;
            }

            /* void  for return types only */
            case 'V': {
                if (!returnType) {
                    throw new IllegalStateException("illegal param type");
                }
                type = "void";
                break;
            }


            default: {
                throw new IllegalStateException("illegal type " + typeName.charAt(dimensions));
            }
        }
        StringBuilder result = new StringBuilder();
        result.append(type);
        for (int i = 0; i < dimensions; i++) {
            result.append("[]");
        }
        return "" + result;
    }

    protected static String parseMethodDescriptor(String methodName, String methodDesc) {
        if (methodDesc == null) {
            throw new NullPointerException("methodDescriptor is null");
        }
        int startParamIndex = methodDesc.indexOf('(');
        int endParamIndex = methodDesc.lastIndexOf(')');
        if (startParamIndex >= endParamIndex) {
            throw new IllegalStateException(ILLEGAL_METHOD_DESCRIPTOR + " " + methodDesc);
        }

        final String returnType = parseTypeName(methodDesc.substring(endParamIndex + 1, methodDesc.length()), true);
        String restDesc = methodDesc.substring(startParamIndex + 1, endParamIndex);

        int newIndex;
        int index = 0;
        StringBuilder builder = new StringBuilder();

        builder.append(returnType);
        builder.append(" ");
        builder.append(methodName);
        builder.append("(");
        while ((newIndex = nextIndex(restDesc, index)) > 0) {
            builder.append(parseTypeName(restDesc.substring(index, newIndex), false));

            index = newIndex;
            if (nextIndex(restDesc, index) > 0) {
                builder.append(",");
            }
        }
        builder.append(")");

        return "" + builder;
    }


    private static int nextIndex(String type, int index) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        while (index < type.length()) {
            switch (type.charAt(index)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'S':
                case 'Z':
                case 'V':
                case 'J': {
                    index++;
                    return index;
                }

                case 'L': {
                    index = type.indexOf(';', index);
                    if (index == -1) {
                        throw new IllegalStateException(ILLEGAL_METHOD_DESCRIPTOR + " " + type);
                    }
                    index++;
                    return index;
                }

                case '[': {
                    index++;
                    break;
                }

                default: {
                    throw new IllegalStateException(ILLEGAL_METHOD_DESCRIPTOR + " " + type);
                }
            }
        }
        return -1;
    }

}
