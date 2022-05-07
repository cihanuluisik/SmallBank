package com.sbank.exception;

import com.sbank.exception.base.SmallBankBaseException;
import org.springframework.http.HttpStatus;

import static com.sbank.exception.base.ValidationMessages.FIELD_ACCOUNT;
import static com.sbank.exception.base.ValidationMessages.MSG_ACCOUNT_NOT_FOUND;

public class AccountNotFoundException extends SmallBankBaseException {
    public AccountNotFoundException() {
        super(FIELD_ACCOUNT, MSG_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
