package com.deepgovindvira.account.account_management_backend.service.interest;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;
import com.deepgovindvira.account.account_management_backend.port.InterestCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SavingsInterestCalculator implements InterestCalculator {
    private static final BigDecimal RATE = new BigDecimal("0.03"); // 3% yearly ~ 0.25% monthly

    @Override
    public boolean supports(Account account) {
        return account.getType() == AccountType.SAVINGS;
    }

    @Override
    public BigDecimal calculateMonthlyInterest(Account account) {
        return account.getBalance().multiply(RATE).divide(new BigDecimal("12"));
    }
}

