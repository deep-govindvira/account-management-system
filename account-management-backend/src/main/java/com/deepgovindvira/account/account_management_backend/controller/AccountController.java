package com.deepgovindvira.account.account_management_backend.controller;

import com.deepgovindvira.account.account_management_backend.dto.AccountResponse;
import com.deepgovindvira.account.account_management_backend.dto.CreateAccountRequest;
import com.deepgovindvira.account.account_management_backend.dto.MoneyRequest;
import com.deepgovindvira.account.account_management_backend.dto.TransferRequest;
import com.deepgovindvira.account.account_management_backend.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    @PostMapping
    public AccountResponse open(@Valid @RequestBody CreateAccountRequest req) {
        return AccountResponse.of(service.open(req.type(), req.ownerName()));
    }

    @GetMapping
    public List<AccountResponse> list() {
        return service.list().stream().map(AccountResponse::of).toList();
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable("id") Long id) {
        return AccountResponse.of(service.get(id));
    }

    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable("id") Long id,
                                   @Valid @RequestBody MoneyRequest req) {
        var amount = paiseToBigDecimal(req.amountInPaise());
        return AccountResponse.of(service.deposit(id, amount));
    }

    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable("id") Long id,
                                    @Valid @RequestBody MoneyRequest req) {
        var amount = paiseToBigDecimal(req.amountInPaise());
        return AccountResponse.of(service.withdraw(id, amount));
    }

    @PostMapping("/transfer")
    public void transfer(@Valid @RequestBody TransferRequest req) {
        service.transfer(req.fromAccountId(), req.toAccountId(), paiseToBigDecimal(req.amountInPaise()));
    }

    @PostMapping("/{id}/apply-interest")
    public AccountResponse applyInterest(@PathVariable("id") Long id) {
        return AccountResponse.of(service.applyMonthlyInterest(id));
    }

    private BigDecimal paiseToBigDecimal(Long paise) {
        return new BigDecimal(paise).movePointLeft(2);
    }
}
