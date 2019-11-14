package com.github.diegopacheco.xunit.testing.bank.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
