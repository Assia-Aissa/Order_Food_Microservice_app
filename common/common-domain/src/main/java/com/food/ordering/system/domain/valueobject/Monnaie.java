package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Monnaie {
    private final BigDecimal amount;

    public static final Monnaie ZERO = new Monnaie(BigDecimal.ZERO);

    public Monnaie(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Monnaie monnaie) {
        return this.amount != null && this.amount.compareTo(monnaie.getAmount()) > 0;
    }

    public Monnaie add(Monnaie monnaie) {
        return new Monnaie(setScale(this.amount.add(monnaie.getAmount())));
    }

    public Monnaie subtract(Monnaie monnaie) {
        return new Monnaie(setScale(this.amount.subtract(monnaie.getAmount())));
    }

    public Monnaie multiply(int multiplier) {
        return new Monnaie(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Monnaie monnaie = (Monnaie) o;
        return amount.equals(monnaie.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }
}
