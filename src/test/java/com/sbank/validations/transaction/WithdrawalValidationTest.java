package com.sbank.validations.transaction;


import com.sbank.validations.base.ValidationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_BE_POSITIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WithdrawalValidationTest  extends ValidationTestBase {

    @Test
    public void givenNegativeAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/-1/withdraw")
                .content(json(BigDecimal.TEN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenZeroAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/0/withdraw")
                .content(json(BigDecimal.TEN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenNegativeAmountToDepositThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/1000/withdraw")
                .content(json(new BigDecimal("-1")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_MUST_BE_POSITIVE));
    }

    @Test
    public void givenZeroAmountToDepositThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/1000/withdraw")
                .content(json(BigDecimal.ZERO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_MUST_BE_POSITIVE));
    }
}
