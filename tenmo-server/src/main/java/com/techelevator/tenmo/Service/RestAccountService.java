package com.techelevator.tenmo.Service;

import java.math.BigDecimal;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import com.techelevator.tenmo.model.Account;
import org.springframework.stereotype.Service;

@Service
public class RestAccountService implements  AccountService{


    AccountDao accountDAO;

    public RestAccountService(AccountDao accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public BigDecimal balance(int id) {
       return accountDAO.findBalanceById(id);
    }

    @Override
    public Account findAccountById(int id){
       return accountDAO.findAccountById(id);
    }
}
