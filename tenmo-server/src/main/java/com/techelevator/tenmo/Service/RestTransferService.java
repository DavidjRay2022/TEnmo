package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestTransferService implements  TransferService{

    final private int send = 2;
    final private int request =1;

    TransferDao transferDao;
    AccountDao accountDao;

    public RestTransferService(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @Override
    public void createTransfer(Transfer transfer) throws InsufficientFunds {


        if(transfer.getTransferTypeId() == send){
            if((transfer.getAmount().compareTo(accountDao.findBalanceById(transfer.getAccountFrom()))  !=1 )){ //check for sufficient funds
                transferDao.create(transfer);
                accountDao.subtractBalance(transfer.getAmount(), transfer.getAccountFrom());
                accountDao.addBalance(transfer.getAmount(), transfer.getAccountTo());
            }else {
                throw new InsufficientFunds();
        }
        }

    }

   @Override
    public List<Transfer> getTransfersByUserId(int userId){

       return transferDao.findAll(userId);

    }

   @Override
    public Transfer getTransferById(int id){
        return transferDao.findByToId(id);
    }

    @Override
    public List<Transfer> getPendingTransfers(int id){
        return transferDao.getPendingRequests(id);
    }

    @Override
    public  List<Transfer> getSentPendingTransfer(int id){
        return transferDao.getPendingSentRequests(id);
    }
    @Override
    public List<Transfer>getReceivedPendingTransfer(int id){
        return transferDao.getPendingReceivedRequests(id);
    }
}
