package com.techelevator.tenmo.Service;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Transfer;


public interface TransferService {

    void createTransfer(Transfer transfer) throws InsufficientFunds;

    List<Transfer> getTransfersByUserId(int userId);

    Transfer getTransferById(int id);
    List<Transfer> getPendingTransfers(int id);
    List<Transfer> getSentPendingTransfer(int id);
    List<Transfer>getReceivedPendingTransfer(int id);

    //TODO added to interface.
    public void approveTransfer(int id) throws InsufficientFunds;
    public void rejectTransfer(int transferId);

}
