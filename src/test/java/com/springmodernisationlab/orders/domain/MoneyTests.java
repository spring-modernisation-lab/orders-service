package com.springmodernisationlab.orders.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTests {

    @Test
    void formatsAndParsesRoundTrip() {
        Money money = Money.of(new BigDecimal("12.5"), "gbp");

        assertThat(money.format()).isEqualTo("12.50 GBP");
        assertThat(Money.parse(money.format())).isEqualTo(money);
    }

    @Test
    void rejectsInvalidCurrency() {
        assertThatThrownBy(() -> Money.of(BigDecimal.ONE, "POUNDS"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsMalformedValues() {
        assertThatThrownBy(() -> Money.parse("12.50"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
