package com.techelevator.tenmo.Service;

import java.math.BigDecimal;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.techelevator.tenmo.model.Account;

public class RestAccountService implements  AccountService{


    AccountDao accountDAO;

    @Override
    public BigDecimal balance(int id) {
       return accountDAO.findBalanceById(id);
    }

    @Override
    public Account findAccountById(int id){
       return accountDAO.findAccountById(id);
    }
}
