package com.springmodernisationlab.orders.domain;

import org.hibernate.annotations.Type;
import com.springmodernisationlab.orders.persistence.MoneyUserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String reference;

    @Type(MoneyUserType.class)
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
