package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    int findBalanceById(int userId);
    boolean addBalance(int balanceToAdd, int userId);
    boolean subtractBalance(int balanceToSubtract, int userId);
    Account findAccountById(int userId);

}
