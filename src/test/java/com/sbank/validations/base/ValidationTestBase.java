package com.sbank.validations.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbank.SmallBankApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SmallBankApplication.class)
@AutoConfigureMockMvc
public class ValidationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected String json(Object objectToWriteAsJson) throws IOException {
        return objectMapper.writeValueAsString(objectToWriteAsJson);
    }

    protected void assertErrorMessageInResponseAndConstraintException(String urlTemplate, Object body, String errorMessage) throws Exception {
        assertErrorMessageInResponse(urlTemplate, body, errorMessage)
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class));
    }

    protected ResultActions assertErrorMessageInResponse(String urlTemplate, Object body, String errorMessage) throws Exception {
        return mockMvc.perform(patch(urlTemplate)
                        .content(json(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat( result.getResolvedException().getMessage()).contains(errorMessage));
    }

}
