package com.steammachine.commonutils.properties.propertiesset;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

/**
 * Created 31.12.2017 18:10
 * {@link PropertyIterator}
 * @author Vladimir Bogodukhov
 */
public class PropertyIterator {

    private PropertyIterator() {
    }

    private static class PropItem<PropertyKey, PropertyObject>
            implements PropertiesItem<PropertyKey, PropertyObject> {

        private final PropertyKey key;
        private final PropertyObject object;

        private PropItem(PropertyKey key, PropertyObject object) {
            this.key = Objects.requireNonNull(key, "key is null");
            this.object = Objects.requireNonNull(object, "object is null");
        }

        public PropertyKey key() {
            return key;
        }

        public PropertyObject object() {
            return object;
        }
    }

    public static Iterable<PropertiesItem<String, Properties>> propertiesSet(
            Path initialPropFilePath,
            Function<Path, Properties> propertyLoader,
            Function<Properties, String> propertyIdGetter,
            Function<Properties, Iterable<String>> nextProperties) {

        final class PathWrapper {
            final Path path;
            final String filePath;

            PathWrapper(Path path) {
                this.path = Objects.requireNonNull(path, "path is null");
                this.filePath = path.normalize().toAbsolutePath().toFile().getAbsolutePath();
            }

            @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
            @Override
            public boolean equals(Object o) {
                return filePath.equals(o);
            }

            @Override
            public int hashCode() {
                return filePath.hashCode();
            }
        }


        Objects.requireNonNull(initialPropFilePath, "initialPropFilePath is null");
        Objects.requireNonNull(propertyLoader, "propertyLoader is null");
        Objects.requireNonNull(propertyIdGetter, "propertyIdGetter is null");


        Path currentPath = initialPropFilePath;
        boolean firstPass = true;
        List<PathWrapper> paths = new ArrayList<>();
        List<PropertiesItem<String, Properties>> list = new LinkedList<>();
        int index = 0;
        while (list.size() > index || firstPass) {
            if (firstPass) {
                firstPass = false;
            }

            Properties properties = propertyLoader.apply(currentPath);
            list.add(new PropItem<>(propertyIdGetter.apply(properties), properties));


            index++;
        }

        return list;
    }


//    PropertiesItem

    public static Iterable<PropertiesItem<String, Properties>> propertiesSet(Path initialPropFilePath) {
        return propertiesSet(
                initialPropFilePath,
                new Function<Path, Properties>() {
                    @Override
                    public Properties apply(Path path) {
                        try {
                            Properties properties = new Properties();
                            FileInputStream stream = new FileInputStream(path.toFile());
                            try {
                                properties.load(stream);
                                return properties;
                            } finally {
                                stream.close();
                            }
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }, new Function<Properties, String>() {
                    @Override
                    public String apply(Properties properties) {
                        String configType = properties.getProperty("configType");
                        if (configType == null) {
                            throw new IllegalStateException("properties.getProperty(\"configType\") == null");
                        }
                        return configType;
                    }
                }, new Function<Properties, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(Properties properties) {
                        List<String> list = new LinkedList<>();
                        for (Object o : properties.keySet()) {
                            String key = (String) o;
                            if (key.contains("nextpropertyfile")) {
                                list.add(properties.getProperty(key));
                            }
                        }
                        return list;
                    }
                }

        );


    }


}
