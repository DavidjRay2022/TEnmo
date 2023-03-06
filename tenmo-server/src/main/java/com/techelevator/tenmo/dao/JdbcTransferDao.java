package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean create(Transfer transfer) {
        if (transfer.getAccountFrom() == transfer.getAccountTo()){
            return  false;
        }

        int pendingStatus = 1;
       String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) values(?, ?,?,?,?)";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), pendingStatus, getAccountFromUserId(transfer.getAccountFrom()), getAccountFromUserId(transfer.getAccountTo()), transfer.getAmount());

        return true;

    }

    //TODO added to methods so they can retrieve the accountId versus the userid
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
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Transfer findByToId(int id) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);


        return mapRowToTransfer(result);
    }

    @Override
    public List<Transfer> findByPayee(int accountTo) {
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
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public int getAccountFromUserId(int id) {
        String sql = "SELECT account_id FROM account WHERE user_id =?;";

       return jdbcTemplate.queryForObject(sql, int.class, id);

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
