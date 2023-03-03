package com.techelevator.tenmo.Service;

import java.math.BigDecimal;
import com.techelevator.tenmo.model.Account;

public interface AccountService {

    BigDecimal balance(int id);

    Account findAccountById(int id);



}
