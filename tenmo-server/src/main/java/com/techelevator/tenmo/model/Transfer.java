package com.techelevator.tenmo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data  //uses lombok to set up getters, setters, and constructor
public class Transfer {

//TODO set up annotations
private int id;
private int transferTypeId;
private int transferStatusId;
private int accountFrom;
private int accountTo;
private BigDecimal amount;



}
