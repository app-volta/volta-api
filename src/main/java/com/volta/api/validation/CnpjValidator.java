package com.volta.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Valida os dígitos verificadores do CNPJ numérico e do alfanumérico (IN RFB 2.229/2024).
// Espera o valor já sem máscara: 12 caracteres [0-9A-Z] seguidos de 2 dígitos verificadores.
public class CnpjValidator implements ConstraintValidator<Cnpj, String> {

    private static final int[] FIRST_DIGIT_WEIGHTS = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] SECOND_DIGIT_WEIGHTS = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null || cnpj.isBlank()) {
            return true;
        }

        if (!cnpj.matches("[0-9A-Z]{12}[0-9]{2}")) {
            return false;
        }

        if (cnpj.chars().distinct().count() == 1) {
            return false;
        }

        int firstDigit = calculateDigit(cnpj, FIRST_DIGIT_WEIGHTS);
        int secondDigit = calculateDigit(cnpj, SECOND_DIGIT_WEIGHTS);

        return firstDigit == Character.getNumericValue(cnpj.charAt(12))
                && secondDigit == Character.getNumericValue(cnpj.charAt(13));
    }

    private int calculateDigit(String cnpj, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            // Valor do caractere = código ASCII - 48 (dígitos 0-9, letras A=17 ... Z=42)
            sum += (cnpj.charAt(i) - '0') * weights[i];
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
