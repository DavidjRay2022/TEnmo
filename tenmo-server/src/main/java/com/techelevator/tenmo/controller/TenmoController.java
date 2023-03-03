package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Service.AccountService;
import com.techelevator.tenmo.Service.RestAccountService;
import com.techelevator.tenmo.Service.TransferService;
import com.techelevator.tenmo.Service.UserService;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@RestController
    @PreAuthorize("isAuthenticated()")
    public class TenmoController {





    private UserService userService;
    private AccountService accountService;
    private TransferService transferService;

    public TenmoController(UserService userService, AccountService accountService, TransferService transferService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
        public BigDecimal getBalance(@PathVariable int id) {
          return accountService.balance(id);
        }

        @RequestMapping(path="/users", method = RequestMethod.GET)
        public List<User> getUsers() {
            return userService.getUsers();
        }

        @ResponseStatus(HttpStatus.CREATED)
        @RequestMapping(path="/transfers/{id}", method = RequestMethod.POST)
        public void addTransfer(@RequestBody int transferType, int accountFrom, int accountTo, BigDecimal amount) throws InsufficientFunds {

        transferService.createTransfer(transferType, accountFrom,accountTo,amount);
        }



        @RequestMapping(path="/account/{id}", method = RequestMethod.GET)
        public Account getAccountFromAccountId(@PathVariable int id) {
            return accountService.findAccountById(id);
        }

        @RequestMapping(path="/transfers/user/{userId}", method = RequestMethod.GET)
        public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
            return transferService.getTransfersByUserId(userId);
        }

        @RequestMapping(path="/transfers/{id}", method = RequestMethod.GET)
        public Transfer getTransferById(@PathVariable int id) {
            return transferService.getTransferById(id);
        }

//
//        @RequestMapping(path="/users/{id}", method = RequestMethod.GET)
//        public User getUserByUserId(@PathVariable int id) {
//            return userDao.getUserByUserId(id);
//        }




    @RequestMapping(path="/transfers/user/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfersByUserId(@PathVariable int userId) {
            return transferService.getPendingTransfers(userId);
        }
    @RequestMapping(path="/transfers/user/{userId}/pending-sent", method = RequestMethod.GET)
    public List<Transfer> getSentPendingTransfersByUserId(@PathVariable int userId) {
        return transferService.getSentPendingTransfer(userId);
    }
    @RequestMapping(path="/transfers/user/{userId}/pending-received", method = RequestMethod.GET)
    public List<Transfer> getReceivedPendingTransfersByUserId(@PathVariable int userId) {
        return transferService.getReceivedPendingTransfer(userId);
    }

//        @RequestMapping(path="/transfers/{id}", method = RequestMethod.PUT)
//        public void updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFunds {
//
//            // only go through with the transfer if it is approved
//            if(transfer.getTransferStatusId() == transferStatusDAO.getTransferStatusByDesc("Approved").getTransferStatusId()) {
//
//                BigDecimal amountToTransfer = transfer.getAmount();
//                Account accountFrom = accountDAO.getAccountByAccountID(transfer.getAccountFrom());
//                Account accountTo = accountDAO.getAccountByAccountID(transfer.getAccountTo());
//
//                accountFrom.getBalance().sendMoney(amountToTransfer);
//                accountTo.getBalance().receiveMoney(amountToTransfer);
//
//                transferDAO.updateTransfer(transfer);
//
//                accountDAO.updateAccount(accountFrom);
//                accountDAO.updateAccount(accountTo);
//            } else {
//                transferDAO.updateTransfer(transfer);
//            }
//
//        }
}
