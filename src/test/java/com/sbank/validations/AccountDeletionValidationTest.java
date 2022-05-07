package com.sbank.validations;


import com.sbank.validations.base.ValidationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.validation.ConstraintViolationException;

import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AccountDeletionValidationTest extends ValidationTestBase {

    @Test
    public void givenNegativeAccountNoThenGivesValidationError() throws Exception {
        assertErrorInDeleteResponse("/api/v1/accounts/-1");
    }

    @Test
    public void givenZeroAccountNoThenGivesValidationError() throws Exception {
        assertErrorInDeleteResponse("/api/v1/accounts/0");
    }

    private void assertErrorInDeleteResponse(String urlTemplate) throws Exception {
        mockMvc.perform(delete(urlTemplate).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).endsWith(MSG_INVALID_ACCOUNT_NUMBER));
    }

}
