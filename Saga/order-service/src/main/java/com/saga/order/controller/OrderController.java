package com.saga.order.controller;


import com.saga.shared.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import com.saga.order.event.OrderTimeoutWatcher;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @PostMapping
    public String createOrder(@RequestBody OrderCreatedEvent order) {
        logger.info("Received order creation request for orderId: {}", order.getOrderId());
        // Simulate order validation
        if (order.getAmount() == null || order.getAmount() <= 0) {
            logger.warn("Order validation failed for orderId: {}. Invalid amount: {}", order.getOrderId(), order.getAmount());
            return "Order validation failed: amount must be positive.";
        }
        logger.info("Order validated for orderId: {}. Amount: {}", order.getOrderId(), order.getAmount());
        order.setStatus("CREATED");
        logger.info("Order status set to CREATED for orderId: {}", order.getOrderId());
        kafkaTemplate.send("order-created", order);
    // Register for timeout/ack tracking
    OrderTimeoutWatcher.addPendingOrder(order.getOrderId().toString());
        logger.info("Published OrderCreatedEvent for orderId: {}", order.getOrderId());
        return "Order created and event published";
    }
}
