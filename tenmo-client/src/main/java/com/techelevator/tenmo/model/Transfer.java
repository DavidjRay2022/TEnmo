package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int accountFrom;
    private int accountTo;
    private int transferTypeId;
    private BigDecimal amount;

    private int transferStatusId;

    public Transfer() {
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransferStatusId(int transferStatusId){
        this.transferStatusId = transferStatusId;
    }

    public int getTransferStatusId(){
        return transferStatusId;
    }

    //TODO fix the formatting of this to show everything they want to see in the readme
@Override public String toString(){
        return "Transfer: , accountFrom, accountTo, TransferType, TransferStatus, Ammount" +
                accountFrom +" " + accountTo + " " + transferTypeId + " " + transferStatusId + " " + amount;
}
//    @Override
//    public String toString(){
//        //TODO add transfer status if permits
//        String transferType = "";
//        switch (transferTypeId){
//            case 1:
//                transferType = "Request";
//                break;
//            case 2:
//                transferType = "Send";
//                break;
//        }
//
//
//        return "=====Transfer===== \nFrom Account = " + accountFrom + "\nTo Account = " +
//                accountTo + "\nTransfer Type = " + transferType + "\nAmount = " + amount + "\n";
//    }
}
