package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    final int pendingStatus = 1;
    final int approvedStatus = 2;
    final int rejectedStatus = 3;

    private final JdbcTemplate jdbcTemplate;

    private final JdbcUserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcUserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public boolean createSentTransfer(Transfer transfer) {
        if (transfer.getAccountFrom() == transfer.getAccountTo()){
            return  false;
        }



       String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) values(?, ?,?,?,?)";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), approvedStatus, getAccountFromUserId(transfer.getAccountFrom()), getAccountFromUserId(transfer.getAccountTo()), transfer.getAmount());

        return true;

    }

    public boolean createRequestTransfer(Transfer transfer) {
        if (transfer.getAccountFrom() == transfer.getAccountTo()){
            return  false;
        }



        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) values(?, ?,?,?,?)";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), pendingStatus, getAccountFromUserId(transfer.getAccountFrom()), getAccountFromUserId(transfer.getAccountTo()), transfer.getAmount());

        return true;

    }


    public int convertUserIdToAccount(int id){
        //Code to retrieve the AccountId from the given userId//
        String accountIdSql = "SELECT account_id FROM account WHERE user_id = ?;";

        int accountId = jdbcTemplate.queryForObject(accountIdSql, int.class, id);
        ////////////////////////////////////////////////////////
        return accountId;
    }
    @Override
    public List<Transfer> findAll(int id) {
        List<Transfer> transfers = new ArrayList<>();

      int accountId= convertUserIdToAccount(id);

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE account_from = ? OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId , accountId);
        while (results.next()) {

            Transfer transfer = mapRowToTransfer(results);
            transfer.setAccountFromUN(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountFrom())).getUsername());
            transfer.setAccountToUN(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountTo())).getUsername());
            transfer.setAccountFromUserId(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountFrom())).getId());
            transfer.setAccountToUserId(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountTo())).getId());
            transfers.add(transfer);

        }

        return transfers;
    }

    @Override
    public Transfer findById(int id) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?;";
        Transfer transfer = null;
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if(result.next()){
            transfer = mapRowToTransfer(result);
        }
        return transfer;
    }

    @Override
    public List<Transfer> findByPayee(int accountTo) { //TODO might need to delete this if not using
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountTo);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getPendingRequests(int id){
        int accountId = getAccountFromUserId(id);
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE (account_to = ? OR account_from = ?) AND transfer_status_id = 1 ;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountId, accountId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getPendingSentRequests(int id){
        int accountId = getAccountFromUserId(id);
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE account_from = ? AND transfer_status_id = 1 AND transfer_type_id = 2;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getPendingReceivedRequests(int id){
        int accountId = getAccountFromUserId(id);
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE account_to = ? AND transfer_status_id = 1 AND transfer_type_id = 1;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfer.setAccountFromUN(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountFrom())).getUsername());
            transfer.setAccountToUN(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountTo())).getUsername());
            transfer.setAccountFromUserId(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountFrom())).getId());
            transfer.setAccountToUserId(userDao.getUserById(userDao.findUserIdByAccount(transfer.getAccountTo())).getId());
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public int getAccountFromUserId(int id) {
        String sql = "SELECT account_id FROM account WHERE user_id =?;";

       return jdbcTemplate.queryForObject(sql, int.class, id);

    }

    public void updateTransfer(Transfer transfer){ //maybe make a boolean
        String sql ="UPDATE transfer " +
                "SET transfer_type_id = ?, transfer_status_id = ?, account_from = ?, account_to = ?, amount = ? " +
                "WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(),
                transfer.getAmount(), transfer.getId());
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
