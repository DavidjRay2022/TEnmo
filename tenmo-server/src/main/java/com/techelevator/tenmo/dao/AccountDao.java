package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal findBalanceById(int userId);
    boolean addBalance(int balanceToAdd, int userId);
    boolean subtractBalance(int balanceToSubtract, int userId);
    Account findAccountById(int userId);

}
