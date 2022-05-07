package com.sbank.validations;

import com.sbank.controller.request.NewAccountRequest;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sbank.exception.base.ValidationMessages.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountCreationValidationTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private void singleFieldFailureCase(NewAccountRequest TEN, String name, String must_not_be_blank) {
        //given
        NewAccountRequest newAccountRequest = TEN;
        //when
        Set<ConstraintViolation<NewAccountRequest>> violations = validator.validate(newAccountRequest);
        //then
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<NewAccountRequest> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo(name);
        assertThat(violation.getMessage()).isEqualTo(must_not_be_blank);
    }

    @Test
    public void givenNullNameThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest(null, "Palm Jumairah, Dubai, UAE", BigDecimal.TEN), FIELD_NAME, MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenEmptyNameThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("", "Palm Jumairah, Dubai, UAE", BigDecimal.TEN), FIELD_NAME, MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenAllSpacesNameThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("  ", "Palm Jumairah, Dubai, UAE", BigDecimal.TEN), FIELD_NAME, MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenNullAddressThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("bill gates", null, BigDecimal.TEN), FIELD_ADDRESS, MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenEmptyAddressThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("bill gates", "", BigDecimal.TEN), FIELD_ADDRESS, MSG_MUST_NOT_BE_EMPTY);
    }
    
    @Test
    public void givenAllSpacesInAddressThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("bill gates", "  ", BigDecimal.TEN), FIELD_ADDRESS, MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenNullBalanceThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("bill gates", "address x", null), FIELD_BALANCE, MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenNegativeBalanceThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("bill gates", "address x", BigDecimal.valueOf(-1)), FIELD_BALANCE, MSG_MUST_BE_POSITIVE);
    }

    @Test
    public void givenZeroBalanceThenNewAccountRequestValidationFails() {
        singleFieldFailureCase(new NewAccountRequest("bill gates", "address x", BigDecimal.ZERO), FIELD_BALANCE, MSG_MUST_BE_POSITIVE);
    }
    
    @Test
    public void givenEmptyNameAndAddressAndBalanceThenNewAccountRequestValidationFailsWithThreeViolations() {
        //given
        NewAccountRequest newAccountRequest = new NewAccountRequest(null,null,null);
        //when
        Set<ConstraintViolation<NewAccountRequest>> violations = validator.validate(newAccountRequest);
        //then
        assertThat(violations.stream().map(v-> v.getPropertyPath().toString()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(FIELD_BALANCE, FIELD_NAME, FIELD_ADDRESS);
        assertThat(violations.stream().map(v-> v.getMessage()).collect(Collectors.toSet()))
                .containsExactly(MSG_MUST_NOT_BE_EMPTY);
    }

    @Test
    public void givenFullNameAndAddressAndPositiveBalanceThenNewAccountRequestValidationPasses() {
        //given
        NewAccountRequest newAccountRequest = new NewAccountRequest("bill gates","USA",BigDecimal.TEN);
        //when
        Set<ConstraintViolation<NewAccountRequest>> violations = validator.validate(newAccountRequest);
        //then
        assertThat(violations.size()).isEqualTo(0);
    }

}
