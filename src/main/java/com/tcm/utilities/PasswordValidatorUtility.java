package com.tcm.utilities;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@UtilityClass
@Slf4j
public class PasswordValidatorUtility {

    private static final String NULLABILITY_CHECK_FAILURE_MESSAGE = "Password should not be null.";

    public Mono<Boolean> isPasswordValid(String password) {
        return Flux.merge(
                        validateLengthRule(password),
                        validateNullabilityRule(password),
                        validateUpperCaseRule(password),
                        validateLowerCaseRule(password),
                        validateDigitRule(password))
                .filter(Boolean::booleanValue)
                .count()
                .flatMap(count -> {
                    if (count >= 3) {
                        return Mono.just(true);
                    } else {
                        return Mono.error(new IllegalArgumentException("Password is NOT OK because at least THREE of the previous conditions is NOT MET."));
                    }
                });

    }

    private Mono<Boolean> validateLengthRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.length() > 8)
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should be larger than 8 chars.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(false));
    }

    private Mono<Boolean> validateNullabilityRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(false));
    }

    private Mono<Boolean> validateUpperCaseRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isUpperCase))
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one uppercase letter at least.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(false));
    }

    private Mono<Boolean> validateLowerCaseRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isLowerCase))
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one lowercase letter at least.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(false));
    }

    private Mono<Boolean> validateDigitRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isDigit))
                .flatMap(e -> Mono.just(true))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one number at least.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(false));
    }

}
