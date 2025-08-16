package com.deepgovindvira.account.account_management_backend.service.impl;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;
import com.deepgovindvira.account.account_management_backend.entity.SavingsAccount;
import com.deepgovindvira.account.account_management_backend.port.InterestCalculator;
import com.deepgovindvira.account.account_management_backend.port.NotificationPort;
import com.deepgovindvira.account.account_management_backend.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountRepository repo;
    private NotificationPort notifier;
    private AccountServiceImpl service;
    private InterestCalculator calculator;

    @BeforeEach
    void setup() {
        repo = mock(AccountRepository.class);
        notifier = mock(NotificationPort.class);
        calculator = mock(InterestCalculator.class);

        service = new AccountServiceImpl(repo, List.of(calculator), notifier);
    }

    @Test
    void testOpenAccountSendsNotification() {
        Account account = new SavingsAccount("Alice");
        when(repo.save(any())).thenReturn(account);

        Account acc = service.open(AccountType.SAVINGS, "Alice");

        assertNotNull(acc);
        verify(repo).save(any());
        verify(notifier).notify(eq("Alice"), anyString());
    }

    @Test
    void testDepositSendsNotification() {
        Account acc = mock(Account.class);
        when(acc.getOwnerName()).thenReturn("Bob");
        when(acc.getId()).thenReturn(1L);
        when(acc.getBalance()).thenReturn(BigDecimal.valueOf(150));
        when(repo.findById(1L)).thenReturn(Optional.of(acc));

        service.deposit(1L, BigDecimal.valueOf(50));

        verify(acc).deposit(BigDecimal.valueOf(50));
        verify(notifier).notify(eq("Bob"), anyString());
    }

    @Test
    void testWithdrawSendsNotification() {
        Account acc = mock(Account.class);
        when(acc.getOwnerName()).thenReturn("Carol");
        when(acc.getId()).thenReturn(2L);
        when(acc.getBalance()).thenReturn(BigDecimal.valueOf(200));
        when(repo.findById(2L)).thenReturn(Optional.of(acc));

        service.withdraw(2L, BigDecimal.valueOf(75));

        verify(acc).withdraw(BigDecimal.valueOf(75));
        verify(notifier).notify(eq("Carol"), anyString());
    }

    @Test
    void testTransferSendsNotifications() {
        Account from = mock(Account.class);
        Account to = mock(Account.class);

        when(from.getOwnerName()).thenReturn("Dave");
        when(from.getId()).thenReturn(3L);
        when(from.getBalance()).thenReturn(BigDecimal.valueOf(500));
        when(to.getOwnerName()).thenReturn("Eve");
        when(to.getId()).thenReturn(4L);
        when(to.getBalance()).thenReturn(BigDecimal.valueOf(100));

        when(repo.findById(3L)).thenReturn(Optional.of(from));
        when(repo.findById(4L)).thenReturn(Optional.of(to));

        service.transfer(3L, 4L, BigDecimal.valueOf(50));

        verify(from).withdraw(BigDecimal.valueOf(50));
        verify(to).deposit(BigDecimal.valueOf(50));

        verify(notifier).notify(eq("Dave"), anyString());
        verify(notifier).notify(eq("Eve"), anyString());
    }

    @Test
    void testTransferToSameAccountThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.transfer(5L, 5L, BigDecimal.valueOf(10)));
        assertEquals("Cannot transfer to same account", ex.getMessage());
    }

    @Test
    void testApplyMonthlyInterestSendsNotification() {
        Account acc = mock(Account.class);
        when(acc.getOwnerName()).thenReturn("Frank");
        when(acc.getId()).thenReturn(6L);
        when(acc.getBalance()).thenReturn(BigDecimal.valueOf(1000));
        when(repo.findById(6L)).thenReturn(Optional.of(acc));

        when(calculator.supports(acc)).thenReturn(true);
        when(calculator.calculateMonthlyInterest(acc)).thenReturn(BigDecimal.valueOf(50));

        service.applyMonthlyInterest(6L);

        verify(acc).deposit(BigDecimal.valueOf(50));
        verify(notifier).notify(eq("Frank"), anyString());
    }

    @Test
    void testGetAccountFound() {
        Account acc = mock(Account.class);
        when(repo.findById(7L)).thenReturn(Optional.of(acc));

        Account result = service.get(7L);

        assertEquals(acc, result);
    }

    @Test
    void testGetAccountNotFound() {
        when(repo.findById(8L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.get(8L));
        assertEquals("Account not found: 8", ex.getMessage());
    }

    @Test
    void testListAccounts() {
        List<Account> accounts = List.of(mock(Account.class), mock(Account.class));
        when(repo.findAll()).thenReturn(accounts);

        List<Account> result = service.list();

        assertEquals(2, result.size());
    }
}

