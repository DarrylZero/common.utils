package com.steammachine.commonutils.orderedproperties;

import com.steammachine.common.utils.commonutils.CommonUtils;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;
import static java.util.Objects.requireNonNull;

/**
 * {$link com.steammachine.commonutils.orderedproperties.OrderedProperties }
 * com.steammachine.commonutils.orderedproperties.OrderedProperties
 */
public class OrderedProperties {

    enum ItemType {
        NOT_FILLED,
        COMMENT,
        VALUE
    }

    static class Key {
        private final ItemType type;
        private final String id;

        Key(ItemType type, String id) {
            this.type = requireNonNull(type);
            this.id = requireNonNull(id);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key1 = (Key) o;

            if (type != key1.type) return false;
            return id != null ? id.equals(key1.id) : key1.id == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (id != null ? id.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "type=" + type +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

    static class Item {
        private final ItemType itemType;
        private final String template;
        private final String key;
        private String value;

        Item(ItemType itemType, String template, String key, String value) {
            this.itemType = requireNonNull(itemType);
            this.template = requireNonNull(template);
            this.key = key;
            this.value = value;
        }

        static Item notFilled(String data) {
            return new Item(ItemType.NOT_FILLED, data, null, null);
        }

        static Item comment(String comment) {
            return new Item(ItemType.COMMENT, comment, null, null);
        }

        static Item value(String template, String key, String value) {
            return new Item(ItemType.VALUE, template, key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item item = (Item) o;

            if (itemType != item.itemType) return false;
            if (template != null ? !template.equals(item.template) : item.template != null) return false;
            if (key != null ? !key.equals(item.key) : item.key != null) return false;
            return value != null ? value.equals(item.value) : item.value == null;
        }

        @Override
        public int hashCode() {
            int result = itemType != null ? itemType.hashCode() : 0;
            result = 31 * result + (template != null ? template.hashCode() : 0);
            result = 31 * result + (key != null ? key.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "itemType=" + itemType +
                    ", template='" + template + '\'' +
                    ", id='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    private static final char EQUALSIGN = '=';
    private static final char SPACESIGN = ' ';
    private static final String STRING_PLACEHOLDER = "%s";
    private static final String PREFIX = "PREFIX";
    private static final Function<Character, Boolean> SPACE = c -> c == SPACESIGN;
    private static final Function<Character, Boolean> EQUAL = c -> c == EQUALSIGN;
    private static final Function<Character, Boolean> SPACE_OR_EQUALS = c -> c == SPACESIGN || c == EQUALSIGN;
    private static final Function<Character, Boolean> WRITTEN_VALUE = c -> !Character.isSpaceChar(c);
    private final LinkedHashMap<Key, Item> properties = new LinkedHashMap<>();
    private int currentPrefix;


    public void load(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().peek(Objects::requireNonNull).
                map(OrderedProperties::parseValue).
                forEachOrdered(v -> properties.put(newKey(v.itemType, v.key), v));
    }

    public void store(OutputStream stream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

        properties.entrySet().stream().map(Map.Entry::getValue).map(OrderedProperties::getLine).
                forEachOrdered(s -> {
                    CommonUtils.suppressWOResult(() -> writer.write(s));
                    CommonUtils.suppressWOResult(writer::newLine);
                });
        writer.flush();
    }

    public void clear() {
        properties.clear();
        currentPrefix = 0;
    }

    public void setPoperty(String key, String value) {
        Key keyy = new Key(ItemType.VALUE, key);
        properties.computeIfAbsent(keyy, key1 -> Item.value("%s=%s", key1.id, null)).value = value;
    }

    public String getProperty(String key) {
        Item item = properties.get(new Key(ItemType.VALUE, key));
        return item == null ? null : item.value;
    }

    public Properties toProperties() {
        Properties props = new Properties();
        properties.entrySet().stream().filter(entry -> entry.getKey().type == ItemType.VALUE).
                forEachOrdered(i -> props.setProperty(i.getKey().id, i.getValue().value));
        return props;
    }


    static Item parseValue(String data) {
        requireNonNull(data);

        if (data.trim().isEmpty()) {
            /* case of comment */
            return new Item(ItemType.NOT_FILLED, data, "", "");
        }

        if (data.trim().startsWith("#")) {
            /* case of comment */
            return new Item(ItemType.COMMENT, data, "", "");
        }

        StringBuilder template = new StringBuilder();

        final int keyFirst = requireNonNull(findFirst(data, 0, WRITTEN_VALUE));
        final Integer keyLast = findFirst(data, keyFirst + 1, SPACE_OR_EQUALS);
        final String key = keyLast != null ? data.substring(keyFirst, keyLast) : data.substring(keyFirst);

        template.append(data.substring(0, keyFirst)); /* id prefix */
        template.append(STRING_PLACEHOLDER); /* a id */
        if (keyLast == null) {
            template.append(STRING_PLACEHOLDER);
            return new Item(ItemType.VALUE, template.toString(), key, "");
        }

        /*  ------------------------------------ a id has been processed ------------------------------------ */

        int dividerFirst = requireNonNull(findFirst(data, keyLast, SPACE_OR_EQUALS));
        template.append(data.substring(keyLast, dividerFirst)); /* a id */


        Integer spaceFirst = findFirst(data, keyFirst + 1, SPACE);
        Integer equalsFirst = findFirst(data, keyFirst + 1, EQUAL);

        if (spaceFirst == null && equalsFirst != null) {
            final int dividerLast = dividerFirst + 1;
            template.append(EQUALSIGN);

            String rest = data.substring(dividerLast);
            int valueFirst = Objects.requireNonNull(findFirst(rest, 0, WRITTEN_VALUE));
            String value = rest.substring(valueFirst);

            template.append(rest.substring(0, valueFirst));
            template.append(STRING_PLACEHOLDER);
            return new Item(ItemType.VALUE, template.toString(), key, value);
        }

        if (spaceFirst != null && equalsFirst == null) {
            /* there is no = divider*/
            int valueFirst = requireNonNull(findFirst(data, spaceFirst + 1, WRITTEN_VALUE));
            template.append(data.substring(keyLast, valueFirst));
            template.append(STRING_PLACEHOLDER);
            String value = data.substring(valueFirst);
            return new Item(ItemType.VALUE, template.toString(), key, value);
        }

        requireNonNull(spaceFirst);
        requireNonNull(equalsFirst);
        check(() -> !Objects.equals(spaceFirst, equalsFirst), IllegalStateException::new);

        if (spaceFirst > equalsFirst) {
            /* case when equals goes first it is a delimetr */
            template.append(data.substring(keyLast, equalsFirst));
            template.append(EQUALSIGN);

            String rest = data.substring(equalsFirst + 1);
            Integer firstValueIndex = findFirst(rest, 0, WRITTEN_VALUE);


            template.append(rest.substring(0, firstValueIndex));
            template.append(STRING_PLACEHOLDER);
            String value = rest.substring(firstValueIndex);


            return Item.value("" + template, key, value);
        }
        if (spaceFirst < equalsFirst) {
            /* the case when space goes the first */
            int valueFirst = requireNonNull(findFirst(data, spaceFirst + 1, WRITTEN_VALUE));
            if (valueFirst < equalsFirst) {
                template.append(data.substring(keyLast, valueFirst));
                template.append(STRING_PLACEHOLDER);
                String value = data.substring(valueFirst);
                return Item.value("" + template, key, value);
            } else {
                template.append(data.substring(keyLast, equalsFirst));
                template.append(EQUALSIGN);

                valueFirst = requireNonNull(findFirst(data, equalsFirst + 1, WRITTEN_VALUE));
                template.append(data.substring(equalsFirst + 1, valueFirst));
                template.append(STRING_PLACEHOLDER);

                String value = data.substring(valueFirst);
                return Item.value("" + template, key, value);
            }
        }

        throw new IllegalStateException();
    }


    static String getLine(Item item) {
        requireNonNull(item);
        switch (item.itemType) {
            case NOT_FILLED:
            case COMMENT:
                return item.template;

            case VALUE:
                return String.format(item.template, item.key, item.value);

            default:
                throw new IllegalStateException(String.format("unknown itemtype %s ", "" + item.itemType.name()));

        }
    }

    private Key newKey(ItemType itemType, String id) {
        switch (itemType) {
            case NOT_FILLED:
            case COMMENT:
                return uniqueId(itemType);


            case VALUE:
                return new Key(itemType, id);

            default:
                throw new IllegalStateException(String.format("unknown type %s", itemType.name()));
        }
    }

    private Key uniqueId(ItemType itemType) {
        check(() -> itemType == ItemType.COMMENT || itemType == ItemType.NOT_FILLED, IllegalStateException::new);
        while (properties.keySet().contains(new Key(itemType, PREFIX + currentPrefix))) {
            currentPrefix++;
        }
        return new Key(itemType, PREFIX + currentPrefix);
    }


    private static Integer findFirst(String data, int start, Function<Character, Boolean> function) {
        for (int i = start; i < data.length(); i++) {
            if (function.apply(data.charAt(i))) {
                return i;
            }
        }
        return null;
    }

}
