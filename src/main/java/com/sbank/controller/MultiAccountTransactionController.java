package com.sbank.controller;

import com.sbank.controller.request.InternationalTransferRequest;
import com.sbank.service.MultiAccountTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_BE_POSITIVE;

@RestController
@RequestMapping("/api/v1")
@Validated
public class MultiAccountTransactionController {

    @Autowired
    MultiAccountTransactionManager accountTransactionManager;

    @PatchMapping("/accounts/{sourceAccountNo}/transfer/{targetAccountNo}")
    BigDecimal transfer(
                @Positive(message = MSG_INVALID_ACCOUNT_NUMBER)
                @PathVariable("sourceAccountNo") Long sourceAccountNo,
                @Positive(message = MSG_INVALID_ACCOUNT_NUMBER)
                @PathVariable("targetAccountNo") Long targetAccountNo,
                @Positive(message = MSG_MUST_BE_POSITIVE)
                @RequestBody BigDecimal amount) throws InterruptedException {
        return accountTransactionManager.transfer(sourceAccountNo, targetAccountNo, amount);
    }

    @PatchMapping("/accounts/{sourceAccountNo}/transfer-international")
    BigDecimal internatinalTransfer(
                @Positive(message = MSG_INVALID_ACCOUNT_NUMBER)
                @PathVariable("sourceAccountNo") Long sourceAccountNo,
                @Valid @RequestBody InternationalTransferRequest request) throws InterruptedException {
        return accountTransactionManager.transferInternational(sourceAccountNo, request);
    }

}
