package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestTransferService implements  TransferService{

    final private int send = 2;
    final private int request =1;
    final private int pending =1;
    final private int approved = 2;
    final private int rejected = 3;

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
                transferDao.createSentTransfer(transfer);
                accountDao.subtractBalance(transfer.getAmount(), transfer.getAccountFrom());
                accountDao.addBalance(transfer.getAmount(), transfer.getAccountTo());
            }else {
                throw new InsufficientFunds();
        }
        }
        if(transfer.getTransferTypeId() == request){
            if(transfer.getAmount().compareTo(accountDao.findBalanceById(transfer.getAccountTo())) != 1){
                 transferDao.createRequestTransfer(transfer);
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
    public void approveTransfer(int id) throws InsufficientFunds {
        /*
        Check the amount in balance compared to transfer
        if sufficient balance, allow the transfer and update that specific transfer to transfer
         */
        Transfer transfer = getTransferById(id);


            if(transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1){ //checks if its a request and a pending transfer.
                if((transfer.getAmount().compareTo(accountDao.findBalanceById(accountDao.findUserIdByAccountNumber(transfer.getAccountTo())))  !=1 )){ //check for sufficient funds
                    accountDao.addBalance(transfer.getAmount(), accountDao.findUserIdByAccountNumber(transfer.getAccountFrom()));
                    accountDao.subtractBalance(transfer.getAmount(), accountDao.findUserIdByAccountNumber(transfer.getAccountTo()));
                    transfer.setTransferStatusId(approved);
                    transferDao.updateTransfer(transfer);
                }else {
                    throw new InsufficientFunds();
                }

            } else {
                //return some error?
                throw new IllegalArgumentException();  //TODO do we this this is the correct thing to throw?
            }

    }

    //TODO incomplete like above
    public void rejectTransfer(int transferId){
        Transfer transfer =getTransferById(transferId);
        if(transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1){ //checks if its a request and a pending transfer.
            transfer.setTransferStatusId(3); //sets status to rejected.
        } else {
            //return some error?
        }
    }

   @Override
    public Transfer getTransferById(int id){
        return transferDao.findById(id);
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
