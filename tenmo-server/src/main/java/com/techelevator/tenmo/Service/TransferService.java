package com.techelevator.tenmo.Service;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.PathVariable;

public interface TransferService {

    void createTransfer(int transferType, int accountFrom, int accountTo, BigDecimal amount);

    List<Transfer> getTransfersByUserId(@PathVariable int userId);

}
