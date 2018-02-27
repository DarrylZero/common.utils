package com.steammachine.commonutils.properties.order;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Objects;
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

        private Item(ItemType itemType, String template, String key, String value) {
            this.itemType = itemType;
            this.template = template;
            this.key = key;
            this.value = value;
        }
    }

    private final LinkedHashMap<String, String> properties = new LinkedHashMap<>();

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


    static Item parseValue(String data) {
        requireNonNull(data);
        if (data.trim().startsWith("#")) {
            /* case of comment */
            return new Item(ItemType.COMMENT, data, null, null);
        }
        data.

    }


}
