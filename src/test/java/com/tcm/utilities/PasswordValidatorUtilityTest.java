package com.tcm.utilities;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorUtilityTest {

    @Test
    void testWhenAllPasswordCriteriaSucceeds() {

        StepVerifier.create(Mono.just("ab7cd9EF0G"))
                .assertNext(input -> assertTrue(PasswordValidatorUtility.isPasswordValid(input)))
                .verifyComplete();
    }
}