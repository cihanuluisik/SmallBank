package com.sbank.validations.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbank.SmallBankApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;

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

}
