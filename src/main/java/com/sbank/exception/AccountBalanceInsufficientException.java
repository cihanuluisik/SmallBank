package com.sbank.exception;

import com.sbank.exception.base.SmallBankBaseException;
import com.sbank.exception.base.ValidationMessages;
import org.springframework.http.HttpStatus;

import static com.sbank.exception.base.ValidationMessages.MSG_ACCOUNT_BALANCE_INSUFFICIENT;

public class AccountBalanceInsufficientException extends SmallBankBaseException {
    public AccountBalanceInsufficientException() {
        super(ValidationMessages.FIELD_BALANCE, MSG_ACCOUNT_BALANCE_INSUFFICIENT, HttpStatus.BAD_REQUEST);
    }
}
