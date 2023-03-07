package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    boolean createSentTransfer(Transfer transfer);
    boolean createRequestTransfer(Transfer transfer);
    List<Transfer> findAll(int id);
    Transfer findById(int id);
    List<Transfer> findByPayee(int accountTo);
    List<Transfer> getPendingRequests(int id);
    public List<Transfer> getPendingSentRequests(int id);
    public List<Transfer> getPendingReceivedRequests(int id);
    public int getAccountFromUserId(int id);

    void updateTransfer(Transfer transfer);
}
