package com.steammachine.commonutils.properties.order;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class OrderedProperties {

    enum ItemType {
        COMMENT,
        VALUE
    }

    static class Item {
        private final ItemType itemType;
        private final String template;
        private final String key;
        private final String value;

        Item(ItemType itemType, String template, String key, String value) {
            this.itemType = requireNonNull(itemType);
            this.template = template;
            this.key = key;
            this.value = value;
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
    }

    private final LinkedHashMap<String, Item> properties = new LinkedHashMap<>();

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
        Item item = properties.get(key);
        if (item.itemType == ItemType.VALUE) {
            return item.value;
        } else {
            return null;
        }
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        this.properties.entrySet().forEach(i -> properties.setProperty(i.getKey(), i.getValue().value));
        return properties;
    }


    static Item parseValue(String data) {
        requireNonNull(data);
        if (data.trim().startsWith("#")) {
            /* case of comment */
            return new Item(ItemType.COMMENT, data, null, null);
        } if (data.indexOf('=') != 0) {
            int equalSignIndex = data.indexOf('=');


            String rawKey = data.substring(0, equalSignIndex);
            String rawValue = data.substring(equalSignIndex + 1, data.length());

            // rawKey.


        }
        return null;

    }


}
