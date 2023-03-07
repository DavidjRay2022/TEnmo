package com.techelevator.tenmo.model;


import java.math.BigDecimal;
import lombok.Data;
@Data
public class Transfer {


    private int id;
    private int accountFrom;
    private String accountFromUN;

    private int accountTo;
    private String accountToUN;
    private int transferTypeId;
    private BigDecimal amount;

    private int transferStatusId;
    private int accountFromUserId;
    private int accountToUserId;


    @Override
    public String toString(){
        String transferStatus = "";
        String transferType ="";

        switch (transferStatusId){
            case 1: transferStatus = "Pending";
            break;
            case 2: transferStatus ="Approved";
            break;
            case 3: transferStatus = "Rejected";
            break;
        }
        switch (transferTypeId){
            case 1: transferType  = "Requested";
            break;
            case 2: transferType = "Sent";
            break;
        }

        return "=====Transfer===== \n" +
                "Transfer id = " + id + "\n" +
                "Transfer Type = " + transferType + "\n"+
                "From Account = " + accountFrom + " ("+ accountFromUN + ")" + "\n"+
                "To Account = " + accountTo +" (" + accountToUN + ")" +"\n" +
                "Of Amount: " + amount +"\n" +
                "Transfer Status = " + transferStatus + "\n";
    }
}
