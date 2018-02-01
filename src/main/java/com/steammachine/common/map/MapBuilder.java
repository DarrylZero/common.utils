package com.steammachine.common.map;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Api(State.EXPERIMENT)
public class MapBuilder<K, V> {

    private final Supplier<Map<K, V>> mapSupplier;
    private Map<K, V> map;

    public static <K, V> MapBuilder<K, V> of(Supplier<Map<K, V>> mapSupplier) {
        return new MapBuilder<>(mapSupplier);
    }

    public static <K, V> MapBuilder<K, V> of() {
        return of(HashMap::new);
    }

    public MapBuilder(Supplier<Map<K, V>> mapSupplier) {
        this.mapSupplier = Objects.requireNonNull(mapSupplier);
    }

    public MapBuilder<K, V> put(K k, V v) {
        requireMap().put(k, v);
        return this;
    }

    public Map<K, V> build() {
        return requireMap();
    }


    private Map<K, V> requireMap() {
        if (map == null) {
            map = mapSupplier.get();
        }
        return map;
    }
}
