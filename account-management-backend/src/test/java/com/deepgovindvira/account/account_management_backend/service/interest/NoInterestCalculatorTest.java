package com.deepgovindvira.account.account_management_backend.service.interest;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NoInterestCalculatorTest {

    private NoInterestCalculator calculator;
    private Account checkingAccount;
    private Account savingsAccount;

    @BeforeEach
    void setUp() {
        calculator = new NoInterestCalculator();

        checkingAccount = new Account() {
            @Override
            public AccountType getType() {
                return AccountType.CHECKING;
            }
        };

        savingsAccount = new Account() {
            @Override
            public AccountType getType() {
                return AccountType.SAVINGS;
            }
        };
    }

    @Test
    void testSupportsCheckingAccount() {
        assertTrue(calculator.supports(checkingAccount),
                "NoInterestCalculator should support CHECKING accounts");
    }

    @Test
    void testDoesNotSupportSavingsAccount() {
        assertFalse(calculator.supports(savingsAccount),
                "NoInterestCalculator should not support SAVINGS accounts");
    }

    @Test
    void testCalculateMonthlyInterestReturnsZero() {
        BigDecimal interest = calculator.calculateMonthlyInterest(checkingAccount);
        assertEquals(BigDecimal.ZERO, interest, "Monthly interest for CHECKING should be zero");
    }
}
