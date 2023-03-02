package com.techelevator.tenmo.exceptions;

public class InsufficientFunds extends Exception {
    public InsufficientFunds() {
        super("Not enough money in account; please reload");
    }
}
