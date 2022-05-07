package com.sbank.controller;

import com.sbank.controller.request.NewAccountRequest;
import com.sbank.domain.Account;
import com.sbank.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.sbank.exception.base.ValidationMessages.MSG_INVALID_ACCOUNT_NUMBER;

@RestController
@RequestMapping("/api/v1")
@Validated
public class AccountController {

    @Autowired
    CrudRepository<Account, Long> accountRepo;

    @GetMapping("/accounts")
    List<Account> listAll() {
        Iterable<Account> all = accountRepo.findAll();
        return StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
    }

    @PostMapping("/accounts")
    ResponseEntity<Long> create(@Valid @RequestBody NewAccountRequest newAccountRequest) {
        Account account = accountRepo.save(newAccountRequest.toNewAccount());
        return new ResponseEntity(account.getAccountNo(), HttpStatus.CREATED);
    }

    @DeleteMapping("/accounts/{accountNo}")
    void delete(@Positive(message = MSG_INVALID_ACCOUNT_NUMBER)
                @PathVariable("accountNo") Long accountNo) {
        accountRepo.deleteById(accountNo);
    }

    @GetMapping("/accounts/{accountNo}/balance")
    BigDecimal getBalance(@Positive(message = MSG_INVALID_ACCOUNT_NUMBER)
                          @PathVariable("accountNo") Long accountNo) {
        Optional<Account> accountOptional = accountRepo.findById(accountNo);
        if (accountOptional.isPresent()) {
            return accountOptional.get().getBalance();
        } else throw new AccountNotFoundException();
    }
}
