package com.sbank.service;

import com.sbank.domain.Account;
import com.sbank.exception.AccountBalanceInsufficientException;
import com.sbank.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SingleAccountTransactionManagerImpl implements SingleAccountTransactionManager {

    @Autowired
    LockAndRunManager lockAndRunManager;

    @Autowired
    CrudRepository<Account, Long> accountRepo;

    @Override
    public BigDecimal deposit(Long accountNo, final BigDecimal amount) throws InterruptedException {
        Optional<Account> accountOptional = accountRepo.findById(accountNo);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            lockAndRunManager.lockAndRun(account.lock, ()-> {
                account.setBalance(account.getBalance().add(amount));
                accountRepo.save(account);
            });
            return account.getBalance();
        } else throw new AccountNotFoundException();
    }

    @Override
    public BigDecimal withdraw(Long accountNo, BigDecimal amount) throws InterruptedException {
        Optional<Account> accountOptional = accountRepo.findById(accountNo);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            lockAndRunManager.lockAndRun(account.lock, ()-> {
                    if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0)
                        throw new AccountBalanceInsufficientException();
                    account.setBalance(account.getBalance().subtract(amount));
                    accountRepo.save(account);
            });
            return account.getBalance();
        } else throw new AccountNotFoundException();
    }
}
