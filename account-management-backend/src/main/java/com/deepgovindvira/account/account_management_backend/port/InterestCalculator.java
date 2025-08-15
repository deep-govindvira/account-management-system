package com.deepgovindvira.account.account_management_backend.port;

import com.deepgovindvira.account.account_management_backend.entity.Account;

import java.math.BigDecimal;

public interface InterestCalculator {
    boolean supports(Account account);
    BigDecimal calculateMonthlyInterest(Account account);
}
