package com.deepgovindvira.account.account_management_backend.service;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account open(AccountType type, String ownerName);
    Account deposit(Long accountId, BigDecimal amount);
    Account withdraw(Long accountId, BigDecimal amount);
    void transfer(Long fromId, Long toId, BigDecimal amount);
    List<Account> list();
    Account applyMonthlyInterest(Long accountId);
    Account get(Long id);
}

