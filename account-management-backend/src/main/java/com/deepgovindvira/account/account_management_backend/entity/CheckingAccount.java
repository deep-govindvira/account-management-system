package com.deepgovindvira.account.account_management_backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends Account {
    public CheckingAccount(String ownerName) {
        super(null, AccountType.CHECKING, ownerName, java.math.BigDecimal.ZERO);
    }
}
