package com.techelevator.tenmo.Service;

import java.math.BigDecimal;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;

public class RestAccountService implements  AccountService{


    AccountDao accountDAO;

    @Override
    public BigDecimal balance() {
        System.out.println(principal.getName());
        return accountDAO.getBalance(principal.getName());
    }
}
