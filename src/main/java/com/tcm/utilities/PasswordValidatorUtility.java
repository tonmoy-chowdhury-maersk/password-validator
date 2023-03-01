package com.tcm.utilities;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class PasswordValidatorUtility {

    public Mono<Boolean> isPasswordValid(String password) {
        return Flux.merge(
                    validateLengthRule(password),
                    validateNullabilityRule(password),
                    validateUpperCaseRule(password),
                    validateLowerCaseRule(password),
                    validateDigitRule(password)).collectList()
                .map(PasswordValidatorUtility::combineResults);
    }

    private static boolean combineResults(List<Boolean> validationResults) {
        return validationResults.stream().reduce(true, (r1, r2) -> r1 && r2);
    }

    private Mono<Boolean> validateLengthRule(String password) {
        return Mono.just(password)
                .filter(Objects::nonNull)
                .filter(p -> p.length() > 8)
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should be larger than 8 chars.")));
    }

    private Mono<Boolean> validateNullabilityRule(String password) {
        return Mono.just(password)
                .filter(Objects::nonNull)
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should not be null.")));
    }

    private Mono<Boolean> validateUpperCaseRule(String password) {
        return Mono.just(password)
                .filter(Objects::nonNull)
                .filter(p -> p.chars().anyMatch(Character::isUpperCase))
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one uppercase letter at least.")));
    }

    private Mono<Boolean> validateLowerCaseRule(String password) {
        return Mono.just(password)
                .filter(Objects::nonNull)
                .filter(p -> p.chars().anyMatch(Character::isLowerCase))
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one lowercase letter at least.")));
    }

    private Mono<Boolean> validateDigitRule(String password) {
        return Mono.just(password)
                .filter(Objects::nonNull)
                .filter(p -> p.chars().anyMatch(Character::isDigit))
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one number at least.")));
    }

}
