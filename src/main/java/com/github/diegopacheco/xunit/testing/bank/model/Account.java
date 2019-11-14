package com.github.diegopacheco.xunit.testing.bank.model;

import java.math.BigDecimal;

public class Account {

    private Integer id;
    private User user;
    private BigDecimal balance;
    private String type;


    public Account(Integer id, User user, BigDecimal balance, String type) throws Exception {
        this.id = id;
        this.user = user;
        this.balance = balance;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal checkBalance() {
        return this.balance;
    }

}