package com.steammachine.commonutils.orderedproperties;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.*;
import static java.util.Objects.requireNonNull;

public class OrderedProperties {

    private static final char EQUALSIGN = '=';
    private static final char SPACESIGN = ' ';
    private static final String STRING_PLACEHOLDER = "%s";
    private static final Function<Character, Boolean> SPACE = c -> c == SPACESIGN;
    private static final Function<Character, Boolean> EQUAL = c -> c == EQUALSIGN;
    private static final Function<Character, Boolean> SPACE_OR_EQUALS = c -> c == SPACESIGN || c == EQUALSIGN;
    private static final Function<Character, Boolean> IS_ALPHABETIC = Character::isAlphabetic;

    enum ItemType {
        NOT_FILLED,
        COMMENT,
        VALUE
    }

    static class Key {
        private final ItemType type;
        private final String key;

        Key(ItemType type, String key) {
            this.type = requireNonNull(type);
            this.key = requireNonNull(key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key1 = (Key) o;

            if (type != key1.type) return false;
            return key != null ? key.equals(key1.key) : key1.key == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (key != null ? key.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "type=" + type +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    static class Item {
        private final ItemType itemType;
        private final String template;
        private final String key;
        private final String value;

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
                    ", key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    private final LinkedHashMap<Key, Item> properties = new LinkedHashMap<>();

    public void load(InputStream stream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

//            String readLine;
//            while ((readLine = reader.readLine()) != null) {
//            }

            reader.lines().peek(Objects::requireNonNull).map(new Function<String, Item>() {
                @Override
                public Item apply(String line) {


                    return null;
                }
            }).forEachOrdered(new Consumer<Item>() {
                @Override
                public void accept(Item value) {

                }
            });

        }
    }

    public void store(OutputStream stream, String comment) {


    }

    public String getProperty(String key) {
        Item item = properties.get(new Key(ItemType.VALUE, key));
        if (item.itemType == ItemType.VALUE) {
            return item.value;
        } else {
            return null;
        }
    }

    public Properties toProperties() {
        Properties props = new Properties();
        properties.entrySet().stream().filter(entry -> entry.getKey().type == ItemType.VALUE).
                forEach(i -> props.setProperty(i.getKey().key, i.getValue().value));
        return props;
    }

    static Item parseValue(String data) {
        requireNonNull(data);

        if (data.trim().isEmpty()) {
            /* case of comment */
            return new Item(ItemType.NOT_FILLED, data, null, null);
        }

        if (data.trim().startsWith("#")) {
            /* case of comment */
            return new Item(ItemType.COMMENT, data, null, null);
        }

        StringBuilder template = new StringBuilder();

        int keyFirst = requireNonNull(findFirst(data, 0, IS_ALPHABETIC));
        int keyLast = requireNonNull(findFirst(data, keyFirst + 1, SPACE_OR_EQUALS));

        template.append(data.substring(0, keyFirst)); /* key prefix */
        template.append(STRING_PLACEHOLDER); /* a key */
        final String key = data.substring(keyFirst, keyLast);

        /*  ------------------------------------ a key has been processed ------------------------------------ */

        int dividerFirst = requireNonNull(findFirst(data, keyLast, SPACE_OR_EQUALS));
        template.append(data.substring(keyLast, dividerFirst)); /* a key */


        Integer spaceFirst = findFirst(data, keyFirst + 1, SPACE);
        Integer equalsFirst = findFirst(data, keyFirst + 1, EQUAL);
        if (spaceFirst == null && equalsFirst != null) {
            final int dividerLast = dividerFirst + 1;
            template.append("=");

            String rest = data.substring(dividerLast);
            Integer valueFirst = findFirst(rest, 0, Character::isAlphabetic);
            String value = rest.substring(valueFirst);

            template.append(rest.substring(0, valueFirst));
            template.append(STRING_PLACEHOLDER);
            return new Item(ItemType.VALUE, template.toString(), key, value);
        }

        if (spaceFirst != null && equalsFirst == null) {


            throw new IllegalStateException();
        }

        requireNonNull(spaceFirst);
        requireNonNull(equalsFirst);
        if (Objects.equals(spaceFirst, equalsFirst)) {
            throw new IllegalStateException();
        }

        if (spaceFirst.compareTo(equalsFirst) > 0) {
            /* case when equals goes first it is a delimetr */
            template.append(data.substring(keyLast, equalsFirst));
            template.append(EQUALSIGN);

            String rest = data.substring(equalsFirst + 1);
            Integer firstValueIndex = findFirst(rest, 0, IS_ALPHABETIC);


            template.append(rest.substring(0, firstValueIndex));
            template.append(STRING_PLACEHOLDER);
            String value = rest.substring(firstValueIndex);


            return Item.value("" + template, key, value);
        }
        if (spaceFirst.compareTo(equalsFirst) < 0) {
            /* the case when space goes the first */

            template.append(data.substring(keyLast, equalsFirst));
            template.append(EQUALSIGN);

            String rest = data.substring(equalsFirst + 1);
            Integer valueFirst = requireNonNull(findFirst(rest, 0, IS_ALPHABETIC));

            template.append(rest.substring(0, valueFirst));
            template.append(STRING_PLACEHOLDER);

            String value = rest.substring(valueFirst);

            return Item.value("" + template, key, value);
        }

        if (data.charAt(dividerFirst) == ' ') {
            int nextEqualSign = findFirst(data, dividerFirst, EQUAL);
            template.append(data.substring(keyLast, nextEqualSign));
            template.append(EQUALSIGN);

            String rest = data.substring(nextEqualSign + 1);
            Integer valueFirst = findFirst(rest, 0, IS_ALPHABETIC);

            template.append(rest.substring(0, valueFirst));
            template.append(STRING_PLACEHOLDER);

            String value = rest.substring(valueFirst);
            return new Item(ItemType.VALUE, template.toString(), key, value);
        } else {
            throw new IllegalStateException();
        }
    }


    static String getLine(Item item) {
        requireNonNull(item);
        switch (item.itemType) {
            case NOT_FILLED:
                return item.template;

            case COMMENT:
                return item.template;

            case VALUE:
                return String.format(item.template, item.key, item.value);

            default:
                throw new IllegalStateException(String.format("unknown itemtype %s ", "" + item.itemType));

        }
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
