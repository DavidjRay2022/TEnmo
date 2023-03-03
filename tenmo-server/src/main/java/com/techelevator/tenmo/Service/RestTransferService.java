package com.techelevator.tenmo.Service;

import com.techelevator.tenmo.dao.TransferDao;

import java.math.BigDecimal;

public class RestTransferService implements  TransferService{

    TransferDao transferDao;

    @Override
    public void createTransfer(int transferType, int accountFrom, int accountTo, BigDecimal amount) {
        transferDao.create(transferType, accountFrom, accountTo, amount);

    }
}
