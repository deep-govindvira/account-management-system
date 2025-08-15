package com.deepgovindvira.account.account_management_backend.dto;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;

import java.math.BigDecimal;

public record AccountResponse(Long id, AccountType type, String ownerName, BigDecimal balance) {
    public static AccountResponse of(Account a) {
        return new AccountResponse(a.getId(), a.getType(), a.getOwnerName(), a.getBalance());
    }
}

