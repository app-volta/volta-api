package com.volta.api.utils;

import java.util.Locale;

public final class CnpjUtils {

    private CnpjUtils() {
    }

    public static String normalize(String cnpj) {
        if (cnpj == null) {
            return null;
        }

        return cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase(Locale.ROOT);
    }
}
