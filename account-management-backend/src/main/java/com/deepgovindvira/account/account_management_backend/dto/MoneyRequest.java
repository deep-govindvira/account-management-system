package com.deepgovindvira.account.account_management_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MoneyRequest(
        @NotNull Long accountId,
        @NotNull @Min(1) Long amountInPaise // avoid floating in transport
) {}
