package com.tcm.utilities;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.tcm.utilities.ApplicationConstants.*;

@UtilityClass
@Slf4j
public class PasswordValidatorUtility {

    public Mono<Boolean> isPasswordValid(String password) {
        return Flux.merge(
                        validateLengthRule(password).delayElement(Duration.ofMillis(OPERATION_WAIT_TIME_IN_MS)),
                        validateNullabilityRule(password).delayElement(Duration.ofMillis(OPERATION_WAIT_TIME_IN_MS)),
                        validateUpperCaseRule(password).delayElement(Duration.ofMillis(OPERATION_WAIT_TIME_IN_MS)),
                        validateLowerCaseRule(password).delayElement(Duration.ofMillis(OPERATION_WAIT_TIME_IN_MS)),
                        validateDigitRule(password).delayElement(Duration.ofMillis(OPERATION_WAIT_TIME_IN_MS)))
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
        return validationResults.stream().anyMatch(validationResult -> LOWERCASE_RULE.equals(validationResult.getValidationRule()));
    }

    private Mono<ValidationResult> validateLengthRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.length() > 8)
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule(LENGTH_RULE).isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(LENGTH_CHECK_FAILURE_MESSAGE)))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule(LENGTH_RULE).isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateNullabilityRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule(NULLABILITY_RULE).isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule(NULLABILITY_RULE).isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateUpperCaseRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isUpperCase))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule(UPPERCASE_RULE).isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(UPPERCASE_CHECK_FAILURE_MESSAGE)))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule(UPPERCASE_RULE).isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateLowerCaseRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isLowerCase))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule(LOWERCASE_RULE).isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(LOWERCASE_CHECK_FAILURE_MESSAGE)))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule(LOWERCASE_RULE).isSuccessfullyValidated(false).build()));
    }

    private Mono<ValidationResult> validateDigitRule(String password) {
        return Mono.justOrEmpty(password)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(NULLABILITY_CHECK_FAILURE_MESSAGE)))
                .filter(p -> p.chars().anyMatch(Character::isDigit))
                .flatMap(e -> Mono.just(ValidationResult.builder().validationRule(DIGITS_RULE).isSuccessfullyValidated(true).build()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(DIGITS_CHECK_FAILURE_MESSAGE)))
                .doOnError(throwable -> log.warn(throwable.getMessage()))
                .onErrorResume(throwable -> Mono.just(ValidationResult.builder().validationRule(DIGITS_RULE).isSuccessfullyValidated(false).build()));
    }

}
