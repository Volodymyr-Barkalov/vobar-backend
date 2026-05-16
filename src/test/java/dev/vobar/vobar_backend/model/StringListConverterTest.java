package dev.vobar.vobar_backend.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StringListConverterTest {

    private final StringListConverter converter = new StringListConverter();

    @Test
    void convertToDatabaseColumn_whenNull_returnsEmptyString() {
        // given / when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void convertToDatabaseColumn_whenEmptyList_returnsEmptyString() {
        // given / when
        String result = converter.convertToDatabaseColumn(List.of());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void convertToDatabaseColumn_whenMultipleItems_returnsCommaSeparated() {
        // given / when
        String result = converter.convertToDatabaseColumn(List.of("java", "spring", "boot"));

        // then
        assertThat(result).isEqualTo("java,spring,boot");
    }

    @Test
    void convertToEntityAttribute_whenNull_returnsEmptyList() {
        // given / when
        List<String> result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void convertToEntityAttribute_whenBlank_returnsEmptyList() {
        // given / when
        List<String> result = converter.convertToEntityAttribute("   ");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void convertToEntityAttribute_whenCommaSeparated_returnsTrimmedList() {
        // given / when
        List<String> result = converter.convertToEntityAttribute("java, spring, boot");

        // then
        assertThat(result).containsExactly("java", "spring", "boot");
    }
}
