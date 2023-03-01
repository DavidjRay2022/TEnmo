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
@NotNull
private int accountTo;
@NotNull
private BigDecimal amount;



}
