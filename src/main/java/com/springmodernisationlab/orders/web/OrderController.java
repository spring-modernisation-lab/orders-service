package com.springmodernisationlab.orders.web;

import com.springmodernisationlab.orders.domain.CustomerOrder;
import com.springmodernisationlab.orders.domain.Money;
import com.springmodernisationlab.orders.persistence.CustomerOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CustomerOrderRepository repository;

    public OrderController(CustomerOrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return repository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{reference}")
    @Transactional(readOnly = true)
    public ResponseEntity<OrderResponse> get(@PathVariable String reference) {
        return repository.findByReference(reference)
                .map(OrderResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        CustomerOrder order = repository.save(
                new CustomerOrder(
                        request.getReference(),
                        Money.of(request.getAmount(), request.getCurrency())));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(OrderResponse.from(order));
    }
}
