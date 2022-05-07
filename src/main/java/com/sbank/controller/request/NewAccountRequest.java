package com.sbank.controller.request;

import com.sbank.domain.Account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import static com.sbank.exception.base.ValidationMessages.MSG_MUST_BE_POSITIVE;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_NOT_BE_EMPTY;


public class NewAccountRequest {
    public static AtomicLong accountNoGenerator = new AtomicLong(1);

    @NotBlank(message = MSG_MUST_NOT_BE_EMPTY)
    private  String name, address;

    @NotNull(message = MSG_MUST_NOT_BE_EMPTY)
    @Positive(message = MSG_MUST_BE_POSITIVE)
    private  BigDecimal balance;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public NewAccountRequest() {
    }

    public NewAccountRequest(String name, String address, BigDecimal balance) {
        this.name = name;
        this.address = address;
        this.balance = balance;
    }

    public Account toNewAccount() {
        return new Account(name,address,balance, accountNoGenerator.getAndIncrement());
    }
}
