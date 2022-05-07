package com.sbank.validations.transaction;


import com.sbank.controller.request.InternationalTransferRequest;
import com.sbank.validations.base.ValidationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.*;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InternationalTransferValidationTest extends ValidationTestBase {

    @Test
    public void givenNegativeSourceAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/-1/transfer-international")
                .content(json(new InternationalTransferRequest(TEN, "AE550030010974907920001", "ADCBAEAA")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenZeroSourceAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/0/transfer-international")
                        .content(json(new InternationalTransferRequest(TEN, "AE550030010974907920001", "ADCBAEAA")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

    @Test
    public void givenNullOrBlankTargetAccountNoThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/100/transfer-international")
                        .content(json(new InternationalTransferRequest(TEN, null, "ADCBAEAA")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).contains(MSG_MUST_NOT_BE_EMPTY));

        mockMvc.perform(patch("/api/v1/accounts/100/transfer-international")
                        .content(json(new InternationalTransferRequest(TEN, "", "ADCBAEAA")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).contains(MSG_MUST_NOT_BE_EMPTY));

        mockMvc.perform(patch("/api/v1/accounts/100/transfer-international")
                        .content(json(new InternationalTransferRequest(TEN, " ", "ADCBAEAA")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).contains(MSG_MUST_NOT_BE_EMPTY));
    }

    @Test
    public void givenNullOrBlankSwiftcodeThenGivesValidationError() throws Exception {
        mockMvc.perform(patch("/api/v1/accounts/100/transfer-international")
                        .content(json(new InternationalTransferRequest(TEN, "AE550030010974907920001", null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).contains(MSG_MUST_NOT_BE_EMPTY));

        mockMvc.perform(patch("/api/v1/accounts/100/transfer-international")
                        .content(json(new InternationalTransferRequest(TEN, "AE550030010974907920001", "")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).contains(MSG_MUST_NOT_BE_EMPTY));

        mockMvc.perform(patch("/api/v1/accounts/100/transfer-international")
                        .content(json(new InternationalTransferRequest(TEN, "AE550030010974907920001", " ")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).contains(MSG_MUST_NOT_BE_EMPTY));
    }

}
