package com.techelevator.tenmo.model;
import java.math.BigDecimal; //maybe?
import java.text.NumberFormat;
import java.util.Locale;
import javax.validation.constraints.NotEmpty;

public class Account {



    private int balance;
    @NotEmpty
    private int accountId;
    private int userId;

    private BigDecimal bd = new BigDecimal(this.balance); //in the function that displays the balance w/ a money format.
    private NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US); //in the function that displays the balance w/ a money format.


    public Account(){}

    public Account(int balance, int accountId, int userId){
        this.balance = balance;
        this.accountId = accountId;
        this.userId = userId;
    }
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }

    public String balanceAsBD(){ //maybe not needed
        return currency.format(bd);
    }

    @Override
    public String toString(){
        return "Account Id: [" + accountId + "]/n " + "User Id: [" + userId + "]" + "/n"+
                "Balance : " + balanceAsBD();
    }
}
