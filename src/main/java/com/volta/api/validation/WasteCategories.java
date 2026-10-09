package com.volta.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WasteCategoriesValidator.class)
public @interface WasteCategories {

    String message() default "Especialidades inválidas. Valores aceitos: "
            + "PAPEL, PLASTICO, VIDRO, METAL, MADEIRA, PERIGOSO, SAUDE, RADIOATIVO, ORGANICO, NAO_RECICLAVEL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
