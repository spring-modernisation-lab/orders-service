package com.springmodernisationlab.orders.web;

import com.springmodernisationlab.orders.domain.CustomerOrder;

import java.math.BigDecimal;

public class OrderResponse {

    private final Long id;
    private final String reference;
    private final BigDecimal amount;
    private final String currency;
    private final String status;

    private OrderResponse(
            Long id,
            String reference,
            BigDecimal amount,
            String currency,
            String status) {

        this.id = id;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }

    public static OrderResponse from(CustomerOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotal().getAmount(),
                order.getTotal().getCurrency(),
                order.getStatus().name());
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }
}
