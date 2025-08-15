package com.deepgovindvira.account.account_management_backend.service.interest;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;
import com.deepgovindvira.account.account_management_backend.port.InterestCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NoInterestCalculator implements InterestCalculator {
    @Override
    public boolean supports(Account account) {
        return account.getType() == AccountType.CHECKING;
    }

    @Override
    public BigDecimal calculateMonthlyInterest(Account account) {
        return BigDecimal.ZERO;
    }
}

