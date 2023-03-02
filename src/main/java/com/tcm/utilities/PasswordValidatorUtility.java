package com.tcm.utilities;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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
                .filter(ValidationResult::isSuccessfullyValidated)
                .collectList()
                .map(PasswordValidatorUtility::checkAtleastMinimumNoOfRulesValid)
                .filter(PasswordValidatorUtility::checkIfMandatoryRuleValid)
                .hasElement();

    }

    private List<ValidationResult> checkAtleastMinimumNoOfRulesValid(List<ValidationResult> validationResults) {
        return validationResults.size() >= 3 ? validationResults : List.of();
    }

    private boolean checkIfMandatoryRuleValid(List<ValidationResult> validationResults) {
        return validationResults.stream().anyMatch(validationResult -> "lowercase".equals(validationResult.getValidationRule()));
    }

    private Mono<ValidationResult> validateLengthRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.length() > 8)
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule("length").isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should be larger than 8 chars.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule("length").isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateNullabilityRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule("nullability").isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule("nullability").isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateUpperCaseRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isUpperCase))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule("uppercase").isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one uppercase letter at least.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule("uppercase").isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateLowerCaseRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isLowerCase))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule("lowercase").isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one lowercase letter at least.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule("lowercase").isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateDigitRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isDigit))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule("digits").isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Password should have one number at least.")))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule("digits").isSuccessfullyValidated(false).build()));
    }

}
