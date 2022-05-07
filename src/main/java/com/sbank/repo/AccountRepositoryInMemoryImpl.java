package com.sbank.repo;

import com.sbank.domain.Account;
import com.sbank.exception.AccountNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class AccountRepositoryInMemoryImpl implements AccountRepository {

    private final ConcurrentMap<Long, Account> accountsMap = new ConcurrentHashMap<>();

    @Override
    public  Account save(Account newAccount) {
        accountsMap.put(newAccount.getAccountNo(), newAccount);
        return newAccount;
    }

    @Override
    public void deleteById(Long accountNo) {
        if (accountsMap.remove(accountNo) == null)
            throw new AccountNotFoundException();
    }

    @Override
    public Optional<Account> findById(Long aLong) {
        Account value = accountsMap.get(aLong);
        return Optional.ofNullable(value); // TODO: return copy to separate the repo layer completely
    }

    @Override
    public boolean existsById(Long aLong) {
        return accountsMap.get(aLong) != null;
    }

    @Override
    public Iterable<Account> findAll() {
        return accountsMap.values(); // TODO: return copies of accounts to separate the repo layer completely
    }

    @Override
    public Iterable<Account> findAllById(Iterable<Long> longs) {
        // return defensive copy also to separate the repo layer and objects
        return StreamSupport.stream(longs.spliterator(), false).map(id -> accountsMap.get(id)).collect(Collectors.toSet());
    }

    @Override
    public long count() {
        return accountsMap.size();
    }

    @Override
    public void delete(Account account) {
        deleteById(account.getAccountNo());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach( id -> deleteById(id));
    }

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> accounts) {
        accounts.forEach(a -> save(a));
        return accounts;
    }

    @Override
    public void deleteAll(Iterable<? extends Account> accounts) {
        accounts.forEach( account -> delete(account));
    }

    @Override
    public void deleteAll() {
        accountsMap.clear();
    }


}
