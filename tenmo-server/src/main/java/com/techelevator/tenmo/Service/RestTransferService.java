package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

public class RestTransferService implements  TransferService{

    TransferDao transferDao;

    @Override
    public void createTransfer(int transferType, int accountFrom, int accountTo, BigDecimal amount) {
        transferDao.create(transferType, accountFrom, accountTo, amount);

    }

    List<Transfer> getTransfersByUserId(@PathVariable int userId){

    }
}
