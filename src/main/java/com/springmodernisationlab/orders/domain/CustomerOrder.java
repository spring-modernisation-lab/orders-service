package com.springmodernisationlab.orders.domain;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String reference;

    @Type(type = "com.springmodernisationlab.orders.persistence.MoneyUserType")
    @Column(name = "total", nullable = false, length = 32)
    private Money total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private OrderStatus status;

    protected CustomerOrder() {
        // Required by JPA.
    }

    public CustomerOrder(String reference, Money total) {
        this.reference = reference;
        this.total = total;
        this.status = OrderStatus.PLACED;
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public Money getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
