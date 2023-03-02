package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class AccountController {
    //private UserDao userDao;
    private AccountDao dao;

    public AccountController(){
        this.dao = new JdbcAccountDao(new JdbcTemplate()); //is this how that works??
    }


    //add transfer dao

    //needed paths
    /*
    Create
    Request?
    Update
    Delete
     */

    //returns the entire account object
    //check if correct. If so add try-catch

    @PreAuthorize("permitAll()") //change
    @RequestMapping(path = "{userId}", method = RequestMethod.GET)
    public Account get(@PathVariable int userId){
        return dao.findAccountById(userId);
    }



    //Make sure all these are needed
    @PreAuthorize("permitAll()")
    @RequestMapping
    public Account create(){return null;}

    @PreAuthorize("permitAll()")
    @RequestMapping
    public Account update(){return null;}

    @PreAuthorize("permitAll()")
    @RequestMapping
    public Account delete(){return null;}







    public void transferAmount(){

    }
    public void seeTransfers(){

    }
    public void seeTransferById(){

    }

}
