package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal findBalanceById(int userId);
    boolean addBalance(BigDecimal balanceToAdd, int userId);
    boolean subtractBalance(BigDecimal balanceToSubtract, int userId);
    Account findAccountById(int userId);

}
