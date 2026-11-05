package com.springmodernisationlab.orders.domain;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable monetary amount stored as "amount currency", for example "12.50 GBP".
 */
public final class Money implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");

        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a three-letter ISO code: " + currency);
        }

        return new Money(amount, currency.toUpperCase());
    }

    public static Money parse(String value) {
        Objects.requireNonNull(value, "value");

        String[] parts = value.trim().split(" ");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid money value: " + value);
        }

        return of(new BigDecimal(parts[0]), parts[1]);
    }

    public String format() {
        return amount.toPlainString() + " " + currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Money)) {
            return false;
        }

        Money money = (Money) other;

        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return format();
    }
}
