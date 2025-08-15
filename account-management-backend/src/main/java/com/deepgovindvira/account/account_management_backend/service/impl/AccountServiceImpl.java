package com.deepgovindvira.account.account_management_backend.service.impl;

import com.deepgovindvira.account.account_management_backend.entity.Account;
import com.deepgovindvira.account.account_management_backend.entity.AccountType;
import com.deepgovindvira.account.account_management_backend.entity.CheckingAccount;
import com.deepgovindvira.account.account_management_backend.entity.SavingsAccount;
import com.deepgovindvira.account.account_management_backend.port.InterestCalculator;
import com.deepgovindvira.account.account_management_backend.port.NotificationPort;
import com.deepgovindvira.account.account_management_backend.repository.AccountRepository;
import com.deepgovindvira.account.account_management_backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repo;
    private final List<InterestCalculator> calculators; // OCP: inject all strategies
    private final NotificationPort notifier;

    @Override
    public Account open(AccountType type, String ownerName) {
        Account acc = switch (type) {
            case CHECKING -> new CheckingAccount(ownerName);
            case SAVINGS  -> new SavingsAccount(ownerName);
        };
        Account saved = repo.save(acc);

        notifier.notify(ownerName, String.format(
                "Dear %s,\n\nYour %s account (Account ID: %d) has been successfully created on %s.\n" +
                        "Thank you for banking with us.\n\n- Your Trusted Bank",
                ownerName, type, saved.getId(), LocalDate.now()
        ));

        return saved;
    }

    @Override
    public Account deposit(Long accountId, BigDecimal amount) {
        Account acc = get(accountId);
        acc.deposit(amount);

        notifier.notify(acc.getOwnerName(), String.format(
                "Dear %s,\n\nA deposit of %s has been successfully credited to your account (Account ID: %d) on %s.\n" +
                        "Your updated balance is %s.\n\n- Your Trusted Bank",
                acc.getOwnerName(), amount, acc.getId(), LocalDate.now(), acc.getBalance()
        ));

        return acc;
    }

    @Override
    public Account withdraw(Long accountId, BigDecimal amount) {
        Account acc = get(accountId);
        acc.withdraw(amount);

        notifier.notify(acc.getOwnerName(), String.format(
                "Dear %s,\n\nA withdrawal of %s has been successfully debited from your account (Account ID: %d) on %s.\n" +
                        "Your updated balance is %s.\n\n- Your Trusted Bank",
                acc.getOwnerName(), amount, acc.getId(), LocalDate.now(), acc.getBalance()
        ));

        return acc;
    }

    @Override
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId.equals(toId)) throw new IllegalArgumentException("Cannot transfer to same account");

        Account from = get(fromId);
        Account to = get(toId);

        from.withdraw(amount);
        to.deposit(amount);

        notifier.notify(from.getOwnerName(), String.format(
                "Dear %s,\n\nYou have successfully transferred %s to Account ID: %d on %s.\n" +
                        "Your updated balance is %s.\n\n- Your Trusted Bank",
                from.getOwnerName(), amount, to.getId(), LocalDate.now(), from.getBalance()
        ));

        notifier.notify(to.getOwnerName(), String.format(
                "Dear %s,\n\nYou have received a transfer of %s from Account ID: %d on %s.\n" +
                        "Your updated balance is %s.\n\n- Your Trusted Bank",
                to.getOwnerName(), amount, from.getId(), LocalDate.now(), to.getBalance()
        ));
    }

    @Override
    public List<Account> list() {
        return repo.findAll();
    }

    @Override
    public Account applyMonthlyInterest(Long accountId) {
        Account acc = get(accountId);
        calculators.stream()
                .filter(c -> c.supports(acc))
                .findFirst()
                .ifPresent(c -> {
                    BigDecimal interest = c.calculateMonthlyInterest(acc);
                    if (interest.signum() > 0) {
                        acc.deposit(interest);

                        notifier.notify(acc.getOwnerName(), String.format(
                                "Dear %s,\n\nMonthly interest of %s has been credited to your account (Account ID: %d) on %s.\n" +
                                        "Your updated balance is %s.\n\n- Your Trusted Bank",
                                acc.getOwnerName(), interest, acc.getId(), LocalDate.now(), acc.getBalance()
                        ));
                    }
                });
        return acc;
    }

    @Override
    @Transactional(readOnly = true)
    public Account get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + id));
    }
}
