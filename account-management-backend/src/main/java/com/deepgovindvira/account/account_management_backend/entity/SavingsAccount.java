package com.deepgovindvira.account.account_management_backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {
    public SavingsAccount(String ownerName) {
        super(null, AccountType.SAVINGS, ownerName, java.math.BigDecimal.ZERO);
    }
}
