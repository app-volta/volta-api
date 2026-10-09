package com.volta.api.validation;

import com.volta.api.enums.WasteCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

// Aceita uma lista separada por vírgula em que todos os itens são categorias da CONAMA 275/2001
public class WasteCategoriesValidator implements ConstraintValidator<WasteCategories, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return Arrays.stream(value.split(","))
                .allMatch(item -> WasteCategory.from(item).isPresent());
    }
}
