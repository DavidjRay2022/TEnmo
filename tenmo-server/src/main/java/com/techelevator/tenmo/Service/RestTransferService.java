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
        if(transfer.getTransferTypeId() == request){
            if(transfer.getAmount().compareTo(accountDao.findBalanceById(transfer.getAccountTo())) != 1){
                 transferDao.create(transfer);
                 //TODO add function that will update the transfer when accepted or denied
            } else {
                throw new InsufficientFunds();
            }
        }

    }

   @Override
    public List<Transfer> getTransfersByUserId(int userId){

       return transferDao.findAll(userId);

    }

   //TODO unable to implement this method like I wanted, hopefully you can figure it out.
    public void approveTransfer(Transfer transfer){
        /*
        Check the amount in balance compared to transfer
        if sufficient balance, allow the transfer and update that specific transfer to transfer
         */
            //checks if the accounts of the account reciever and the userid from the url match.
            if(transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1){ //checks if its a request and a pending transfer.
                accountDao.subtractBalance(transfer.getAmount(), transfer.getAccountTo()); //subtract from the reciever of the request
                accountDao.addBalance(transfer.getAmount(), transfer.getAccountFrom()); //add the to the requester
                transfer.setTransferStatusId(2); //sets status to aproved.
            } else {
                //idk if needed.
            }


//        Transfer transfer = getTransferById(transferID);
//        if(transferDao.getAccountFromUserId(userId) == transfer.getAccountTo()){ //checks if the accounts of the account reciever and the userid from the url match.
//            if(transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1){ //checks if its a request and a pending transfer.
//                accountDao.subtractBalance(transfer.getAmount(), transfer.getAccountTo()); //subtract from the reciever of the request
//                accountDao.addBalance(transfer.getAmount(), transfer.getAccountFrom()); //add the to the requester
//                transfer.setTransferStatusId(2); //sets status to aproved.
//            } else {
//                //idk if needed.
//            }
//        }


    }

    //TODO incomplete like above
    public void rejectTransfer(int transferID, int userId){
        Transfer transfer = getTransferById(transferID);
        if(transferDao.getAccountFromUserId(userId) == transfer.getAccountTo()){ //checks if the accounts of the account reciever and the userid from the url match.
            if(transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1){ //checks if its a request and a pending transfer.
                transfer.setTransferStatusId(3); //sets status to rejected.
            } else {
                //idk if needed.
            }
        }
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
