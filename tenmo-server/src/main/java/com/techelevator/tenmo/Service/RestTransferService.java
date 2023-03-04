package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestTransferService implements  TransferService{

    TransferDao transferDao;

    public RestTransferService(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @Override
    public void createTransfer(Transfer transfer) {
        transferDao.create(transfer);

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
