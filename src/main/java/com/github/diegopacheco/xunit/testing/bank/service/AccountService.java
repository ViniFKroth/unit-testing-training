package com.github.diegopacheco.xunit.testing.bank.service;

import com.github.diegopacheco.xunit.testing.bank.exception.AccountNotFoundException;
import com.github.diegopacheco.xunit.testing.bank.exception.InvalidNullElement;
import com.github.diegopacheco.xunit.testing.bank.exception.ValueTooSmallException;
import com.github.diegopacheco.xunit.testing.bank.model.Account;
import com.github.diegopacheco.xunit.testing.bank.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountService {

    private List<Account> accounts = new ArrayList<>();
    private Integer id = 0;

    //TODO add validation for types of account
    public Account createAccount(User user, BigDecimal initialValue, String type) throws Exception {
        if (initialValue == null)
            throw new InvalidNullElement("Initial value cannot be null");

        if (initialValue.doubleValue() < 100.0)
            throw new ValueTooSmallException("Initial value too small. It needs to be at least 100");

        if (user == null)
            throw new InvalidNullElement("User cannot be null");

        if (user.getEmail() == null || user.getName() == null)
            throw new InvalidNullElement("User id field cannot be null");

        if (type == null)
            throw new InvalidNullElement("Type cannot be null");


        Account account = new Account(id, user, initialValue, type);
        accounts.add(account);
        id++;
        return account;

    }


    //Minimum deposit value is of 1 cent
    public BigDecimal deposit(Integer accountId, BigDecimal value) throws AccountNotFoundException, ValueTooSmallException {
        if (value.doubleValue() < 0.01)
            throw new ValueTooSmallException("Deposit value too small, it needs to be at least 0.01");

        Account account = findAccountById(accountId);
        account.setBalance(new BigDecimal(account.getBalance().doubleValue() + value.doubleValue()));

        return account.getBalance();
    }

    //TODO Enhance return type
    public BigDecimal checkBalance(Integer accountId) throws InvalidNullElement, AccountNotFoundException {
        if (accountId == null)
            throw new InvalidNullElement("AccountId cannot be null");
        Account account = findAccountById(accountId);
        return account.getBalance();
    }

    private Account findAccountById(Integer id) throws AccountNotFoundException {
        Optional<Account> account = accounts.stream()
                .filter(foundAccount -> foundAccount.getId().equals(id))
                .findFirst();

        if (!account.isPresent())
            throw new AccountNotFoundException("An account with this id does not exist");

        return account.get();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

}