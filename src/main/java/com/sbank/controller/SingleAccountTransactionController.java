package com.sbank.controller;

import com.sbank.service.SingleAccountTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;
import static com.sbank.exception.base.ValidationMessages.MSG_MUST_BE_POSITIVE;

@RestController
@RequestMapping("/api/v1")
@Validated
public class SingleAccountTransactionController {

    @Autowired
    SingleAccountTransactionManager accountTransactionManager;

    @PatchMapping("/accounts/{accountNo}/deposit")
    BigDecimal deposit(@Positive(message = MSG_INVALID_ACCOUNT_NUMBER)
                       @PathVariable("accountNo") Long accountNo,
                       @Positive(message = MSG_MUST_BE_POSITIVE) @RequestBody BigDecimal amount) throws InterruptedException {
        return accountTransactionManager.deposit(accountNo, amount);
    }

    @PatchMapping("/accounts/{accountNo}/withdraw")
    BigDecimal withdraw(@Positive(message = MSG_INVALID_ACCOUNT_NUMBER)
                       @PathVariable("accountNo") Long accountNo,
                       @Positive(message = MSG_MUST_BE_POSITIVE) @RequestBody BigDecimal amount) throws InterruptedException {
        return accountTransactionManager.withdraw(accountNo, amount);
    }

}
