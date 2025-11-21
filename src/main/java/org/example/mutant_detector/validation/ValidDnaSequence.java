package org.example.mutant_detector.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDnaSequenceValidator.class) // Indica qué clase tiene la lógica
@Target({ElementType.FIELD, ElementType.PARAMETER}) // Se puede usar en campos y parámetros
@Retention(RetentionPolicy.RUNTIME) // Se verifica en tiempo de ejecución
public @interface ValidDnaSequence {

    String message() default "Invalid DNA sequence: must be a square NxN matrix (minimum 4x4) with only A, T, C, G characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}