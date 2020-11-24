package com.foxconn.fii.data.primary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MapResponse<T extends Comparable<? super T>> {
    private List<Entry<T>> data = new ArrayList<>();
    private int size = 0;

    @JsonIgnore
    private Map<String, T> raw = new LinkedHashMap<>();

    public static <T extends Comparable<? super T>> MapResponse<T> of(Map<String, T> raw, int size) {
        MapResponse<T> mapResponse = new MapResponse<>();
        mapResponse.setRaw(raw);
        mapResponse.setSize(size);
        mapResponse.convert();
        return mapResponse;
    }

    public void put(String key, T element) {
        raw.put(key, element);
    }

    public T get(String key, T defaultValue) {
        return raw.getOrDefault(key, defaultValue);
    }

    public void sort(boolean reverseOrder) {
        if (reverseOrder) {
            raw = raw.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        } else {
            raw = raw.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        }
    }

    public void convert() {
        raw.forEach((key, value) -> data.add(Entry.of(key, value)));
    }

    @Value(staticConstructor = "of")
    private static class Entry<T> {
        private String key;
        private T value;
    }
}
