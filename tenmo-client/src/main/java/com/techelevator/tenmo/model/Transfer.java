package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int fromAccount;
    private int toAccount;
    private int transferTypeId;
    private BigDecimal amount;

    public Transfer() {
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
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

    @Override
    public String toString() {
        return "Transfer{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", transferType=" + transferTypeId +
                ", amount=" + amount +
                '}';
    }
}
