package com.volta.api.enums;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

// Categorias e cores da coleta seletiva definidas pela Resolução CONAMA 275/2001
public enum WasteCategory {
    PAPEL("Azul", false),
    PLASTICO("Vermelho", false),
    VIDRO("Verde", false),
    METAL("Amarelo", false),
    MADEIRA("Preto", false),
    PERIGOSO("Laranja", true),
    SAUDE("Branco", true),
    RADIOATIVO("Roxo", true),
    ORGANICO("Marrom", false),
    NAO_RECICLAVEL("Cinza", false);

    private final String color;
    private final boolean hazardous;

    WasteCategory(String color, boolean hazardous) {
        this.color = color;
        this.hazardous = hazardous;
    }

    public String getColor() {
        return color;
    }

    public boolean isHazardous() {
        return hazardous;
    }

    public static Optional<WasteCategory> from(String value) {
        if (value == null) {
            return Optional.empty();
        }

        String normalized = normalize(value);
        return Arrays.stream(values())
                .filter(category -> category.name().equals(normalized))
                .findFirst();
    }

    public static Set<WasteCategory> fromList(String values) {
        Set<WasteCategory> categories = EnumSet.noneOf(WasteCategory.class);
        if (values == null) {
            return categories;
        }

        for (String value : values.split(",")) {
            from(value).ifPresent(categories::add);
        }

        return categories;
    }

    // "Não reciclável" -> "NAO_RECICLAVEL"
    public static String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .replaceAll("[\\s-]+", "_")
                .toUpperCase();
    }
}
