package pl.estimateplus.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
//    String message() default  "{password.error.message}";
    String message() default  "Invalid password. Must contain: 1 small letter, 1 cap letter, 1 digit, 1 special char. Must be 4-15 char long.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
