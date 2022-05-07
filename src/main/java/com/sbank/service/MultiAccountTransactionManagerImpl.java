package com.sbank.service;

import com.sbank.controller.request.InternationalTransferRequest;
import com.sbank.domain.Account;
import com.sbank.exception.AccountBalanceInsufficientException;
import com.sbank.exception.AccountNotFoundException;
import com.sbank.service.international.InternationalTransferManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MultiAccountTransactionManagerImpl implements MultiAccountTransactionManager {

    @Autowired
    LockAndRunManager lockAndRunManager;

    @Autowired
    InternationalTransferManager internationalTransferManager;

    @Autowired
    CrudRepository<Account, Long> accountRepo;

    @Override
    public BigDecimal transfer(Long sourceAccountNo, Long targetAccountNo, BigDecimal amount) throws InterruptedException {
        Optional<Account> sourceAccountOptional = accountRepo.findById(sourceAccountNo);
        Optional<Account> targetAccountOptional = accountRepo.findById(targetAccountNo);
        if (sourceAccountOptional.isPresent() && targetAccountOptional.isPresent()) {
            Account sourceAccount = sourceAccountOptional.get();
            Account targetAccount = targetAccountOptional.get();
            lockAndRunManager.lockBothAndRun(sourceAccount.lock, targetAccount.lock,
                    () -> { if (sourceAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0)
                                    throw new AccountBalanceInsufficientException();
                        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
                        targetAccount.setBalance(targetAccount.getBalance().add(amount));
                        accountRepo.saveAll(List.of(sourceAccount, targetAccount)); // both account locked while saving
                    });
            return sourceAccount.getBalance();
        } else throw new AccountNotFoundException(); // TODO: add account no to the exception
    }

    @Override
    public BigDecimal transferInternational(Long sourceAccountNo, InternationalTransferRequest request) throws InterruptedException {
        Optional<Account> sourceAccountOptional = accountRepo.findById(sourceAccountNo);
        if (sourceAccountOptional.isPresent() ) {
            Account sourceAccount = sourceAccountOptional.get();
            lockAndRunManager.lockAndRun(sourceAccount.lock,
                    () -> { if (sourceAccount.getBalance().subtract(request.getAmount()).compareTo(BigDecimal.ZERO) < 0)
                        throw new AccountBalanceInsufficientException();
                        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
                        internationalTransferManager.sendMoney(sourceAccount, request);
                        accountRepo.save(sourceAccount);
                    });
            return sourceAccount.getBalance();
        } else throw new AccountNotFoundException();
    }
}
