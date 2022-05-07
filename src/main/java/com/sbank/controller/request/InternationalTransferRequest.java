package com.sbank.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_MUST_BE_POSITIVE;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_NOT_BE_EMPTY;

public class InternationalTransferRequest {

    @NotNull(message = MSG_MUST_NOT_BE_EMPTY)
    @Positive(message = MSG_MUST_BE_POSITIVE)
    private  BigDecimal amount;

    @NotBlank(message = MSG_MUST_NOT_BE_EMPTY)
    private  String targetAccountIBAN, targetAccountSwiftCode;

    private InternationalTransferRequest() {
    }

    public InternationalTransferRequest(BigDecimal amount, String targetAccountIBAN, String targetAccountSwiftCode) {
        this.amount = amount;
        this.targetAccountIBAN = targetAccountIBAN;
        this.targetAccountSwiftCode = targetAccountSwiftCode;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public String getTargetAccountIBAN() {
        return targetAccountIBAN;
    }

    public String getTargetAccountSwiftCode() {
        return targetAccountSwiftCode;
    }

}
