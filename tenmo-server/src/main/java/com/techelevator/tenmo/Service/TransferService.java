package com.techelevator.tenmo.Service;

import java.math.BigDecimal;

public interface TransferService {

    void createTransfer(int transferType, int accountFrom, int accountTo, BigDecimal amount);

}
