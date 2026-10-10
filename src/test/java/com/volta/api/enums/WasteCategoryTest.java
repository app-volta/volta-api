package com.volta.api.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WasteCategory")
class WasteCategoryTest {

    @ParameterizedTest(name = "{0} usa o coletor {1}")
    @CsvSource({
            "PAPEL, Azul",
            "PLASTICO, Vermelho",
            "VIDRO, Verde",
            "METAL, Amarelo",
            "MADEIRA, Preto",
            "PERIGOSO, Laranja",
            "SAUDE, Branco",
            "RADIOATIVO, Roxo",
            "ORGANICO, Marrom",
            "NAO_RECICLAVEL, Cinza"
    })
    @DisplayName("cor do coletor conforme CONAMA 275/2001")
    void getColor_eachCategory_returnsConamaColor(WasteCategory category, String color) {
        assertThat(category.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("somente PERIGOSO, SAUDE e RADIOATIVO são perigosas")
    void isHazardous_onlyHazardousCategories() {
        EnumSet<WasteCategory> hazardous = EnumSet.copyOf(
                Arrays.stream(WasteCategory.values()).filter(WasteCategory::isHazardous).toList());

        assertThat(hazardous).containsExactlyInAnyOrder(
                WasteCategory.PERIGOSO, WasteCategory.SAUDE, WasteCategory.RADIOATIVO);
    }

    @ParameterizedTest(name = "\"{0}\" vira {1}")
    @CsvSource({
            "plastico, PLASTICO",
            "Plástico, PLASTICO",
            "'  Papel  ', PAPEL",
            "Não reciclável, NAO_RECICLAVEL",
            "nao-reciclavel, NAO_RECICLAVEL",
            "ORGÂNICO, ORGANICO"
    })
    @DisplayName("from ignora caixa, acentos, espaços e hífens")
    void from_variantSpellings_returnsCategory(String value, WasteCategory expected) {
        assertThat(WasteCategory.from(value)).contains(expected);
    }

    @ParameterizedTest(name = "\"{0}\" não é uma categoria")
    @ValueSource(strings = {"", "   ", "borracha", "PLASTICOS"})
    @DisplayName("from devolve vazio para valores desconhecidos")
    void from_unknownValue_returnsEmpty(String value) {
        assertThat(WasteCategory.from(value)).isEmpty();
    }

    @Test
    @DisplayName("from devolve vazio para nulo")
    void from_null_returnsEmpty() {
        assertThat(WasteCategory.from(null)).isEmpty();
    }

    @ParameterizedTest
    @EnumSource(WasteCategory.class)
    @DisplayName("from reconhece o nome de todas as categorias")
    void from_enumName_returnsSameCategory(WasteCategory category) {
        assertThat(WasteCategory.from(category.name())).contains(category);
    }

    @Test
    @DisplayName("fromList separa por vírgula e ignora valores desconhecidos")
    void fromList_commaSeparatedValues_ignoresUnknownValues() {
        assertThat(WasteCategory.fromList(" papel , Vidro, borracha,, perigoso"))
                .containsExactlyInAnyOrder(WasteCategory.PAPEL, WasteCategory.VIDRO, WasteCategory.PERIGOSO);
    }

    @Test
    @DisplayName("fromList não repete categorias")
    void fromList_duplicatedValues_returnsEachCategoryOnce() {
        assertThat(WasteCategory.fromList("metal,METAL,Metal")).containsExactly(WasteCategory.METAL);
    }

    @Test
    @DisplayName("fromList devolve vazio para nulo e texto vazio")
    void fromList_nullOrEmpty_returnsEmptySet() {
        assertThat(WasteCategory.fromList(null)).isEmpty();
        assertThat(WasteCategory.fromList("")).isEmpty();
    }

    @Test
    @DisplayName("normalize não depende do idioma do sistema")
    void normalize_turkishDefaultLocale_stillReturnsCategoryName() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));

            assertThat(WasteCategory.normalize("plastico")).isEqualTo("PLASTICO");
            assertThat(WasteCategory.from("organico")).contains(WasteCategory.ORGANICO);
        } finally {
            Locale.setDefault(original);
        }
    }

    @Test
    @DisplayName("normalize remove acentos e troca espaços e hífens por underscore")
    void normalize_accentedText_returnsEnumStyleName() {
        assertThat(WasteCategory.normalize("  Não reciclável ")).isEqualTo("NAO_RECICLAVEL");
        assertThat(WasteCategory.normalize("nao - reciclavel")).isEqualTo("NAO_RECICLAVEL");
    }
}
