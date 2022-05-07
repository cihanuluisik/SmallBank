package com.sbank.exception;

import com.sbank.exception.base.SmallBankBaseException;
import com.sbank.exception.base.ValidationMessages;
import org.springframework.http.HttpStatus;

public class InvalidAccountNumberException extends SmallBankBaseException {

    public Long getAccountNo() {
        return accountNo;
    }

    private final Long accountNo;

    public InvalidAccountNumberException(Long accountNo) {
        super("", ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
        this.accountNo = accountNo;
    }
}
