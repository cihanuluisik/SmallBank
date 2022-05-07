package com.sbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    @JsonIgnore
    public final Lock lock = new ReentrantLock();

    private final String name, address;

    private BigDecimal balance;

    private final Long accountNo;

    private final CurrencyUnit currency = CurrencyUnit.AED;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Account(String name, String address, BigDecimal balance, Long accountNo) {
        this.name = name;
        this.address = address;
        this.balance = balance;
        this.accountNo = accountNo;
    }

    public Long getAccountNo() {
        return accountNo;
    }

    public CurrencyUnit getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNo.equals(account.accountNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNo);
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }
}

