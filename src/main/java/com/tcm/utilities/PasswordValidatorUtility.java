package com.tcm.utilities;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class PasswordValidatorUtility {

    public boolean isPasswordValid(String password) {
        return validateLengthRule(password)
                && validateNullabilityRule(password)
                && validateUpperCaseRule(password)
                && validateLowerCaseRule(password)
                && validateDigitRule(password);
    }

    private boolean validateLengthRule(String password) {
        return Objects.nonNull(password) && password.length() > 8;
    }

    private boolean validateNullabilityRule(String password) {
        return Objects.nonNull(password);
    }

    private boolean validateUpperCaseRule(String password) {
        return Objects.nonNull(password) && password.chars().anyMatch(Character::isUpperCase);
    }

    private boolean validateLowerCaseRule(String password) {
        return Objects.nonNull(password) && password.chars().anyMatch(Character::isLowerCase);
    }

    private boolean validateDigitRule(String password) {
        return Objects.nonNull(password) && password.chars().anyMatch(Character::isDigit);
    }

}
