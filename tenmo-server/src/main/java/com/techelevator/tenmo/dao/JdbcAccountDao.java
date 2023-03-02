package com.techelevator.tenmo.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.tenmo.model.Account;
@Service
public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //TODO ADD THE ACCOUNT MAPPING

    @Override
    public int findBalanceById(int userId) {
        //if (userId == null) throw new IllegalArgumentException("Username cannot be null");
        int balance;
        try {
            balance = jdbcTemplate.queryForObject("SELECT balance FROM account WHERE user_id = ?", int.class);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException(userId);
            //find a new exception to throw
        }
        return balance;
    }

    @Override
    public boolean addBalance(int balanceToAdd, int userId) {
        int balance = findBalanceById(userId);
        balance += balanceToAdd;
        String sql = "UPDATE account SET balance = ?, WHERE user_id = ?";
        return jdbcTemplate.update(sql,balance,userId) == 1;
    }

    @Override
    public boolean subtractBalance(int balanceToSubtract, int userId) {
        int balance = findBalanceById(userId);
        balance -= balanceToSubtract;
        String sql = "UPDATE account SET balance = ?. where user_id = ?";
        return jdbcTemplate.update(sql, balance, userId) == 1;
    }

    @Override
    public Account findAccountById(int userId) {
        Account account;
        try{
            account = jdbcTemplate.queryForObject("SELECT * FROM account WHERE user_id = ?", Account.class);
        } catch (NullPointerException | EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException(userId);
        }
        return account;
    }


    //TODO ADD THE MAPPING FUNCTIONS FOR ALL
}
