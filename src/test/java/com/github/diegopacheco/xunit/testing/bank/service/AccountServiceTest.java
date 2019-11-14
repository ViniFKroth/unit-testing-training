package com.github.diegopacheco.xunit.testing.bank.service;


import com.github.diegopacheco.xunit.testing.bank.exception.AccountNotFoundException;
import com.github.diegopacheco.xunit.testing.bank.exception.InvalidNullElement;
import com.github.diegopacheco.xunit.testing.bank.exception.ValueTooSmallException;
import com.github.diegopacheco.xunit.testing.bank.model.Account;
import com.github.diegopacheco.xunit.testing.bank.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AccountServiceTest {

    private AccountService accountService;


    @BeforeEach
    public void setup() {
        accountService = new AccountService();
    }

    @Test
    public void testCreationSuccess() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal balance = new BigDecimal(150.30);
        Account expected = new Account(0, user, balance, "normal");

        Account actual = accountService.createAccount(user, balance, "normal");

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBalance(), actual.getBalance());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getUser().getEmail(), actual.getUser().getEmail());
        assertEquals(expected.getUser().getName(), actual.getUser().getName());
        assertEquals(1, accountService.getAccounts().size());

    }

    @Test
    public void testCreationSuccessWithBigValue() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal balance = new BigDecimal(900000000.23);
        Account expected = new Account(0, user, balance, "normal");

        Account actual = accountService.createAccount(user, balance, "normal");

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBalance(), actual.getBalance());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getUser().getEmail(), actual.getUser().getEmail());
        assertEquals(expected.getUser().getName(), actual.getUser().getName());
        assertEquals(1, accountService.getAccounts().size());

    }

    @Test
    public void testCreationSuccessWithTwoAccounts() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal balance = new BigDecimal(150.30);
        Account expected = new Account(0, user, balance, "normal");
        Account expected2 = new Account(1, user, balance, "savings");

        Account actual = accountService.createAccount(user, balance, "normal");

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBalance(), actual.getBalance());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getUser().getEmail(), actual.getUser().getEmail());
        assertEquals(expected.getUser().getName(), actual.getUser().getName());
        assertEquals(1, accountService.getAccounts().size());

        Account actual2 = accountService.createAccount(user, balance, "savings");

        assertEquals(expected2.getId(), actual2.getId());
        assertEquals(expected2.getBalance(), actual2.getBalance());
        assertEquals(expected2.getType(), actual2.getType());
        assertEquals(expected2.getUser().getEmail(), actual2.getUser().getEmail());
        assertEquals(expected2.getUser().getName(), actual2.getUser().getName());
        assertEquals(2, accountService.getAccounts().size());

    }

    @Test
    public void testCreationFailWhenInitialValueLessThen100() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal balance = new BigDecimal(90.0);
        assertThrows(ValueTooSmallException.class, () -> accountService.createAccount(user, balance, "normal"));
    }

    @Test
    public void testCreationFailWhenInitialValueIsNegative() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal balance = BigDecimal.valueOf(-150);
        assertThrows(ValueTooSmallException.class, () -> accountService.createAccount(user, balance, "normal"));
    }

    @Test
    public void testCreationFailWhenUserIsNull() throws Exception {
        BigDecimal balance = new BigDecimal(150.0);
        assertThrows(InvalidNullElement.class, () -> accountService.createAccount(null, balance, "normal"));
    }

    @Test
    public void testCreationFailWhenUserNameIsNull() throws Exception {
        User user = new User(null, "joao@gmail.com");
        BigDecimal balance = new BigDecimal(150.0);
        assertThrows(InvalidNullElement.class, () -> accountService.createAccount(user, balance, "normal"));
    }

    @Test
    public void testCreationFailWhenEmailIsNull() throws Exception {
        User user = new User("Joao", null);
        BigDecimal balance = new BigDecimal(150.0);
        assertThrows(InvalidNullElement.class, () -> accountService.createAccount(user, balance, "normal"));
    }

    @Test
    public void testCreationFailWhenTypelIsNull() throws Exception {
        User user = new User("Joao", "email");
        BigDecimal balance = new BigDecimal(150.0);
        assertThrows(InvalidNullElement.class, () -> accountService.createAccount(user, balance, null));
    }

    @Test
    public void testCreationFailWhenInitialValueIsNull() throws Exception {
        User user = new User("Joao", "email");
        assertThrows(InvalidNullElement.class, () -> accountService.createAccount(user, null, "normal"));
    }


    @Test
    public void testDepositSuccess() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal initialDeposit = BigDecimal.valueOf(150.30);
        Account account = accountService.createAccount(user, initialDeposit, "normal");
        BigDecimal depositValue = BigDecimal.valueOf(50.0);
        BigDecimal totalExpectedValue = initialDeposit.add(depositValue);

        assertEquals(totalExpectedValue.doubleValue(), accountService.deposit(account.getId(), depositValue).doubleValue(), 0.01);
        assertEquals(totalExpectedValue.doubleValue(), accountService.getAccounts().get(0).getBalance().doubleValue(), 0.01);
    }

    @Test
    public void testDepositSuccessWithTwoTransactions() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal initialDeposit = BigDecimal.valueOf(150.30);
        Account account = accountService.createAccount(user, initialDeposit, "normal");
        BigDecimal firstDepositValue = BigDecimal.valueOf(50.0);
        BigDecimal secondDepositeValue = BigDecimal.valueOf(1000.0);

        BigDecimal totalExpectedValue = initialDeposit.add(firstDepositValue).add(secondDepositeValue);

        assertEquals(initialDeposit.add(firstDepositValue).doubleValue(), accountService.deposit(account.getId(), firstDepositValue).doubleValue(), 0.01);
        assertEquals(totalExpectedValue.doubleValue(), accountService.deposit(account.getId(), secondDepositeValue).doubleValue(), 0.01);
        assertEquals(totalExpectedValue.doubleValue(), accountService.getAccounts().get(0).getBalance().doubleValue(), 0.01);
    }

    @Test
    public void testDepositSuccessWithBigValue() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal initialDeposit = BigDecimal.valueOf(999999999.99);
        Account account = accountService.createAccount(user, initialDeposit, "normal");
        BigDecimal depositValue = BigDecimal.valueOf(50.0);
        BigDecimal totalExpectedValue = initialDeposit.add(depositValue);

        assertEquals(totalExpectedValue.doubleValue(), accountService.deposit(account.getId(), depositValue).doubleValue(), 0.01);
        assertEquals(totalExpectedValue.doubleValue(), accountService.getAccounts().get(0).getBalance().doubleValue(), 0.01);
    }

    @Test
    public void testDepositFailWhenValueIsZero() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal initialDeposit = new BigDecimal(150.30);
        Account account = accountService.createAccount(user, initialDeposit, "normal");
        assertThrows(ValueTooSmallException.class, () -> accountService.deposit(account.getId(), BigDecimal.ZERO));
    }

    @Test
    public void testDepositFailWhenValueIsNegative() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal initialDeposit = new BigDecimal(150.30);
        Account account = accountService.createAccount(user, initialDeposit, "normal");

        assertThrows(ValueTooSmallException.class, () -> accountService.deposit(account.getId(), BigDecimal.valueOf(-500)));

    }

    @Test
    public void testDepositFailWhenIdDoesNotExist() throws Exception {
        User user = new User("João", "joao@gmail.com");
        BigDecimal initialDeposit = new BigDecimal(150.30);
        Account account = accountService.createAccount(user, initialDeposit, "normal");
        assertThrows(AccountNotFoundException.class, () -> accountService.deposit(22, new BigDecimal(500)));

    }

    @Test
    void testCheckBalanceSuccess() throws Exception {
        User user = new User("Talita", "talita@gmail.com");
        BigDecimal balance = new BigDecimal(230.30);
        Account account = accountService.createAccount(user, balance, "normal");

        assertEquals(balance, accountService.checkBalance(account.getId()));
    }

    @Test
    void testCheckBalanceFailWhenIdIsNull() throws Exception {
        User user = new User("Talita", "talita@gmail.com");
        BigDecimal balance = new BigDecimal(230.30);
        Account account = accountService.createAccount(user, balance, "normal");

        assertThrows(InvalidNullElement.class, () -> accountService.checkBalance(null));
    }

    @Test
    void testCheckBalanceFailWhenAccountDoesNotExist() throws Exception {
        User user = new User("Talita", "talita@gmail.com");
        BigDecimal balance = new BigDecimal(230.30);
        Account account = accountService.createAccount(user, balance, "normal");

        assertThrows(AccountNotFoundException.class, () -> accountService.checkBalance(10));
    }

}