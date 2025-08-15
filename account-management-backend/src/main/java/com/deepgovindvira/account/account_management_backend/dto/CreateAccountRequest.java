package com.deepgovindvira.account.account_management_backend.dto;

import com.deepgovindvira.account.account_management_backend.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest (
        @NotNull AccountType type,
        @NotBlank String ownerName
) {}