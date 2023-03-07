package com.techelevator.tenmo.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data  //uses lombok to set up getters, setters, and constructor
public class Transfer {

private int id;
private int transferTypeId;
private int transferStatusId;
@NotNull
private int accountFrom;
private String accountFromUN;
@NotNull
private int accountTo;
private String accountToUN;
private int accountFromUserId;
private int accountToUserId;
@NotNull
private BigDecimal amount;



}
