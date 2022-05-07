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

public class TransferValidationTest extends ValidationTestBase {

    @Test
    public void givenNegativeSourceAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/-1/transfer/100")
                .content(json(BigDecimal.TEN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenZeroSourceAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/0/transfer/100")
                .content(json(BigDecimal.TEN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenNegativeTargetAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/100/transfer/-100")
                .content(json(BigDecimal.TEN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenZeroTargetAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/1000/transfer/0")
                .content(json(BigDecimal.TEN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenNegativeAmountToDepositThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/2000/transfer/100")
                .content(json(new BigDecimal("-1")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_MUST_BE_POSITIVE));
    }

    @Test
    public void givenZeroAmountToDepositThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/2000/transfer/100")
                .content(json(BigDecimal.ZERO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_MUST_BE_POSITIVE));
    }


}
