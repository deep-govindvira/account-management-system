package com.deepgovindvira.account.account_management_backend.service.interest;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SavingsInterestCalculatorTest {

    private SavingsInterestCalculator calculator;
    private Account savingsAccount;
    private Account checkingAccount;

    @BeforeEach
    void setUp() {
        calculator = new SavingsInterestCalculator();

        savingsAccount = new Account() {
            @Override
            public AccountType getType() {
                return AccountType.SAVINGS;
            }

            @Override
            public BigDecimal getBalance() {
                return new BigDecimal("1200");
            }
        };

        checkingAccount = new Account() {
            @Override
            public AccountType getType() {
                return AccountType.CHECKING;
            }

            @Override
            public BigDecimal getBalance() {
                return new BigDecimal("1000");
            }
        };
    }

    @Test
    void testSupportsSavingsAccount() {
        assertTrue(calculator.supports(savingsAccount),
                "SavingsInterestCalculator should support SAVINGS accounts");
    }

    @Test
    void testDoesNotSupportCheckingAccount() {
        assertFalse(calculator.supports(checkingAccount),
                "SavingsInterestCalculator should not support CHECKING accounts");
    }

    @Test
    void testCalculateMonthlyInterest() {
        // expected: 1200 * 0.03 / 12 = 3.0
        BigDecimal expected = new BigDecimal("3.0");
        BigDecimal actual = calculator.calculateMonthlyInterest(savingsAccount);
        assertEquals(0, expected.compareTo(actual), "Monthly interest should be 3.0");
    }
}
