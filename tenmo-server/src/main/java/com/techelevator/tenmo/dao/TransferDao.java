package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    boolean create(int transferType, int accountFrom, int accountTo, BigDecimal amount);
    List<Transfer> findAll(int id);
    Transfer findByToId(int id);
    List<Transfer> findByPayee(int accountTo);
    List<Transfer> getPendingRequests(int id);
    public List<Transfer> getPendingSentRequests(int id);
    public List<Transfer> getPendingReceivedRequests(int id);

}
