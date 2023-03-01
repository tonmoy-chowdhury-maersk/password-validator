package com.tcm.utilities;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class PasswordValidatorUtilityTest {

    @Test
    void testWhenAllPasswordCriteriaSucceeds() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("dsfdDFF454sfsdsd"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @Disabled
    void testWhenAllPasswordLengthCriteriaFails() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("dffF5D"))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && "Password should be larger than 8 chars.".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    @Disabled
    void testWhenAllPasswordNullabilityCriteriaFails() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid(null))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && "Password should not be null.".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    @Disabled
    void testWhenAllPasswordUpperCaseCriteriaFails() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("dffsf23fd"))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && "Password should have one uppercase letter at least.".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    @Disabled
    void testWhenAllPasswordLowerCaseCriteriaFails() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("DSDS343SDS"))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && "Password should have one lowercase letter at least.".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    @Disabled
    void testWhenAllPasswordDigitCriteriaFails() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("sdfDfdFDF"))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && "Password should have one number at least.".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    void testNullabilityUpperAndLowerCaseValid() {
        // Valid due to successful Nullability, UpperCase and LowerCase criteria
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("dsfdDF"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testNullabilityDigitsAndLowerCaseValid() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("dsf36df"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testNullabilityDigitsAndUpperCaseValid() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("DG7N"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testNullabilityLengthAndUpperCaseValid() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("DDFDFTTTSSR"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testNullabilityLengthAndLowerCaseValid() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("fgdfgfdgdggdfg"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testNullabilityLengthAndDigitsValid() {
        StepVerifier.create(PasswordValidatorUtility.isPasswordValid("4535675624"))
                .expectNext(true)
                .verifyComplete();
    }

}