package com.steammachine.common.map;

import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapBuilderCheck {

    @Test
    void fullName() {
        assertEquals("com.steammachine.common.map.MapBuilder", MapBuilder.class.getName());
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void creation() {
        new MapBuilder<String, String>(null);
    }

    @Test
    void creation10() {
        new MapBuilder<String, String>(HashMap::new);
    }

    @Test
    void of10() {
        MapBuilder.of();
    }

    @Test
    void of20() {
        MapBuilder.of(HashMap::new);
    }

    @Test
    void of30() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("11", 11);
        Assertions.assertEquals(expected, MapBuilder.<String, Integer>of().put("11", 11).build());
    }

    @Test
    void of40() {
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("11", 11);
        Assertions.assertEquals(expected, MapBuilder.<String, Integer>of(LinkedHashMap::new).put("11", 11).build());
    }




}