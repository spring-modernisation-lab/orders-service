package com.springmodernisationlab.orders.persistence;

import com.springmodernisationlab.orders.domain.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    Optional<CustomerOrder> findByReference(String reference);
}
