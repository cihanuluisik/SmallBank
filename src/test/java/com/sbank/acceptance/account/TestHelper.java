package com.sbank.acceptance.account;


import com.sbank.acceptance.account.transaction.international.MockedInternatioalTransferListener;
import com.sbank.controller.error.ApiError;
import com.sbank.controller.request.InternationalTransferRequest;
import com.sbank.domain.international.InternationalTransferEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class TestHelper {

    @Autowired
    MockedInternatioalTransferListener swiftMessageListener;


    public void assertStatusAndMessage(ResponseEntity<ApiError[]> result, HttpStatus httpStatus, String errorMessage) {
        assertThat(result.getStatusCode()).isEqualTo(httpStatus);
         assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
         ApiError[] apiErrors = result.getBody();
         assertThat(apiErrors.length).isEqualTo(1);
         assertThat(apiErrors[0].getMessage()).isEqualTo(errorMessage);
    }

    public void assertStatusAndMessageAndField(ResponseEntity<ApiError[]> result, HttpStatus httpStatus, String errorMessage, String fieldName) {
        assertStatusAndMessage(result, httpStatus, errorMessage);
        assertThat(result.getBody()[0].getField()).isEqualTo(fieldName);
    }

    public void assertMessageReceivedBySwift(InternationalTransferRequest internationalTransferRequest) {
        InternationalTransferEvent messageReceived = swiftMessageListener.getNextMessage();
        assertThat(swiftMessageListener.getNextMessage().getToIban()).isEqualTo(internationalTransferRequest.getTargetAccountIBAN());
        assertThat(messageReceived.getAmount()).isEqualTo(internationalTransferRequest.getAmount());
        assertThat(messageReceived.getToSwiftCode()).isEqualTo(internationalTransferRequest.getTargetAccountSwiftCode());
    }

    public void assertResponseBody(ResponseEntity<BigDecimal> response, Object body) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(body);
    }


}
