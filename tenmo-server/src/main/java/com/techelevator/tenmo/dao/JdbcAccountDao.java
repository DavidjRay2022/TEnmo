package com.techelevator.tenmo.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

@Service
public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal findBalanceById(int userId) {
        //if (userId == null) throw new IllegalArgumentException("Username cannot be null");
        int balance;
        try {
            balance = jdbcTemplate.queryForObject("SELECT balance FROM account WHERE user_id = ?", int.class);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException(userId);
            //find a new exception to throw
        }
        return BigDecimal.valueOf(balance);
    }

    @Override
    public boolean addBalance(BigDecimal balanceToAdd, int userId) {
        BigDecimal balance = findBalanceById(userId);
        balance.add(balanceToAdd);
        String sql = "UPDATE account SET balance = ?, WHERE user_id = ?";
        return jdbcTemplate.update(sql,balance,userId) == 1;

        //TODO learn about BigDecimal see why .add is ignored
    }

    @Override
    public boolean subtractBalance(BigDecimal balanceToSubtract, int userId) {
        BigDecimal balance = findBalanceById(userId);
        balance.subtract(balanceToSubtract);
        String sql = "UPDATE account SET balance = ?. where user_id = ?";
        return jdbcTemplate.update(sql, balance, userId) == 1;
    }

    //rewrite of findAccountById using the mapRow
    @Override
    public Account findAccountById(int userId){
        String sql = "SELECT user_id, account_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToUser(results);
        } else {
            return null;
        }
    }


    private Account mapRowToUser(SqlRowSet rs) {
        Account account = new Account();
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getInt("balance"));
        account.setAccountId(rs.getInt("account_id"));

        return account;
    }
}
