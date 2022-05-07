package com.sbank.service;

import com.sbank.controller.request.InternationalTransferRequest;

import java.math.BigDecimal;

public interface MultiAccountTransactionManager {

    BigDecimal transfer(Long sourceAccountNo, Long targetAccountNo, BigDecimal amount) throws InterruptedException;

    BigDecimal transferInternational(Long sourceAccountNo, InternationalTransferRequest request) throws InterruptedException;

}
