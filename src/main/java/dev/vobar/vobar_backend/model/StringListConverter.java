package dev.vobar.vobar_backend.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(",", list);
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        if (data == null || data.isBlank()) return List.of();
        return Arrays.stream(data.split(",")).map(String::trim).toList();
    }
}
