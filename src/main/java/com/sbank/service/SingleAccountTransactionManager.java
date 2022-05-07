package com.sbank.service;

import java.math.BigDecimal;

public interface SingleAccountTransactionManager {

    BigDecimal deposit(Long accountNo, BigDecimal amount) throws InterruptedException;

    BigDecimal withdraw(Long accountNo, BigDecimal amount) throws InterruptedException;

}
