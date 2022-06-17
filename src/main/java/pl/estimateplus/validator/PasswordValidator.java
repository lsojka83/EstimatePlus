package pl.estimateplus.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.estimateplus.controller.AdminController;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password,String> {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null)
        {
            return true;
        }
        return verifyPassword(value);
    }



    static boolean verifyPassword(String passwordCandidate) {
        logger.info("!!! validating"+passwordCandidate);
        //has 4 - 15 chars
        if(passwordCandidate.length()<4 || passwordCandidate.length()>15) {
            return false;
        }
        //contains at least one small letter
        Pattern patAtLeastOneSmallLetter = Pattern.compile("[a-z]");
        Matcher matAtLeastOneSmallLetter = patAtLeastOneSmallLetter.matcher(passwordCandidate);
        if(!matAtLeastOneSmallLetter.find()) {
            return false;
        }
        //contains at least one capital letter
        Pattern patAtLeastOneBigLetter = Pattern.compile("[A-Z]");
        Matcher matAtLeastOneBigLetter = patAtLeastOneBigLetter.matcher(passwordCandidate);
        if(!matAtLeastOneBigLetter.find()) {
            return false;
        }
        //contains at least one special char
        Pattern patAtLeastOneSpecialChar = Pattern.compile("[@$!%*#?&]");
        Matcher matAtLeastOneSpecialChar = patAtLeastOneSpecialChar.matcher(passwordCandidate);
        if(!matAtLeastOneSpecialChar.find()) {
            return false;
        }
        //contains at least one digit
        Pattern patAtLeastOneDigit = Pattern.compile("[\\d]");
        Matcher matAtLeastOneDigit = patAtLeastOneDigit.matcher(passwordCandidate);
        if(!matAtLeastOneDigit.find()) {
            return false;
        }
        return true;
    }


}
